package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "usuario")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String name;

    @Column(name = "login", nullable = false, length = 50, unique = true)
    private String login;

    @Column(name = "senha_hash", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clinica", nullable = false)
    private ClinicEntity clinic;

    @Column(name = "nivel_acesso", nullable = false, length = 20)
    private String accessLevel;

    @Column(name = "ativo")
    private Boolean active;

    @OneToMany(mappedBy = "user")
    private List<AppointmentEntity> appointments = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(String name, String login, String password, Long clinicId, String accessLevel, Boolean active) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.setClinicId(clinicId);
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
        return clinic != null ? clinic.getId() : null;
    }

    public void setClinicId(Long clinicId) {
        if (clinicId == null) {
            this.clinic = null;
            return;
        }
        if (this.clinic == null) {
            this.clinic = new ClinicEntity();
        }
        this.clinic.setId(clinicId);
    }

    public ClinicEntity getClinic() {
        return clinic;
    }

    public void setClinic(ClinicEntity clinic) {
        this.clinic = clinic;
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

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }
}
