package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.dashboard.DailyDashboardPoint;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DashboardDailyPointResponseDTO(
        LocalDate date,
        BigDecimal generalGross,
        BigDecimal totalExpenses,
        BigDecimal totalDiscounts,
        BigDecimal totalOutflows,
        BigDecimal netTotal,
        BigDecimal finalBalance,
        Long patientCount
) {
    public static DashboardDailyPointResponseDTO from(DailyDashboardPoint point) {
        return new DashboardDailyPointResponseDTO(
                point.date(), point.generalGross(), point.totalExpenses(), point.totalDiscounts(),
                point.totalExpenses().add(point.totalDiscounts()), point.netTotal(),
                point.finalBalance(), point.patientCount());
    }
}
