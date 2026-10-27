package dental.flow.api.adapters.rest.in.controller.mapper;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterUserRequestDTO;
import dental.flow.api.application.command.RegisterUserCommand;

public class UserWebMapper {
    public static RegisterUserCommand toCommand(RegisterUserRequestDTO dto) {
        return new RegisterUserCommand(
                dto.name(),
                dto.login(),
                dto.password(),
                dto.clinicId(),
                dto.accessLevel(),
                dto.active()
        );
    }
}
