package dental.flow.api.boot.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class ClinicAccess {
    private ClinicAccess() {
    }

    public static DentalFlowUserPrincipal currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof DentalFlowUserPrincipal principal)) {
            throw new AccessDeniedException("Usuário autenticado não identificado.");
        }
        return principal;
    }

    public static Long requireClinic(Long clinicId) {
        DentalFlowUserPrincipal principal = currentUser();
        if (clinicId != null && !principal.clinicId().equals(clinicId)) {
            throw new AccessDeniedException("Acesso permitido somente aos dados da própria clínica.");
        }
        return principal.clinicId();
    }
}
