package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.inventory.StockMovement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockMovementResponseDTO(Long id, Long productId, Long userId, String type,
                                       BigDecimal quantity, BigDecimal unitCostAtTime,
                                       String reason, LocalDateTime occurredAt) {
    public static StockMovementResponseDTO from(StockMovement movement) {
        return new StockMovementResponseDTO(movement.id(), movement.productId(), movement.userId(), movement.type(),
                movement.quantity(), movement.unitCostAtTime(), movement.reason(), movement.occurredAt());
    }
}
