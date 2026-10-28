package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.RegisterUserResponseDTO;
import dental.flow.api.adapters.rest.out.persistence.mapper.UserPersistenceMapper;
import dental.flow.api.application.command.RegisterUserCommand;
import dental.flow.api.application.in.RegisterUserUseCase;
import dental.flow.api.application.out.RegisterUserRepository;
import dental.flow.api.application.out.RegisterDentistRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final RegisterUserRepository registerUserRepository;
    private final RegisterDentistRepository dentistRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(RegisterUserRepository registerUserRepository,
                               RegisterDentistRepository dentistRepository,
                               PasswordEncoder passwordEncoder) {
        this.registerUserRepository = registerUserRepository;
        this.dentistRepository = dentistRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterUserResponseDTO register(RegisterUserCommand command) {
        User user = UserPersistenceMapper.toDomain(command);
        if (user.getDentistId() != null) {
            var dentist = dentistRepository.findById(user.getDentistId())
                    .orElseThrow(() -> new BusinessException(Failures.BUSINESS_FAILURE, "Dentista vinculado não encontrado."));
            if (!dentist.getClinicId().equals(user.getClinicId())) {
                throw new BusinessException(Failures.BUSINESS_FAILURE, "O dentista e o usuário devem pertencer à mesma clínica.");
            }
        }
        if (registerUserRepository.existsByLogin(user)) {
            throw new BusinessException(Failures.BUSINESS_FAILURE, "Já existe um usuário cadastrado com este login.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return new RegisterUserResponseDTO(registerUserRepository.register(user));
    }
}
