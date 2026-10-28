package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.dailymap.DailyMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DailyMapResponseDTO(Long id, Long clinicId, Long dentistId, Long attendantUserId,
                                  Long checkingUserId, LocalDate date, LocalDate conferenceDate, LocalDateTime closedAt,
                                  String status,
                                  BigDecimal clinicalTotal, BigDecimal prosthesisTotal, BigDecimal clinicalGross,
                                  BigDecimal pediatricSurgeryGross,
                                  BigDecimal generalGross, BigDecimal totalExpenses, BigDecimal cardDiscount,
                                  BigDecimal predatedCheckDiscount,
                                  BigDecimal cashCheckDiscount, BigDecimal voucherDiscount, BigDecimal otherDiscount,
                                  BigDecimal totalDiscounts,
                                  BigDecimal netTotal, BigDecimal finalBalance, Integer patientCount,
                                  Integer moldingCount, Integer biteCount,
                                  Integer fittingCount, Integer deliveryCount, Integer repetitionCount, String notes,
                                  List<DailyMap.Visit> visits, List<DailyMap.Voucher> vouchers,
                                  List<DailyMap.Expense> expenses,
                                  List<DailyMap.TeamSchedule> teamSchedules) {
    public static DailyMapResponseDTO from(DailyMap map) {
        BigDecimal discounts = map.getCardDiscount()
                .add(map.getPredatedCheckDiscount())
                .add(map.getCashCheckDiscount())
                .add(map.getVoucherDiscount())
                .add(map.getOtherDiscount());

        return new DailyMapResponseDTO(
                map.getId(),
                map.getClinicId(),
                map.getDentistId(),
                map.getAttendantUserId(),
                map.getCheckingUserId(),
                map.getDate(),
                map.getConferenceDate(),
                map.getClosedAt(),
                map.getStatus(),
                map.getClinicalTotal(),
                map.getProsthesisTotal(),
                map.getClinicalGross(),
                map.getPediatricSurgeryGross(),
                map.getGeneralGross(),
                map.getTotalExpenses(),
                map.getCardDiscount(),
                map.getPredatedCheckDiscount(),
                map.getCashCheckDiscount(),
                map.getVoucherDiscount(),
                map.getOtherDiscount(),
                discounts,
                map.getNetTotal(),
                map.getFinalBalance(),
                map.getPatientCount(),
                map.getMoldingCount(),
                map.getBiteCount(),
                map.getFittingCount(),
                map.getDeliveryCount(),
                map.getRepetitionCount(),
                map.getNotes(),
                map.getVisits(),
                map.getVouchers(),
                map.getExpenses(),
                map.getTeamSchedules()
        );
    }
}
