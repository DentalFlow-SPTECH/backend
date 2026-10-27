package dental.flow.api.application.in;

import dental.flow.api.adapters.rest.in.controller.dto.response.ClinicResponseDTO;
import dental.flow.api.application.command.RegisterClinicCommand;

public interface RegisterClinicUseCase {
    ClinicResponseDTO register(RegisterClinicCommand command);
    ClinicResponseDTO getById(Long id);
    ClinicResponseDTO update(Long id, RegisterClinicCommand command);
}
