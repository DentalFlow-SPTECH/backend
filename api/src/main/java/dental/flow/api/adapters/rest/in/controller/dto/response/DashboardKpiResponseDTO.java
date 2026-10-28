package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.dashboard.AppointmentMetrics;

import java.math.BigDecimal;

public record DashboardKpiResponseDTO(
        Long operatingDays,
        BigDecimal averageTicket,
        BigDecimal averageDailyRevenue,
        BigDecimal expenseRate,
        BigDecimal discountRate,
        BigDecimal outflowRate,
        BigDecimal netMargin,
        BigDecimal repetitionRate,
        BigDecimal completionRate,
        BigDecimal noShowRate
) {
    public static DashboardKpiResponseDTO from(MonthlyDashboardSummaryResponseDTO summary,
                                                long operatingDays,
                                                AppointmentMetrics appointments) {
        return new DashboardKpiResponseDTO(
                operatingDays,
                DashboardMath.divide(summary.generalGross(), summary.patientCount()),
                DashboardMath.divide(summary.generalGross(), operatingDays),
                DashboardMath.percentage(summary.totalExpenses(), summary.generalGross()),
                DashboardMath.percentage(summary.totalDiscounts(), summary.generalGross()),
                DashboardMath.percentage(summary.totalOutflows(), summary.generalGross()),
                DashboardMath.percentage(summary.finalBalance(), summary.generalGross()),
                DashboardMath.percentage(summary.repetitionCount(), summary.patientCount()),
                DashboardMath.percentage(appointments.completed(), appointments.total()),
                DashboardMath.percentage(appointments.noShow(), appointments.total())
        );
    }
}
