package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dentista")
public class DentistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dentista")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clinica", nullable = false)
    private ClinicEntity clinic;

    @Column(name = "nome", nullable = false, length = 100)
    private String name;

    @Column(name = "cro", nullable = false, length = 20, unique = true)
    private String cro;

    @Column(name = "especialidade", length = 50)
    private String specialty;

    @Column(name = "telefone", length = 20)
    private String phone;

    @OneToMany(mappedBy = "dentist")
    private List<AppointmentEntity> appointments = new ArrayList<>();

    public DentistEntity() {
    }

    public DentistEntity(Long clinicId, String name, String cro, String specialty, String phone) {
        this.setClinicId(clinicId);
        this.name = name;
        this.cro = cro;
        this.specialty = specialty;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }
}
