package dental.flow.api.domain.dailymap;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DailyMap {
    public record Visit(Long id, Long appointmentId, Long patientId, Long dentistId, Long procedureId,
                        Integer lineNumber, String recordNumber, String patientName, LocalTime scheduledTime,
                        LocalTime entryTime, LocalTime exitTime, LocalTime returnTime, String paymentCode,
                        BigDecimal value, String type, Boolean checked) {
    }

    public record Voucher(Long id, String responsibleName, String recordNumber, BigDecimal value,
                          String description, Boolean checked) {
    }

    public record Expense(Long id, String description, BigDecimal value) {
    }

    public record TeamSchedule(Long id, Long userId, String employeeName, String role, LocalTime startTime,
                               LocalTime lunchOutTime, LocalTime lunchReturnTime, LocalTime endTime) {
    }

    private Long id;
    private final Long clinicId;
    private final Long dentistId;
    private final Long attendantUserId;
    private Long checkingUserId;
    private final LocalDate date;
    private LocalDate conferenceDate;
    private LocalDateTime closedAt;
    private String status;
    private final BigDecimal clinicalTotal, prosthesisTotal, clinicalGross, pediatricSurgeryGross,
            generalGross, totalExpenses, netValue, cardDiscount, predatedCheckDiscount,
            cashCheckDiscount, voucherDiscount, otherDiscount, netTotal, finalBalance;
    private final Integer patientCount, moldingCount, biteCount, fittingCount, deliveryCount, repetitionCount;
    private final String notes;
    private final List<Visit> visits;
    private final List<Voucher> vouchers;
    private final List<Expense> expenses;
    private final List<TeamSchedule> teamSchedules;

    public DailyMap(Long clinicId, Long dentistId, Long attendantUserId, LocalDate date,
                    BigDecimal cardDiscount, BigDecimal predatedCheckDiscount, BigDecimal cashCheckDiscount,
                    BigDecimal otherDiscount, Integer moldingCount, Integer biteCount, Integer fittingCount,
                    Integer deliveryCount, Integer repetitionCount, String notes, List<Visit> visits,
                    List<Voucher> vouchers, List<Expense> expenses, List<TeamSchedule> teamSchedules) {
        if (clinicId == null || clinicId <= 0 || dentistId == null || dentistId <= 0 || date == null) {
            throw error("Clínica, dentista e data são obrigatórios no mapa diário.");
        }
        this.clinicId = clinicId;
        this.dentistId = dentistId;
        this.attendantUserId = attendantUserId;
        this.date = date;
        this.visits = List.copyOf(visits == null ? List.of() : visits);
        this.vouchers = List.copyOf(vouchers == null ? List.of() : vouchers);
        this.expenses = List.copyOf(expenses == null ? List.of() : expenses);
        this.teamSchedules = List.copyOf(teamSchedules == null ? List.of() : teamSchedules);
        validateLines();
        this.cardDiscount = nonNegative(cardDiscount, "Desconto de cartões");
        this.predatedCheckDiscount = nonNegative(predatedCheckDiscount, "Desconto de cheque pré-datado");
        this.cashCheckDiscount = nonNegative(cashCheckDiscount, "Desconto de cheque à vista");
        this.otherDiscount = nonNegative(otherDiscount, "Outros descontos");
        this.voucherDiscount = sum(this.vouchers.stream().map(Voucher::value).toList());
        this.clinicalTotal = sumByType("CLINICO");
        this.prosthesisTotal = sumByType("PROTESE");
        this.pediatricSurgeryGross = sumByType("PEDIATRIA_CIRURGIA");
        this.clinicalGross = clinicalTotal.add(prosthesisTotal);
        this.generalGross = clinicalGross.add(pediatricSurgeryGross);
        this.totalExpenses = sum(this.expenses.stream().map(Expense::value).toList());
        BigDecimal discounts = this.cardDiscount.add(this.predatedCheckDiscount).add(this.cashCheckDiscount)
                .add(this.voucherDiscount).add(this.otherDiscount);
        this.netTotal = generalGross.subtract(discounts);
        this.netValue = netTotal;
        this.finalBalance = netTotal.subtract(totalExpenses);
        this.patientCount = this.visits.size();
        this.moldingCount = count(moldingCount);
        this.biteCount = count(biteCount);
        this.fittingCount = count(fittingCount);
        this.deliveryCount = count(deliveryCount);
        this.repetitionCount = count(repetitionCount);
        if (notes != null && notes.length() > 4000) {
            throw error("As observações devem ter no máximo 4000 caracteres.");
        }
        this.notes = notes == null || notes.isBlank() ? null : notes.trim();
        this.status = "ABERTO";
    }

    private void validateLines() {
        Set<Integer> lines = new HashSet<>();
        for (Visit visit : visits) {
            if (visit.lineNumber() == null || visit.lineNumber() <= 0 || !lines.add(visit.lineNumber())) {
                throw error("As linhas dos atendimentos devem ser positivas e únicas.");
            }
            if (visit.value() == null || visit.value().signum() < 0) {
                throw error("O valor do atendimento não pode ser negativo.");
            }
            if (!Set.of("CLINICO", "PROTESE", "PEDIATRIA_CIRURGIA").contains(visit.type())) {
                throw error("Tipo de atendimento inválido.");
            }
        }
        for (Voucher voucher : vouchers) {
            if (voucher.value() == null || voucher.value().signum() < 0) {
                throw error("O valor do vale não pode ser negativo.");
            }
        }
        for (Expense expense : expenses) {
            boolean missingDescription = expense.description() == null || expense.description().isBlank();
            boolean invalidValue = expense.value() == null || expense.value().signum() < 0;
            if (missingDescription || invalidValue) {
                throw error("Descrição e valor não negativo são obrigatórios na despesa.");
            }
        }
    }

    public void restore(Long id, String status, Long checkingUserId, LocalDate conferenceDate, LocalDateTime closedAt) {
        this.id = id;
        this.status = status;
        this.checkingUserId = checkingUserId;
        this.conferenceDate = conferenceDate;
        this.closedAt = closedAt;
    }

    public void close(Long userId) {
        if (!"ABERTO".equals(status)) {
            throw error("O mapa diário já está fechado.");
        }
        if (visits.stream().anyMatch(v -> !Boolean.TRUE.equals(v.checked()))
                || vouchers.stream().anyMatch(v -> !Boolean.TRUE.equals(v.checked()))) {
            throw error("Todos os atendimentos e vales devem estar conferidos antes do fechamento.");
        }
        status = "FECHADO";
        checkingUserId = userId;
        conferenceDate = LocalDate.now();
        closedAt = LocalDateTime.now();
    }

    private BigDecimal sumByType(String type) {
        BigDecimal total = BigDecimal.ZERO;
        for (Visit visit : visits) {
            if (type.equals(visit.type())) {
                total = total.add(visit.value());
            }
        }
        return total;
    }

    private static BigDecimal sum(List<BigDecimal> values) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            total = total.add(value);
        }
        return total;
    }

    private static BigDecimal nonNegative(BigDecimal value, String field) {
        BigDecimal result = value == null ? BigDecimal.ZERO : value;
        if (result.signum() < 0) {
            throw error(field + " não pode ser negativo.");
        }
        return result;
    }

    private static int count(Integer value) {
        if (value != null && value < 0) {
            throw error("Contadores não podem ser negativos.");
        }
        return value == null ? 0 : value;
    }

    private static DomainException error(String message) {
        return new DomainException(Failures.DOMAIN_ERROR, message);
    }

    public Long getId() {
        return id;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public Long getDentistId() {
        return dentistId;
    }

    public Long getAttendantUserId() {
        return attendantUserId;
    }

    public Long getCheckingUserId() {
        return checkingUserId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getConferenceDate() {
        return conferenceDate;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getClinicalTotal() {
        return clinicalTotal;
    }

    public BigDecimal getProsthesisTotal() {
        return prosthesisTotal;
    }

    public BigDecimal getClinicalGross() {
        return clinicalGross;
    }

    public BigDecimal getPediatricSurgeryGross() {
        return pediatricSurgeryGross;
    }

    public BigDecimal getGeneralGross() {
        return generalGross;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public BigDecimal getCardDiscount() {
        return cardDiscount;
    }

    public BigDecimal getPredatedCheckDiscount() {
        return predatedCheckDiscount;
    }

    public BigDecimal getCashCheckDiscount() {
        return cashCheckDiscount;
    }

    public BigDecimal getVoucherDiscount() {
        return voucherDiscount;
    }

    public BigDecimal getOtherDiscount() {
        return otherDiscount;
    }

    public BigDecimal getNetTotal() {
        return netTotal;
    }

    public BigDecimal getFinalBalance() {
        return finalBalance;
    }

    public Integer getPatientCount() {
        return patientCount;
    }

    public Integer getMoldingCount() {
        return moldingCount;
    }

    public Integer getBiteCount() {
        return biteCount;
    }

    public Integer getFittingCount() {
        return fittingCount;
    }

    public Integer getDeliveryCount() {
        return deliveryCount;
    }

    public Integer getRepetitionCount() {
        return repetitionCount;
    }

    public String getNotes() {
        return notes;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public List<Voucher> getVouchers() {
        return vouchers;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<TeamSchedule> getTeamSchedules() {
        return teamSchedules;
    }
}
