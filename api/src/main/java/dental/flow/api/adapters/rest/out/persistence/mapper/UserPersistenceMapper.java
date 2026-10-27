package dental.flow.api.adapters.rest.out.persistence.mapper;

import dental.flow.api.adapters.rest.out.persistence.entity.UserEntity;
import dental.flow.api.application.command.RegisterUserCommand;
import dental.flow.api.domain.user.User;

public class UserPersistenceMapper {
    public static User toDomain(RegisterUserCommand dto) {
        return new User(
                dto.name(),
                dto.login(),
                dto.password(),
                dto.clinicId(),
                dto.accessLevel(),
                dto.active()
        );
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(
                user.getName(),
                user.getLogin(),
                user.getPassword(),
                user.getClinicId(),
                user.getAccessLevel(),
                user.getActive()
        );
    }
}
