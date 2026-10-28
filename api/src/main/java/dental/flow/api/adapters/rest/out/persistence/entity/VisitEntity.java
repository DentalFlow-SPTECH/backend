package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "atendimento")
public class VisitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atendimento")
    private Long id;
    @Column(name = "id_mapa_diario", nullable = false)
    private Long dailyMapId;
    @Column(name = "id_agendamento")
    private Long appointmentId;
    @Column(name = "id_paciente")
    private Long patientId;
    @Column(name = "id_dentista")
    private Long dentistId;
    @Column(name = "id_procedimento")
    private Long procedureId;
    @Column(name = "numero_linha", nullable = false)
    private Integer lineNumber;
    @Column(name = "numero_ficha")
    private String recordNumber;
    @Column(name = "nome_paciente")
    private String patientName;
    @Column(name = "hora_agendada")
    private LocalTime scheduledTime;
    @Column(name = "hora_entrada")
    private LocalTime entryTime;
    @Column(name = "hora_saida")
    private LocalTime exitTime;
    @Column(name = "hora_retorno")
    private LocalTime returnTime;
    @Column(name = "codigo_pagamento")
    private String paymentCode;
    @Column(name = "valor")
    private BigDecimal value;
    @Column(name = "tipo_atendimento", nullable = false)
    private String type;
    @Column(name = "conferido")
    private Boolean checked;

    public VisitEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long v) {
        id = v;
    }

    public Long getDailyMapId() {
        return dailyMapId;
    }

    public void setDailyMapId(Long v) {
        dailyMapId = v;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long v) {
        appointmentId = v;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long v) {
        patientId = v;
    }

    public Long getDentistId() {
        return dentistId;
    }

    public void setDentistId(Long v) {
        dentistId = v;
    }

    public Long getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Long v) {
        procedureId = v;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer v) {
        lineNumber = v;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String v) {
        recordNumber = v;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String v) {
        patientName = v;
    }

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalTime v) {
        scheduledTime = v;
    }

    public LocalTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalTime v) {
        entryTime = v;
    }

    public LocalTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalTime v) {
        exitTime = v;
    }

    public LocalTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalTime v) {
        returnTime = v;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String v) {
        paymentCode = v;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal v) {
        value = v;
    }

    public String getType() {
        return type;
    }

    public void setType(String v) {
        type = v;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean v) {
        checked = v;
    }
}
