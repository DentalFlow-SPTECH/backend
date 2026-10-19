package dental.flow.api.adapters.rest.in.controller.dto.request;

public record RegisterUserRequestDTO(
        String name,
        String email,
        String password,
        Long clinicId
){
}
