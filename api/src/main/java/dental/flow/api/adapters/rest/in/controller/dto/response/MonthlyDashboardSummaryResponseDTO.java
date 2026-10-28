package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.dashboard.MonthlyDashboard;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public record MonthlyDashboardSummaryResponseDTO(
        Long registeredMapDays,
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
        Long patientCount,
        Long moldingCount,
        Long biteCount,
        Long fittingCount,
        Long deliveryCount,
        Long repetitionCount
) {
    public static MonthlyDashboardSummaryResponseDTO from(List<MonthlyDashboard> dashboards) {
        BigDecimal expenses = sum(dashboards, MonthlyDashboard::totalExpenses);
        BigDecimal discounts = sum(dashboards, MonthlyDashboard::totalDiscounts);

        return new MonthlyDashboardSummaryResponseDTO(
                sumLong(dashboards, MonthlyDashboard::registeredDays),
                sum(dashboards, MonthlyDashboard::clinicalTotal),
                sum(dashboards, MonthlyDashboard::prosthesisTotal),
                sum(dashboards, MonthlyDashboard::clinicalGross),
                sum(dashboards, MonthlyDashboard::pediatricSurgeryGross),
                sum(dashboards, MonthlyDashboard::generalGross),
                expenses,
                sum(dashboards, MonthlyDashboard::cardDiscount),
                sum(dashboards, MonthlyDashboard::predatedCheckDiscount),
                sum(dashboards, MonthlyDashboard::cashCheckDiscount),
                sum(dashboards, MonthlyDashboard::voucherDiscount),
                sum(dashboards, MonthlyDashboard::otherDiscount),
                discounts,
                expenses.add(discounts),
                sum(dashboards, MonthlyDashboard::netTotal),
                sum(dashboards, MonthlyDashboard::finalBalance),
                sumLong(dashboards, MonthlyDashboard::patientCount),
                sumLong(dashboards, MonthlyDashboard::moldingCount),
                sumLong(dashboards, MonthlyDashboard::biteCount),
                sumLong(dashboards, MonthlyDashboard::fittingCount),
                sumLong(dashboards, MonthlyDashboard::deliveryCount),
                sumLong(dashboards, MonthlyDashboard::repetitionCount)
        );
    }

    private static BigDecimal sum(List<MonthlyDashboard> dashboards,
                                  Function<MonthlyDashboard, BigDecimal> value) {
        return dashboards.stream()
                .map(value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static Long sumLong(List<MonthlyDashboard> dashboards,
                                Function<MonthlyDashboard, Long> value) {
        return dashboards.stream()
                .map(value)
                .reduce(0L, Long::sum);
    }
}
