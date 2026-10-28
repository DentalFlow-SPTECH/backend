package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paciente")
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clinica", nullable = false)
    private ClinicEntity clinic;

    @Column(name = "nome", nullable = false, length = 100)
    private String name;

    @Column(name = "cpf", nullable = false, length = 14)
    private String cpf;

    @Column(name = "endereco", nullable = false, length = 150)
    private String address;

    @Column(name = "bairro", nullable = false, length = 80)
    private String neighborhood;

    @Column(name = "cep", nullable = false, length = 9)
    private String zipCode;

    @Column(name = "telefone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "data_nascimento")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "patient")
    private List<AppointmentEntity> appointments = new ArrayList<>();

    public PatientEntity() {
    }

    public PatientEntity(Long clinicId, String name, String cpf, String address, String neighborhood,
                         String zipCode, String phone, String email, LocalDate birthDate) {
        this.setClinicId(clinicId);
        this.name = name;
        this.cpf = cpf;
        this.address = address;
        this.neighborhood = neighborhood;
        this.zipCode = zipCode;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
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

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }
}
