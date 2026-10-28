package dental.flow.api.adapters.rest.out.persistence.repository;

public interface AppointmentMetricsProjection {
    Long getTotalAppointments();

    Long getScheduledAppointments();

    Long getConfirmedAppointments();

    Long getInProgressAppointments();

    Long getCompletedAppointments();

    Long getCancelledAppointments();

    Long getNoShowAppointments();
}
