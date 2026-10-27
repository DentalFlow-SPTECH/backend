package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.patient.Patient;

import java.time.LocalDate;

public record PatientResponseDTO(
        Long id,
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
    public static PatientResponseDTO from(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getClinicId(),
                patient.getName(),
                patient.getCpf(),
                patient.getAddress(),
                patient.getNeighborhood(),
                patient.getZipCode(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getBirthDate()
        );
    }
}
