package dental.flow.api.application.out;

import dental.flow.api.domain.user.User;

public interface RegisterUserRepository {
    String register(User user);
    Boolean existsByLogin(User user);
}
