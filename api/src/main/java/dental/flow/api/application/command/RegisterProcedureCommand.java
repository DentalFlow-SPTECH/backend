package dental.flow.api.application.command;

import java.math.BigDecimal;

public record RegisterProcedureCommand(
        Long clinicId,
        String name,
        BigDecimal price,
        Integer durationMinutes
) {
}
