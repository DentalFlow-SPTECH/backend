package dental.flow.api.application.in;

import dental.flow.api.adapters.rest.in.controller.dto.response.RegisterUserResponseDTO;
import dental.flow.api.application.command.RegisterUserCommand;

public interface RegisterUserUseCase {
    RegisterUserResponseDTO register(RegisterUserCommand command);
}
