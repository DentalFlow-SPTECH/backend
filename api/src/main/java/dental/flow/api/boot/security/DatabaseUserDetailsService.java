package dental.flow.api.boot.security;

import dental.flow.api.adapters.rest.out.persistence.entity.UserEntity;
import dental.flow.api.adapters.rest.out.persistence.repository.UserJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserJpaRepository repository;

    public DatabaseUserDetailsService(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repository.findByLogin(username.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha inválidos."));
        return new DentalFlowUserPrincipal(
                user.getId(),
                user.getClinicId(),
                user.getDentistId(),
                user.getName(),
                user.getLogin(),
                user.getPassword(),
                user.getAccessLevel(),
                Boolean.TRUE.equals(user.getActive())
        );
    }
}
