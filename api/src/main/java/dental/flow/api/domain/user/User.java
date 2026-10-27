package dental.flow.api.domain.user;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

import java.util.Locale;

public class User {
    private Long id;
    private String name;
    private String login;
    private String password;
    private Long clinicId;
    private String accessLevel;
    private Boolean active;

    public User(String name, String login, String password, Long clinicId) {
        this(name, login, password, clinicId, "ADMIN", true);
    }

    public User(String name, String login, String password, Long clinicId, String accessLevel, Boolean active) {
        if (name == null || name.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome é obrigatório.");
        }
        if (name.length() > 100) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nome deve ter no máximo 100 caracteres.");
        }
        if (login == null || login.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O login é obrigatório.");
        }
        if (login.length() > 50) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O login deve ter no máximo 50 caracteres.");
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

        this.name = name.trim();
        this.login = login.trim().toLowerCase(Locale.ROOT);
        this.password = password;
        this.clinicId = clinicId;
        this.accessLevel = (accessLevel == null || accessLevel.isBlank()) ? "ADMIN" : accessLevel.trim().toUpperCase(Locale.ROOT);
        this.active = active == null ? true : active;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}