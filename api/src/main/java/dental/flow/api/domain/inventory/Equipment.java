package dental.flow.api.domain.inventory;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Equipment {
    private Long id;
    private final Long clinicId;
    private final String name;
    private final String description;
    private final BigDecimal acquisitionValue;
    private final Integer usefulLifeUses;
    private final Integer completedUses;
    private final BigDecimal costPerUse;
    private final Boolean active;

    public Equipment(Long clinicId, String name, String description, BigDecimal acquisitionValue,
                     Integer usefulLifeUses, Integer completedUses, Boolean active) {
        if (clinicId == null || clinicId <= 0) {
            throw error("A clínica é obrigatória.");
        }
        if (name == null || name.isBlank()) {
            throw error("O nome do equipamento é obrigatório.");
        }
        if (name.length() > 100) {
            throw error("O nome do equipamento deve ter no máximo 100 caracteres.");
        }
        if (description != null && description.length() > 255) {
            throw error("A descrição deve ter no máximo 255 caracteres.");
        }
        if (acquisitionValue == null || acquisitionValue.signum() < 0) {
            throw error("O valor de aquisição não pode ser negativo.");
        }
        if (usefulLifeUses == null || usefulLifeUses <= 0) {
            throw error("A vida útil em usos deve ser maior que zero.");
        }
        if (completedUses != null && completedUses < 0) {
            throw error("A quantidade de usos não pode ser negativa.");
        }
        this.clinicId = clinicId;
        this.name = name.trim();
        this.description = description == null || description.isBlank() ? null : description.trim();
        this.acquisitionValue = acquisitionValue;
        this.usefulLifeUses = usefulLifeUses;
        this.completedUses = completedUses == null ? 0 : completedUses;
        this.costPerUse = acquisitionValue.divide(BigDecimal.valueOf(usefulLifeUses), 2, RoundingMode.HALF_UP);
        this.active = active == null || active;
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAcquisitionValue() {
        return acquisitionValue;
    }

    public Integer getUsefulLifeUses() {
        return usefulLifeUses;
    }

    public Integer getCompletedUses() {
        return completedUses;
    }

    public BigDecimal getCostPerUse() {
        return costPerUse;
    }

    public Boolean getActive() {
        return active;
    }

    private static DomainException error(String message) {
        return new DomainException(Failures.DOMAIN_ERROR, message);
    }
}
