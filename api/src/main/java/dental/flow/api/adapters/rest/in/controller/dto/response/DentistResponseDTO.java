package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.dentist.Dentist;

public record DentistResponseDTO(
        Long id,
        Long clinicId,
        String name,
        String cro,
        String specialty,
        String phone
) {
    public static DentistResponseDTO from(Dentist dentist) {
        return new DentistResponseDTO(dentist.getId(), dentist.getClinicId(), dentist.getName(), dentist.getCro(), dentist.getSpecialty(), dentist.getPhone());
    }
}
