package dental.flow.api.application.out;

import dental.flow.api.adapters.rest.in.controller.dto.response.RegisterUserResponseDTO;
import dental.flow.api.application.command.RegisterUserCommand;
import dental.flow.api.domain.user.User;

public interface RegisterUserRepository {
    String register(User user);
    Boolean existsByEmail(User user);
}
