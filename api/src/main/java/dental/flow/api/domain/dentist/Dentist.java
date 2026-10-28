package dental.flow.api.domain.dentist;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

public class Dentist {
    private Long id;
    private Long clinicId;
    private String name;
    private String cro;
    private String specialty;
    private String phone;

    public Dentist(Long clinicId, String name, String cro, String specialty, String phone) {
        if (clinicId == null || clinicId <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A clínica é obrigatória.");
        }
        if (name == null || name.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome é obrigatório.");
        }
        if (name.length() > 100) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome deve ter no máximo 100 caracteres.");
        }
        if (cro == null || cro.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O CRO é obrigatório.");
        }
        if (cro.length() > 20) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O CRO deve ter no máximo 20 caracteres.");
        }
        if (specialty != null && specialty.length() > 50) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A especialidade deve ter no máximo 50 caracteres.");
        }
        if (phone != null && phone.length() > 20) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O telefone deve ter no máximo 20 caracteres.");
        }

        this.clinicId = clinicId;
        this.name = name.trim();
        this.cro = cro.trim();
        this.specialty = specialty == null || specialty.isBlank() ? null : specialty.trim();
        this.phone = phone == null || phone.isBlank() ? null : phone.trim();
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

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
