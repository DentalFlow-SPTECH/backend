package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.clinic.Clinic;

public record ClinicResponseDTO(
        Long id,
        String name,
        String cnpj,
        String address,
        String phone
) {
    public static ClinicResponseDTO from(Clinic clinic) {
        return new ClinicResponseDTO(clinic.getId(), clinic.getName(), clinic.getCnpj(), clinic.getAddress(), clinic.getPhone());
    }
}
