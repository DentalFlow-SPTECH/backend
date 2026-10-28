package dental.flow.api.application.in;

import dental.flow.api.adapters.rest.in.controller.dto.response.ProcedureResponseDTO;
import dental.flow.api.application.command.RegisterProcedureCommand;

public interface RegisterProcedureUseCase {
    ProcedureResponseDTO register(RegisterProcedureCommand command);

    ProcedureResponseDTO getById(Long id);

    ProcedureResponseDTO update(Long id, RegisterProcedureCommand command);
}
