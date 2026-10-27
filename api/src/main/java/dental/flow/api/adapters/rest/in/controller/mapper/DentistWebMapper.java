package dental.flow.api.adapters.rest.in.controller.mapper;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterDentistRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.DentistResponseDTO;
import dental.flow.api.application.command.RegisterDentistCommand;
import dental.flow.api.domain.dentist.Dentist;

public class DentistWebMapper {
    public static RegisterDentistCommand toCommand(RegisterDentistRequestDTO dto) {
        return new RegisterDentistCommand(dto.clinicId(), dto.name(), dto.cro(), dto.specialty(), dto.phone());
    }

    public static DentistResponseDTO toResponse(Dentist dentist) {
        return DentistResponseDTO.from(dentist);
    }
}
