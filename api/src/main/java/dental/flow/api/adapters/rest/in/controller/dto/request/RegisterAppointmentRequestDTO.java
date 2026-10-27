package dental.flow.api.adapters.rest.in.controller.dto.request;

import java.time.LocalDateTime;

public record RegisterAppointmentRequestDTO(
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
