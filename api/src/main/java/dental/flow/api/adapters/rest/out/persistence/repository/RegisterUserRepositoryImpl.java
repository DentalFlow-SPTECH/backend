package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.UserEntity;
import dental.flow.api.adapters.rest.out.persistence.mapper.UserPersistenceMapper;
import dental.flow.api.application.out.RegisterUserRepository;
import dental.flow.api.boot.exception.DatabaseException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class RegisterUserRepositoryImpl implements RegisterUserRepository {

    private final UserJpaRepository userJpaRepository;

    public RegisterUserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public String register(User user) {
        try {
            UserEntity entity = UserPersistenceMapper.toEntity(user);

            UserEntity saved = userJpaRepository.saveAndFlush(entity);

            System.out.println("ID gerado: " + saved.getId());

            return "User id:" + saved.getId() + " registered successfully";
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error registering user: " + e.getMessage());
        }
    }

    public Boolean existsByEmail(User user) {
        return userJpaRepository.existsByEmail(user.getEmail());
    }
}
