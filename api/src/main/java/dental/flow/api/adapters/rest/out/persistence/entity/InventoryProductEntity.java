package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "insumo")
public class InventoryProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insumo")
    private Long id;
    @Column(name = "id_clinica", nullable = false)
    private Long clinicId;
    @Column(name = "nome", nullable = false, length = 100)
    private String name;
    @Column(name = "unidade", nullable = false, length = 20)
    private String unit;
    @Column(name = "custo_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitCost;
    @Column(name = "estoque_atual", nullable = false, precision = 12, scale = 3)
    private BigDecimal currentStock;
    @Column(name = "estoque_minimo", nullable = false, precision = 12, scale = 3)
    private BigDecimal minimumStock;
    @Column(name = "ativo", nullable = false)
    private Boolean active;

    public InventoryProductEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
