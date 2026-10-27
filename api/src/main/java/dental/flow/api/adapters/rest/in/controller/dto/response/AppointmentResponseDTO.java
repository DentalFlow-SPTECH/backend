package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.appointment.Appointment;

import java.time.LocalDateTime;

public record AppointmentResponseDTO(
        Long id,
        Long clinicId,
        Long patientId,
        Long dentistId,
        Long procedureId,
        Long userId,
        LocalDateTime scheduledAt,
        String status,
        String notes
) {
    public static AppointmentResponseDTO from(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getClinicId(),
                appointment.getPatientId(),
                appointment.getDentistId(),
                appointment.getProcedureId(),
                appointment.getUserId(),
                appointment.getScheduledAt(),
                appointment.getStatus(),
                appointment.getNotes()
        );
    }
}
