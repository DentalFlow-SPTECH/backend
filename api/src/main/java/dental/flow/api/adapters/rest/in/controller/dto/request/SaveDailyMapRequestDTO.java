package dental.flow.api.adapters.rest.in.controller.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record SaveDailyMapRequestDTO(
        LocalDate date,
        BigDecimal cardDiscount,
        BigDecimal predatedCheckDiscount,
        BigDecimal cashCheckDiscount,
        BigDecimal otherDiscount,
        Integer moldingCount,
        Integer biteCount,
        Integer fittingCount,
        Integer deliveryCount,
        Integer repetitionCount,
        String notes,
        List<VisitInput> visits,
        List<VoucherInput> vouchers,
        List<ExpenseInput> expenses,
        List<TeamScheduleInput> teamSchedules
) {
    public record VisitInput(Long appointmentId, Long patientId, Long procedureId, Integer lineNumber,
                             String recordNumber, String patientName, LocalTime scheduledTime,
                             LocalTime entryTime, LocalTime exitTime, LocalTime returnTime,
                             String paymentCode, BigDecimal value, String type, Boolean checked) {
    }

    public record VoucherInput(String responsibleName, String recordNumber, BigDecimal value,
                               String description, Boolean checked) {
    }

    public record ExpenseInput(String description, BigDecimal value) {
    }

    public record TeamScheduleInput(Long userId, String employeeName, String role, LocalTime startTime,
                                    LocalTime lunchOutTime, LocalTime lunchReturnTime, LocalTime endTime) {
    }
}
