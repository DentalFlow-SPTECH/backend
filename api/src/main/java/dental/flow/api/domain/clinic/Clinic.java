package dental.flow.api.domain.clinic;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

public class Clinic {
    private Long id;
    private String name;
    private String cnpj;
    private String address;
    private String phone;

    public Clinic(String name, String cnpj, String address, String phone) {
        if (name == null || name.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome é obrigatório.");
        }
        if (name.length() > 100) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome deve ter no máximo 100 caracteres.");
        }
        if (cnpj != null && !cnpj.isBlank() && cnpj.length() > 18) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O CNPJ deve ter no máximo 18 caracteres.");
        }
        if (address != null && address.length() > 150) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O endereço deve ter no máximo 150 caracteres.");
        }
        if (phone != null && phone.length() > 20) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O telefone deve ter no máximo 20 caracteres.");
        }

        this.name = name.trim();
        this.cnpj = cnpj == null || cnpj.isBlank() ? null : cnpj.trim();
        this.address = address == null || address.isBlank() ? null : address.trim();
        this.phone = phone == null || phone.isBlank() ? null : phone.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
