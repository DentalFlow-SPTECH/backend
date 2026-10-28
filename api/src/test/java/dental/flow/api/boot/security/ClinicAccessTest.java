package dental.flow.api.boot.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClinicAccessTest {
    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldRestrictEveryUserToOwnClinic() {
        DentalFlowUserPrincipal principal = new DentalFlowUserPrincipal(
                1L, 10L, null, "Recepção", "recepcao", "hash", "RECEPCIONISTA", true
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, "hash", principal.getAuthorities())
        );

        assertEquals(10L, ClinicAccess.requireClinic(null));
        assertEquals(10L, ClinicAccess.requireClinic(10L));
        assertThrows(AccessDeniedException.class, () -> ClinicAccess.requireClinic(99L));
    }

    @Test
    void shouldRejectMissingPrincipal() {
        assertThrows(AccessDeniedException.class, ClinicAccess::currentUser);
    }
}
