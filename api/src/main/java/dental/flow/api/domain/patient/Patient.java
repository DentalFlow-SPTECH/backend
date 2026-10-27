package dental.flow.api.domain.patient;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

import java.time.LocalDate;
import java.util.Locale;

public class Patient {
    private Long id;
    private Long clinicId;
    private String name;
    private String cpf;
    private String address;
    private String neighborhood;
    private String zipCode;
    private String phone;
    private String email;
    private LocalDate birthDate;

    public Patient(Long clinicId, String name, String cpf, String address, String neighborhood, String zipCode,
                   String phone, String email, LocalDate birthDate) {
        if (clinicId == null || clinicId <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A clínica é obrigatória.");
        }
        if (name == null || name.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome é obrigatório.");
        }
        if (name.length() > 100) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome deve ter no máximo 100 caracteres.");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O CPF é obrigatório.");
        }
        if (cpf.length() > 14) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O CPF deve ter no máximo 14 caracteres.");
        }
        if (address == null || address.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O endereço é obrigatório.");
        }
        if (address.length() > 150) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O endereço deve ter no máximo 150 caracteres.");
        }
        if (neighborhood == null || neighborhood.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O bairro é obrigatório.");
        }
        if (neighborhood.length() > 80) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O bairro deve ter no máximo 80 caracteres.");
        }
        if (zipCode == null || zipCode.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O CEP é obrigatório.");
        }
        if (zipCode.length() > 9) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O CEP deve ter no máximo 9 caracteres.");
        }
        if (phone != null && phone.length() > 20) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O telefone deve ter no máximo 20 caracteres.");
        }
        if (email != null && email.length() > 100) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O e-mail deve ter no máximo 100 caracteres.");
        }
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A data de nascimento não pode ser futura.");
        }

        this.clinicId = clinicId;
        this.name = name.trim();
        this.cpf = cpf.trim();
        this.address = address.trim();
        this.neighborhood = neighborhood.trim();
        this.zipCode = zipCode.trim();
        this.phone = phone == null ? null : phone.trim();
        this.email = email == null ? null : email.trim().toLowerCase(Locale.ROOT);
        this.birthDate = birthDate;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
