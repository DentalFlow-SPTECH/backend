package dental.flow.api.application.command;

import java.time.LocalDateTime;

public record RegisterAppointmentCommand(
        Long clinicId,
        Long patientId,
        Long dentistId,
        Long procedureId,
        Long userId,
        LocalDateTime scheduledAt,
        String status,
        String notes
) {
}
