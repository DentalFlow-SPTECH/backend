package dental.flow.api.adapters.rest.in.controller.mapper;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterClinicRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ClinicResponseDTO;
import dental.flow.api.application.command.RegisterClinicCommand;
import dental.flow.api.domain.clinic.Clinic;

public class ClinicWebMapper {
    public static RegisterClinicCommand toCommand(RegisterClinicRequestDTO dto) {
        return new RegisterClinicCommand(dto.name(), dto.cnpj(), dto.address(), dto.phone());
    }

    public static ClinicResponseDTO toResponse(Clinic clinic) {
        return ClinicResponseDTO.from(clinic);
    }
}
