package dental.flow.api.domain.inventory;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

public record StockMovement(Long id, Long productId, Long userId, String type, BigDecimal quantity,
                            BigDecimal unitCostAtTime, String reason, LocalDateTime occurredAt) {
    private static final Set<String> TYPES = Set.of("ENTRADA", "SAIDA", "AJUSTE");

    public StockMovement {
        String normalized = type == null ? "" : type.trim().toUpperCase(Locale.ROOT);
        if (!TYPES.contains(normalized))
            throw new DomainException(Failures.DOMAIN_ERROR, "Tipo de movimentação inválido.");
        if (quantity == null || quantity.signum() < 0 || (!"AJUSTE".equals(normalized) && quantity.signum() == 0)) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A quantidade da movimentação é inválida.");
        }
        if (reason != null && reason.length() > 150)
            throw new DomainException(Failures.DOMAIN_ERROR, "O motivo deve ter no máximo 150 caracteres.");
        type = normalized;
        reason = reason == null || reason.isBlank() ? null : reason.trim();
    }
}
