package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.dailymap.DailyMap;
import java.math.BigDecimal; import java.time.*; import java.util.List;

public record DailyMapResponseDTO(Long id, Long clinicId, Long dentistId, Long attendantUserId,
 Long checkingUserId, LocalDate date, LocalDate conferenceDate, LocalDateTime closedAt, String status,
 BigDecimal clinicalTotal, BigDecimal prosthesisTotal, BigDecimal clinicalGross, BigDecimal pediatricSurgeryGross,
 BigDecimal generalGross, BigDecimal totalExpenses, BigDecimal cardDiscount, BigDecimal predatedCheckDiscount,
 BigDecimal cashCheckDiscount, BigDecimal voucherDiscount, BigDecimal otherDiscount, BigDecimal totalDiscounts,
 BigDecimal netTotal, BigDecimal finalBalance, Integer patientCount, Integer moldingCount, Integer biteCount,
 Integer fittingCount, Integer deliveryCount, Integer repetitionCount, String notes,
 List<DailyMap.Visit> visits, List<DailyMap.Voucher> vouchers, List<DailyMap.Expense> expenses,
 List<DailyMap.TeamSchedule> teamSchedules) {
 public static DailyMapResponseDTO from(DailyMap m){ BigDecimal discounts=m.getCardDiscount().add(m.getPredatedCheckDiscount()).add(m.getCashCheckDiscount()).add(m.getVoucherDiscount()).add(m.getOtherDiscount()); return new DailyMapResponseDTO(m.getId(),m.getClinicId(),m.getDentistId(),m.getAttendantUserId(),m.getCheckingUserId(),m.getDate(),m.getConferenceDate(),m.getClosedAt(),m.getStatus(),m.getClinicalTotal(),m.getProsthesisTotal(),m.getClinicalGross(),m.getPediatricSurgeryGross(),m.getGeneralGross(),m.getTotalExpenses(),m.getCardDiscount(),m.getPredatedCheckDiscount(),m.getCashCheckDiscount(),m.getVoucherDiscount(),m.getOtherDiscount(),discounts,m.getNetTotal(),m.getFinalBalance(),m.getPatientCount(),m.getMoldingCount(),m.getBiteCount(),m.getFittingCount(),m.getDeliveryCount(),m.getRepetitionCount(),m.getNotes(),m.getVisits(),m.getVouchers(),m.getExpenses(),m.getTeamSchedules()); }
}
