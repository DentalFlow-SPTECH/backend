package dental.flow.api.domain.user;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

public class User {
    private Long id;
    private String nome;
    private String email;
    private String password;
    private Long clinicId;

    public User(String nome, String email, String password, Long clinicId) {
        if (nome == null || nome.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome é obrigatório.");
        }
        if (nome.length() > 100) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome deve ter no máximo 100 caracteres.");
        }
        if (email == null || email.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O email é obrigatório.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O email é inválido.");
        }
        if (email.length() > 100) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O email deve ter no máximo 100 caracteres.");
        }
        if (password == null || password.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A senha é obrigatória.");
        }
        if (password.length() < 6) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A senha deve conter pelo menos 6 caracteres.");
        }
        if (clinicId == null || clinicId <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A clínica é obrigatória.");
        }

        this.nome = nome.trim();
        this.email = email.trim().toLowerCase();
        this.password = password;
        this.clinicId = clinicId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }
}