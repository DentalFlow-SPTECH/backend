package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clinica", nullable = false)
    private ClinicEntity clinic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dentista", nullable = false)
    private DentistEntity dentist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_procedimento", nullable = false)
    private ProcedureEntity procedure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private UserEntity user;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime scheduledAt;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "observacoes", length = 255)
    private String notes;

    public AppointmentEntity() {
    }

    public AppointmentEntity(Long clinicId, Long patientId, Long dentistId, Long procedureId, Long userId,
                             LocalDateTime scheduledAt, String status, String notes) {
        this.setClinicId(clinicId);
        this.setPatientId(patientId);
        this.setDentistId(dentistId);
        this.setProcedureId(procedureId);
        this.setUserId(userId);
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.notes = notes;
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

    public Long getPatientId() {
        return patient != null ? patient.getId() : null;
    }

    public void setPatientId(Long patientId) {
        if (patientId == null) {
            this.patient = null;
            return;
        }
        if (this.patient == null) {
            this.patient = new PatientEntity();
        }
        this.patient.setId(patientId);
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public Long getDentistId() {
        return dentist != null ? dentist.getId() : null;
    }

    public void setDentistId(Long dentistId) {
        if (dentistId == null) {
            this.dentist = null;
            return;
        }
        if (this.dentist == null) {
            this.dentist = new DentistEntity();
        }
        this.dentist.setId(dentistId);
    }

    public DentistEntity getDentist() {
        return dentist;
    }

    public void setDentist(DentistEntity dentist) {
        this.dentist = dentist;
    }

    public Long getProcedureId() {
        return procedure != null ? procedure.getId() : null;
    }

    public void setProcedureId(Long procedureId) {
        if (procedureId == null) {
            this.procedure = null;
            return;
        }
        if (this.procedure == null) {
            this.procedure = new ProcedureEntity();
        }
        this.procedure.setId(procedureId);
    }

    public ProcedureEntity getProcedure() {
        return procedure;
    }

    public void setProcedure(ProcedureEntity procedure) {
        this.procedure = procedure;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public void setUserId(Long userId) {
        if (userId == null) {
            this.user = null;
            return;
        }
        if (this.user == null) {
            this.user = new UserEntity();
        }
        this.user.setId(userId);
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
