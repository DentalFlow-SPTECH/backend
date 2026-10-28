package dental.flow.api.domain.procedure;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

import java.math.BigDecimal;

public class Procedure {
    private Long id;
    private Long clinicId;
    private String name;
    private BigDecimal price;
    private Integer durationMinutes;

    public Procedure(Long clinicId, String name, BigDecimal price, Integer durationMinutes) {
        if (clinicId == null || clinicId <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A clínica é obrigatória.");
        }
        if (name == null || name.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome é obrigatório.");
        }
        if (name.length() > 100) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome deve ter no máximo 100 caracteres.");
        }
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O preço não pode ser negativo.");
        }
        if (durationMinutes != null && durationMinutes <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A duração em minutos deve ser maior que zero.");
        }

        this.clinicId = clinicId;
        this.name = name.trim();
        this.price = price;
        this.durationMinutes = durationMinutes;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
