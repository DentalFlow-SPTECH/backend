package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_insumo")
public class StockMovementEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_movimentacao") private Long id;
    @Column(name = "id_insumo", nullable = false) private Long productId;
    @Column(name = "id_usuario", nullable = false) private Long userId;
    @Column(name = "tipo", nullable = false, length = 10) private String type;
    @Column(name = "quantidade", nullable = false, precision = 12, scale = 3) private BigDecimal quantity;
    @Column(name = "custo_unitario_na_data", nullable = false, precision = 12, scale = 2) private BigDecimal unitCostAtTime;
    @Column(name = "motivo", length = 150) private String reason;
    @Column(name = "data_movimentacao", nullable = false) private LocalDateTime occurredAt;
    public StockMovementEntity() {}
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; } public void setProductId(Long productId) { this.productId = productId; }
    public Long getUserId() { return userId; } public void setUserId(Long userId) { this.userId = userId; }
    public String getType() { return type; } public void setType(String type) { this.type = type; }
    public BigDecimal getQuantity() { return quantity; } public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getUnitCostAtTime() { return unitCostAtTime; } public void setUnitCostAtTime(BigDecimal value) { this.unitCostAtTime = value; }
    public String getReason() { return reason; } public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getOccurredAt() { return occurredAt; } public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
