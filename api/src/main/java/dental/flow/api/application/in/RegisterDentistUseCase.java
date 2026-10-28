package dental.flow.api.application.in;

import dental.flow.api.adapters.rest.in.controller.dto.response.DentistResponseDTO;
import dental.flow.api.application.command.RegisterDentistCommand;

public interface RegisterDentistUseCase {
    DentistResponseDTO register(RegisterDentistCommand command);

    DentistResponseDTO getById(Long id);

    DentistResponseDTO update(Long id, RegisterDentistCommand command);
}
