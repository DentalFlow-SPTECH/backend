package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.procedure.Procedure;

import java.math.BigDecimal;

public record ProcedureResponseDTO(
        Long id,
        Long clinicId,
        String name,
        BigDecimal price,
        Integer durationMinutes
) {
    public static ProcedureResponseDTO from(Procedure procedure) {
        return new ProcedureResponseDTO(procedure.getId(), procedure.getClinicId(), procedure.getName(), procedure.getPrice(), procedure.getDurationMinutes());
    }
}
