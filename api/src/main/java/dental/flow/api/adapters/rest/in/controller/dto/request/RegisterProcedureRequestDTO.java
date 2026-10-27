package dental.flow.api.adapters.rest.in.controller.dto.request;

import java.math.BigDecimal;

public record RegisterProcedureRequestDTO(
        Long clinicId,
        String name,
        BigDecimal price,
        Integer durationMinutes
) {
}
