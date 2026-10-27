package dental.flow.api.adapters.rest.in.controller.mapper;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterAppointmentRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.AppointmentResponseDTO;
import dental.flow.api.application.command.RegisterAppointmentCommand;
import dental.flow.api.domain.appointment.Appointment;

public class AppointmentWebMapper {
    public static RegisterAppointmentCommand toCommand(RegisterAppointmentRequestDTO dto) {
        return new RegisterAppointmentCommand(
                dto.clinicId(),
                dto.patientId(),
                dto.dentistId(),
                dto.procedureId(),
                dto.userId(),
                dto.scheduledAt(),
                dto.status(),
                dto.notes()
        );
    }

    public static AppointmentResponseDTO toResponse(Appointment appointment) {
        return AppointmentResponseDTO.from(appointment);
    }
}
