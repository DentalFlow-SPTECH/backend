package dental.flow.api.application.command;

import java.time.LocalDate;

public record RegisterPatientCommand(
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
