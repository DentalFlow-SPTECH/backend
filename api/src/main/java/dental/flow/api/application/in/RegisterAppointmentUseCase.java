package dental.flow.api.application.in;

import dental.flow.api.adapters.rest.in.controller.dto.response.AppointmentResponseDTO;
import dental.flow.api.application.command.RegisterAppointmentCommand;

import java.time.LocalDate;
import java.util.List;

public interface RegisterAppointmentUseCase {
    AppointmentResponseDTO register(RegisterAppointmentCommand command);
    AppointmentResponseDTO getById(Long id);
    List<AppointmentResponseDTO> listByDateAndDentist(LocalDate date, Long clinicId, Long dentistId);
    AppointmentResponseDTO update(Long id, RegisterAppointmentCommand command);
    AppointmentResponseDTO updateStatus(Long id, String status);
}
