package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "equipamento")
public class EquipmentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_equipamento") private Long id;
    @Column(name = "id_clinica", nullable = false) private Long clinicId;
    @Column(name = "nome", nullable = false, length = 100) private String name;
    @Column(name = "descricao", length = 255) private String description;
    @Column(name = "valor_aquisicao", nullable = false, precision = 12, scale = 2) private BigDecimal acquisitionValue;
    @Column(name = "vida_util_usos", nullable = false) private Integer usefulLifeUses;
    @Column(name = "usos_realizados", nullable = false) private Integer completedUses;
    @Column(name = "custo_por_uso", nullable = false, precision = 12, scale = 2) private BigDecimal costPerUse;
    @Column(name = "ativo", nullable = false) private Boolean active;
    public EquipmentEntity() {}
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getClinicId() { return clinicId; } public void setClinicId(Long clinicId) { this.clinicId = clinicId; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public BigDecimal getAcquisitionValue() { return acquisitionValue; } public void setAcquisitionValue(BigDecimal acquisitionValue) { this.acquisitionValue = acquisitionValue; }
    public Integer getUsefulLifeUses() { return usefulLifeUses; } public void setUsefulLifeUses(Integer usefulLifeUses) { this.usefulLifeUses = usefulLifeUses; }
    public Integer getCompletedUses() { return completedUses; } public void setCompletedUses(Integer completedUses) { this.completedUses = completedUses; }
    public BigDecimal getCostPerUse() { return costPerUse; } public void setCostPerUse(BigDecimal costPerUse) { this.costPerUse = costPerUse; }
    public Boolean getActive() { return active; } public void setActive(Boolean active) { this.active = active; }
}
