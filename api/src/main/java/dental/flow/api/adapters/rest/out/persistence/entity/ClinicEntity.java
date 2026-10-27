package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clinica")
public class ClinicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clinica")
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String name;

    @Column(name = "cnpj", unique = true, length = 18)
    private String cnpj;

    @Column(name = "endereco", length = 150)
    private String address;

    @Column(name = "telefone", length = 20)
    private String phone;

    @OneToMany(mappedBy = "clinic")
    private List<UserEntity> users = new ArrayList<>();

    @OneToMany(mappedBy = "clinic")
    private List<PatientEntity> patients = new ArrayList<>();

    @OneToMany(mappedBy = "clinic")
    private List<DentistEntity> dentists = new ArrayList<>();

    @OneToMany(mappedBy = "clinic")
    private List<ProcedureEntity> procedures = new ArrayList<>();

    @OneToMany(mappedBy = "clinic")
    private List<AppointmentEntity> appointments = new ArrayList<>();

    public ClinicEntity() {
    }

    public ClinicEntity(String name, String cnpj, String address, String phone) {
        this.name = name;
        this.cnpj = cnpj;
        this.address = address;
        this.phone = phone;
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

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public List<PatientEntity> getPatients() {
        return patients;
    }

    public void setPatients(List<PatientEntity> patients) {
        this.patients = patients;
    }

    public List<DentistEntity> getDentists() {
        return dentists;
    }

    public void setDentists(List<DentistEntity> dentists) {
        this.dentists = dentists;
    }

    public List<ProcedureEntity> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<ProcedureEntity> procedures) {
        this.procedures = procedures;
    }

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }
}
