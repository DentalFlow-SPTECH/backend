package dental.flow.api.domain.dashboard;

import java.math.BigDecimal;

public record MonthlyDashboard(
        Long clinicId,
        String clinicName,
        Long dentistId,
        String dentistName,
        Integer year,
        Integer month,
        Long registeredDays,
        BigDecimal clinicalTotal,
        BigDecimal prosthesisTotal,
        BigDecimal clinicalGross,
        BigDecimal pediatricSurgeryGross,
        BigDecimal generalGross,
        BigDecimal totalExpenses,
        BigDecimal cardDiscount,
        BigDecimal predatedCheckDiscount,
        BigDecimal cashCheckDiscount,
        BigDecimal voucherDiscount,
        BigDecimal otherDiscount,
        BigDecimal netTotal,
        BigDecimal finalBalance,
        Long patientCount,
        Long moldingCount,
        Long biteCount,
        Long fittingCount,
        Long deliveryCount,
        Long repetitionCount
) {
    public BigDecimal totalDiscounts() {
        return cardDiscount
                .add(predatedCheckDiscount)
                .add(cashCheckDiscount)
                .add(voucherDiscount)
                .add(otherDiscount);
    }
}
