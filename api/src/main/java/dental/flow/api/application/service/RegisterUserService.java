package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.RegisterUserResponseDTO;
import dental.flow.api.adapters.rest.out.persistence.mapper.UserPersistenceMapper;
import dental.flow.api.application.command.RegisterUserCommand;
import dental.flow.api.application.in.RegisterUserUseCase;
import dental.flow.api.application.out.RegisterUserRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.user.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final RegisterUserRepository registerUserRepository;

    public RegisterUserService(RegisterUserRepository registerUserRepository) {
        this.registerUserRepository = registerUserRepository;
    }

    @Override
    public RegisterUserResponseDTO register(RegisterUserCommand command) {
        User user = UserPersistenceMapper.toDomain(command);
        if (registerUserRepository.existsByLogin(user)) {
            throw new BusinessException(Failures.BUSINESS_FAILURE, "Já existe um usuário cadastrado com este login.");
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);

        return new RegisterUserResponseDTO(registerUserRepository.register(user));
    }
}
