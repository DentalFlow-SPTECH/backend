package dental.flow.api.adapters.rest.in.controller.dto.request;

public record RegisterClinicRequestDTO(
        String name,
        String cnpj,
        String address,
        String phone
) {
}
