package dental.flow.api.adapters.rest.in.controller.dto.request;

import java.time.LocalDate;

public record RegisterPatientRequestDTO(
        Long clinicId,
        String name,
        String cpf,
        String address,
        String neighborhood,
        String zipCode,
        String phone,
        String email,
        LocalDate birthDate
) {
}
