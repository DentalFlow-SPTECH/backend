package dental.flow.api.adapters.rest.out.persistence.mapper;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterUserRequestDTO;
import dental.flow.api.adapters.rest.out.persistence.entity.UserEntity;
import dental.flow.api.application.command.RegisterUserCommand;
import dental.flow.api.domain.user.User;

public class UserPersistenceMapper {
    public static User toDomain(RegisterUserCommand dto){
        return new User(
                dto.name(),
                dto.email(),
                dto.password(),
                dto.clinicId()
        );
    }
    public static UserEntity toEntity(User user){
        return new UserEntity(
                user.getNome(),
                user.getEmail(),
                user.getPassword(),
                user.getClinicId()
        );
    }
}
