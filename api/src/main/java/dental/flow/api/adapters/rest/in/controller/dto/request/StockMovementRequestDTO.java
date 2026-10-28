package dental.flow.api.adapters.rest.in.controller.dto.request;

import java.math.BigDecimal;

public record StockMovementRequestDTO(String type, BigDecimal quantity, String reason) {
}
