package dental.flow.api.adapters.rest.in.controller.dto.request;

public record RegisterDentistRequestDTO(
        Long clinicId,
        String name,
        String cro,
        String specialty,
        String phone
) {
}
