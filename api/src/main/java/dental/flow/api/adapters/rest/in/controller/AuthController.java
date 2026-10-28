package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.AuthenticatedUserResponseDTO;
import dental.flow.api.boot.security.DentalFlowUserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthenticatedUserResponseDTO>> me(
            @AuthenticationPrincipal DentalFlowUserPrincipal principal
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Usuário autenticado com sucesso.",
                AuthenticatedUserResponseDTO.from(principal)
        ));
    }
}
