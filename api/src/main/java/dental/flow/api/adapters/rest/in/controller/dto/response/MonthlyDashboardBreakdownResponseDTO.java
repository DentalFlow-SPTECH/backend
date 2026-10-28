package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.dashboard.MonthlyDashboard;

import java.math.BigDecimal;

public record MonthlyDashboardBreakdownResponseDTO(
        Long clinicId,
        String clinicName,
        Long dentistId,
        String dentistName,
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
        BigDecimal totalDiscounts,
        BigDecimal totalOutflows,
        BigDecimal netTotal,
        BigDecimal finalBalance,
        BigDecimal averageTicket,
        BigDecimal expenseRate,
        BigDecimal discountRate,
        BigDecimal netMargin,
        BigDecimal repetitionRate,
        Long patientCount,
        Long moldingCount,
        Long biteCount,
        Long fittingCount,
        Long deliveryCount,
        Long repetitionCount
) {
    public static MonthlyDashboardBreakdownResponseDTO from(MonthlyDashboard dashboard) {
        BigDecimal discounts = dashboard.totalDiscounts();
        return new MonthlyDashboardBreakdownResponseDTO(
                dashboard.clinicId(),
                dashboard.clinicName(),
                dashboard.dentistId(),
                dashboard.dentistName(),
                dashboard.registeredDays(),
                dashboard.clinicalTotal(),
                dashboard.prosthesisTotal(),
                dashboard.clinicalGross(),
                dashboard.pediatricSurgeryGross(),
                dashboard.generalGross(),
                dashboard.totalExpenses(),
                dashboard.cardDiscount(),
                dashboard.predatedCheckDiscount(),
                dashboard.cashCheckDiscount(),
                dashboard.voucherDiscount(),
                dashboard.otherDiscount(),
                discounts,
                dashboard.totalExpenses().add(discounts),
                dashboard.netTotal(),
                dashboard.finalBalance(),
                DashboardMath.divide(dashboard.generalGross(), dashboard.patientCount()),
                DashboardMath.percentage(dashboard.totalExpenses(), dashboard.generalGross()),
                DashboardMath.percentage(discounts, dashboard.generalGross()),
                DashboardMath.percentage(dashboard.finalBalance(), dashboard.generalGross()),
                DashboardMath.percentage(dashboard.repetitionCount(), dashboard.patientCount()),
                dashboard.patientCount(),
                dashboard.moldingCount(),
                dashboard.biteCount(),
                dashboard.fittingCount(),
                dashboard.deliveryCount(),
                dashboard.repetitionCount()
        );
    }
}
