package dental.flow.api.boot.security;

import dental.flow.api.adapters.rest.out.persistence.entity.UserEntity;
import dental.flow.api.adapters.rest.out.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsServiceTest {
    @Mock
    private UserJpaRepository repository;
    @InjectMocks
    private DatabaseUserDetailsService service;

    @Test
    void shouldLoadDentistWithClinicContext() {
        UserEntity user = new UserEntity("Dra. Ana", "dentista", "hash", 1L, 2L, "DENTISTA", true);
        user.setId(3L);
        when(repository.findByLogin("dentista")).thenReturn(Optional.of(user));

        DentalFlowUserPrincipal principal = (DentalFlowUserPrincipal) service.loadUserByUsername(" DENTISTA ");

        assertEquals(3L, principal.userId());
        assertEquals(1L, principal.clinicId());
        assertEquals(2L, principal.dentistId());
        assertEquals("ROLE_DENTISTA", principal.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void shouldDisableInactiveUserAndRejectUnknownLogin() {
        UserEntity user = new UserEntity("Recepção", "recepcao", "hash", 1L, null, "RECEPCIONISTA", false);
        when(repository.findByLogin("recepcao")).thenReturn(Optional.of(user));
        assertFalse(service.loadUserByUsername("recepcao").isEnabled());

        when(repository.findByLogin("missing")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("missing"));
    }
}
