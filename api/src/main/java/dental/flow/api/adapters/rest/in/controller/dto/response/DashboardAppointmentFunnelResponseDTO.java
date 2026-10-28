package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.dashboard.AppointmentMetrics;

import java.math.BigDecimal;

public record DashboardAppointmentFunnelResponseDTO(
        Long total,
        Long scheduled,
        Long confirmed,
        Long inProgress,
        Long completed,
        Long cancelled,
        Long noShow,
        BigDecimal completionRate,
        BigDecimal cancellationRate,
        BigDecimal noShowRate
) {
    public static DashboardAppointmentFunnelResponseDTO from(AppointmentMetrics metrics) {
        return new DashboardAppointmentFunnelResponseDTO(
                metrics.total(), metrics.scheduled(), metrics.confirmed(), metrics.inProgress(),
                metrics.completed(), metrics.cancelled(), metrics.noShow(),
                DashboardMath.percentage(metrics.completed(), metrics.total()),
                DashboardMath.percentage(metrics.cancelled(), metrics.total()),
                DashboardMath.percentage(metrics.noShow(), metrics.total()));
    }
}
