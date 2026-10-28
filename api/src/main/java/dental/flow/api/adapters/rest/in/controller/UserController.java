package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterUserRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.RegisterUserResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.UserWebMapper;
import dental.flow.api.application.in.RegisterUserUseCase;
import dental.flow.api.boot.security.ClinicAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuários", description = "Cadastro de dentistas e recepcionistas no sistema")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um usuário")
    public ResponseEntity<ApiResponse<RegisterUserResponseDTO>> register(@RequestBody RegisterUserRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        RegisterUserResponseDTO data = registerUserUseCase.register(UserWebMapper.toCommand(requestDTO));
        ApiResponse<RegisterUserResponseDTO> response = ApiResponse.created("Usuário cadastrado com sucesso.", data);
        return ResponseEntity.status(201).body(response);
    }
}
