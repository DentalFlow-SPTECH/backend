package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.dashboard.MonthlyDashboard;
import dental.flow.api.domain.dashboard.DailyDashboardPoint;
import dental.flow.api.domain.dashboard.AppointmentMetrics;

import java.util.List;

public record MonthlyDashboardResponseDTO(
        Integer year,
        Integer month,
        Long clinicId,
        Long dentistId,
        MonthlyDashboardSummaryResponseDTO summary,
        DashboardKpiResponseDTO kpis,
        DashboardAppointmentFunnelResponseDTO appointmentFunnel,
        List<DashboardDailyPointResponseDTO> dailyEvolution,
        List<MonthlyDashboardBreakdownResponseDTO> breakdown,
        List<DashboardAlertResponseDTO> alerts
) {
    public static MonthlyDashboardResponseDTO from(
            Integer year,
            Integer month,
            Long clinicId,
            Long dentistId,
            List<MonthlyDashboard> dashboards
    ) {
        return from(year, month, clinicId, dentistId, dashboards, List.of(), AppointmentMetrics.empty());
    }

    public static MonthlyDashboardResponseDTO from(
            Integer year,
            Integer month,
            Long clinicId,
            Long dentistId,
            List<MonthlyDashboard> dashboards,
            List<DailyDashboardPoint> dailyEvolution,
            AppointmentMetrics appointmentMetrics
    ) {
        MonthlyDashboardSummaryResponseDTO summary = MonthlyDashboardSummaryResponseDTO.from(dashboards);
        DashboardKpiResponseDTO kpis = DashboardKpiResponseDTO.from(summary, dailyEvolution.size(), appointmentMetrics);
        return new MonthlyDashboardResponseDTO(
                year,
                month,
                clinicId,
                dentistId,
                summary,
                kpis,
                DashboardAppointmentFunnelResponseDTO.from(appointmentMetrics),
                dailyEvolution.stream().map(DashboardDailyPointResponseDTO::from).toList(),
                dashboards.stream()
                        .map(MonthlyDashboardBreakdownResponseDTO::from)
                        .toList(),
                DashboardAlertResponseDTO.evaluate(summary, kpis)
        );
    }
}
