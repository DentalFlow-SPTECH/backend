package dental.flow.api.adapters.rest.in.controller.mapper;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterProcedureRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ProcedureResponseDTO;
import dental.flow.api.application.command.RegisterProcedureCommand;
import dental.flow.api.domain.procedure.Procedure;

public class ProcedureWebMapper {
    public static RegisterProcedureCommand toCommand(RegisterProcedureRequestDTO dto) {
        return new RegisterProcedureCommand(dto.clinicId(), dto.name(), dto.price(), dto.durationMinutes());
    }

    public static ProcedureResponseDTO toResponse(Procedure procedure) {
        return ProcedureResponseDTO.from(procedure);
    }
}
