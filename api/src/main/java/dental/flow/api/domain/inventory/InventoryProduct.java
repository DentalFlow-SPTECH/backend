package dental.flow.api.domain.inventory;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

import java.math.BigDecimal;

public class InventoryProduct {
    private Long id;
    private final Long clinicId;
    private final String name;
    private final String unit;
    private final BigDecimal unitCost;
    private BigDecimal currentStock;
    private final BigDecimal minimumStock;
    private final Boolean active;

    public InventoryProduct(Long clinicId, String name, String unit, BigDecimal unitCost,
                            BigDecimal currentStock, BigDecimal minimumStock, Boolean active) {
        if (clinicId == null || clinicId <= 0) throw error("A clínica é obrigatória.");
        if (name == null || name.isBlank()) throw error("O nome do produto é obrigatório.");
        if (name.length() > 100) throw error("O nome do produto deve ter no máximo 100 caracteres.");
        if (unit == null || unit.isBlank()) throw error("A unidade do produto é obrigatória.");
        if (unit.length() > 20) throw error("A unidade deve ter no máximo 20 caracteres.");
        if (negative(unitCost) || negative(currentStock) || negative(minimumStock)) {
            throw error("Custos e quantidades de estoque não podem ser negativos.");
        }
        this.clinicId = clinicId;
        this.name = name.trim();
        this.unit = unit.trim().toUpperCase();
        this.unitCost = value(unitCost);
        this.currentStock = value(currentStock);
        this.minimumStock = value(minimumStock);
        this.active = active == null || active;
    }

    public boolean lowStock() { return currentStock.compareTo(minimumStock) <= 0; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClinicId() { return clinicId; }
    public String getName() { return name; }
    public String getUnit() { return unit; }
    public BigDecimal getUnitCost() { return unitCost; }
    public BigDecimal getCurrentStock() { return currentStock; }
    public void setCurrentStock(BigDecimal currentStock) { this.currentStock = currentStock; }
    public BigDecimal getMinimumStock() { return minimumStock; }
    public Boolean getActive() { return active; }

    private static BigDecimal value(BigDecimal value) { return value == null ? BigDecimal.ZERO : value; }
    private static boolean negative(BigDecimal value) { return value != null && value.signum() < 0; }
    private static DomainException error(String message) { return new DomainException(Failures.DOMAIN_ERROR, message); }
}
