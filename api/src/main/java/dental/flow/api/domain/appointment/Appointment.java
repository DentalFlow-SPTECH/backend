package dental.flow.api.domain.appointment;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

public class Appointment {
    public static final Set<String> VALID_STATUS = Set.of(
            "AGENDADO",
            "CONFIRMADO",
            "EM_ATENDIMENTO",
            "CONCLUIDO",
            "CANCELADO",
            "FALTOU"
    );

    private Long id;
    private Long clinicId;
    private Long patientId;
    private Long dentistId;
    private Long procedureId;
    private Long userId;
    private LocalDateTime scheduledAt;
    private String status;
    private String notes;

    public Appointment(Long clinicId, Long patientId, Long dentistId, Long procedureId, Long userId,
                       LocalDateTime scheduledAt, String status, String notes) {
        if (clinicId == null || clinicId <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A clínica é obrigatória.");
        }
        if (patientId == null || patientId <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O paciente é obrigatório.");
        }
        if (dentistId == null || dentistId <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O dentista é obrigatório.");
        }
        if (procedureId == null || procedureId <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O procedimento é obrigatório.");
        }
        if (scheduledAt == null) {
            throw new DomainException(Failures.DOMAIN_ERROR, "A data e hora do agendamento são obrigatórias.");
        }
        if (notes != null && notes.length() > 255) {
            throw new DomainException(Failures.DOMAIN_ERROR, "As observações devem ter no máximo 255 caracteres.");
        }

        String normalizedStatus = (status == null || status.isBlank()) ? "AGENDADO" : status.trim().toUpperCase(Locale.ROOT);
        if (!VALID_STATUS.contains(normalizedStatus)) {
            throw new DomainException(Failures.DOMAIN_ERROR, "Status do agendamento inválido.");
        }

        this.clinicId = clinicId;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.procedureId = procedureId;
        this.userId = userId;
        this.scheduledAt = scheduledAt;
        this.status = normalizedStatus;
        this.notes = notes == null || notes.isBlank() ? null : notes.trim();
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

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDentistId() {
        return dentistId;
    }

    public void setDentistId(Long dentistId) {
        this.dentistId = dentistId;
    }

    public Long getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Long procedureId) {
        this.procedureId = procedureId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        if (status == null || status.isBlank()) {
            this.status = "AGENDADO";
            return;
        }
        String normalized = status.trim().toUpperCase(Locale.ROOT);
        if (!VALID_STATUS.contains(normalized)) {
            throw new DomainException(Failures.DOMAIN_ERROR, "Status do agendamento inválido.");
        }
        this.status = normalized;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null || notes.isBlank() ? null : notes.trim();
    }

    public boolean isConflictCandidate() {
        return !("CANCELADO".equals(status) || "FALTOU".equals(status));
    }
}
