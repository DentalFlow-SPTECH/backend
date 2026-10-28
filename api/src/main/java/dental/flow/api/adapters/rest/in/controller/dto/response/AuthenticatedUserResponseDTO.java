package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.boot.security.DentalFlowUserPrincipal;

public record AuthenticatedUserResponseDTO(
        Long id,
        Long clinicId,
        Long dentistId,
        String name,
        String login,
        String accessLevel
) {
    public static AuthenticatedUserResponseDTO from(DentalFlowUserPrincipal principal) {
        return new AuthenticatedUserResponseDTO(
                principal.userId(),
                principal.clinicId(),
                principal.dentistId(),
                principal.name(),
                principal.username(),
                principal.accessLevel()
        );
    }
}
