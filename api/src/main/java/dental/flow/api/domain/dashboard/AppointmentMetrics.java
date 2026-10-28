package dental.flow.api.domain.dashboard;

public record AppointmentMetrics(
        Long total,
        Long scheduled,
        Long confirmed,
        Long inProgress,
        Long completed,
        Long cancelled,
        Long noShow
) {
    public static AppointmentMetrics empty() {
        return new AppointmentMetrics(0L, 0L, 0L, 0L, 0L, 0L, 0L);
    }
}
