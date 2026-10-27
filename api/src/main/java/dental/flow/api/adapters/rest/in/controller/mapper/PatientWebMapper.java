package dental.flow.api.adapters.rest.in.controller.mapper;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterPatientRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.PatientResponseDTO;
import dental.flow.api.application.command.RegisterPatientCommand;
import dental.flow.api.domain.patient.Patient;

public class PatientWebMapper {
    public static RegisterPatientCommand toCommand(RegisterPatientRequestDTO dto) {
        return new RegisterPatientCommand(
                dto.clinicId(),
                dto.name(),
                dto.cpf(),
                dto.address(),
                dto.neighborhood(),
                dto.zipCode(),
                dto.phone(),
                dto.email(),
                dto.birthDate()
        );
    }

    public static PatientResponseDTO toResponse(Patient patient) {
        return PatientResponseDTO.from(patient);
    }
}
