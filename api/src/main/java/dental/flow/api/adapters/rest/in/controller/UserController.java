package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterUserRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.RegisterUserResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.UserWebMapper;
import dental.flow.api.application.in.RegisterUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RegisterUserResponseDTO>> register(@RequestBody RegisterUserRequestDTO requestDTO){
        RegisterUserResponseDTO data = registerUserUseCase.register(UserWebMapper.toCommand(requestDTO));
        ApiResponse<RegisterUserResponseDTO> response = ApiResponse.created("Usuário cadastrado com sucesso.", data);
        return ResponseEntity.status(201).body(response);
    }
}
