package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;

@Entity @Table(name="mapa_diario")
public class DailyMapEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_mapa_diario") private Long id;
 @Column(name="id_clinica",nullable=false) private Long clinicId; @Column(name="id_dentista",nullable=false) private Long dentistId;
 @Column(name="id_usuario_atendente") private Long attendantUserId; @Column(name="id_usuario_conferente") private Long checkingUserId;
 @Column(name="data_mapa",nullable=false) private LocalDate date; @Column(name="dia_semana",length=15) private String weekDay;
 @Column(name="data_conferencia") private LocalDate conferenceDate; @Column(name="data_fechamento") private LocalDateTime closedAt;
 @Column(name="status",nullable=false,length=15) private String status;
 @Column(name="total_clinico") private BigDecimal clinicalTotal; @Column(name="total_protese") private BigDecimal prosthesisTotal;
 @Column(name="bruto_clinico") private BigDecimal clinicalGross; @Column(name="bruto_pediatria_cirurgia") private BigDecimal pediatricSurgeryGross;
 @Column(name="bruto_geral") private BigDecimal generalGross; @Column(name="total_despesas") private BigDecimal totalExpenses;
 @Column(name="valor_liquido") private BigDecimal netValue; @Column(name="desconto_cartoes") private BigDecimal cardDiscount;
 @Column(name="desconto_cheque_pre") private BigDecimal predatedCheckDiscount; @Column(name="desconto_cheque_vista") private BigDecimal cashCheckDiscount;
 @Column(name="desconto_vales") private BigDecimal voucherDiscount; @Column(name="desconto_outros") private BigDecimal otherDiscount;
 @Column(name="total_liquido") private BigDecimal netTotal; @Column(name="saldo_final") private BigDecimal finalBalance;
 @Column(name="qtd_pacientes") private Integer patientCount; @Column(name="qtd_moldagens") private Integer moldingCount;
 @Column(name="qtd_mordidas") private Integer biteCount; @Column(name="qtd_provas") private Integer fittingCount;
 @Column(name="qtd_entregas") private Integer deliveryCount; @Column(name="qtd_repeticoes") private Integer repetitionCount;
 @Column(name="observacoes",columnDefinition="TEXT") private String notes;
 public DailyMapEntity(){}
 public Long getId(){return id;} public void setId(Long v){id=v;} public Long getClinicId(){return clinicId;} public void setClinicId(Long v){clinicId=v;} public Long getDentistId(){return dentistId;} public void setDentistId(Long v){dentistId=v;}
 public Long getAttendantUserId(){return attendantUserId;} public void setAttendantUserId(Long v){attendantUserId=v;} public Long getCheckingUserId(){return checkingUserId;} public void setCheckingUserId(Long v){checkingUserId=v;} public LocalDate getDate(){return date;} public void setDate(LocalDate v){date=v;} public String getWeekDay(){return weekDay;} public void setWeekDay(String v){weekDay=v;} public LocalDate getConferenceDate(){return conferenceDate;} public void setConferenceDate(LocalDate v){conferenceDate=v;} public LocalDateTime getClosedAt(){return closedAt;} public void setClosedAt(LocalDateTime v){closedAt=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public BigDecimal getClinicalTotal(){return clinicalTotal;} public void setClinicalTotal(BigDecimal v){clinicalTotal=v;} public BigDecimal getProsthesisTotal(){return prosthesisTotal;} public void setProsthesisTotal(BigDecimal v){prosthesisTotal=v;} public BigDecimal getClinicalGross(){return clinicalGross;} public void setClinicalGross(BigDecimal v){clinicalGross=v;} public BigDecimal getPediatricSurgeryGross(){return pediatricSurgeryGross;} public void setPediatricSurgeryGross(BigDecimal v){pediatricSurgeryGross=v;} public BigDecimal getGeneralGross(){return generalGross;} public void setGeneralGross(BigDecimal v){generalGross=v;} public BigDecimal getTotalExpenses(){return totalExpenses;} public void setTotalExpenses(BigDecimal v){totalExpenses=v;} public BigDecimal getNetValue(){return netValue;} public void setNetValue(BigDecimal v){netValue=v;}
 public BigDecimal getCardDiscount(){return cardDiscount;} public void setCardDiscount(BigDecimal v){cardDiscount=v;} public BigDecimal getPredatedCheckDiscount(){return predatedCheckDiscount;} public void setPredatedCheckDiscount(BigDecimal v){predatedCheckDiscount=v;} public BigDecimal getCashCheckDiscount(){return cashCheckDiscount;} public void setCashCheckDiscount(BigDecimal v){cashCheckDiscount=v;} public BigDecimal getVoucherDiscount(){return voucherDiscount;} public void setVoucherDiscount(BigDecimal v){voucherDiscount=v;} public BigDecimal getOtherDiscount(){return otherDiscount;} public void setOtherDiscount(BigDecimal v){otherDiscount=v;} public BigDecimal getNetTotal(){return netTotal;} public void setNetTotal(BigDecimal v){netTotal=v;} public BigDecimal getFinalBalance(){return finalBalance;} public void setFinalBalance(BigDecimal v){finalBalance=v;}
 public Integer getPatientCount(){return patientCount;} public void setPatientCount(Integer v){patientCount=v;} public Integer getMoldingCount(){return moldingCount;} public void setMoldingCount(Integer v){moldingCount=v;} public Integer getBiteCount(){return biteCount;} public void setBiteCount(Integer v){biteCount=v;} public Integer getFittingCount(){return fittingCount;} public void setFittingCount(Integer v){fittingCount=v;} public Integer getDeliveryCount(){return deliveryCount;} public void setDeliveryCount(Integer v){deliveryCount=v;} public Integer getRepetitionCount(){return repetitionCount;} public void setRepetitionCount(Integer v){repetitionCount=v;} public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}
