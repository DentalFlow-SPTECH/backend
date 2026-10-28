package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Entity
@Immutable
@Table(name = "vw_dashboard_mensal")
@IdClass(MonthlyDashboardId.class)
public class MonthlyDashboardEntity {
    @Id
    @Column(name = "id_clinica")
    private Long clinicId;

    @Id
    @Column(name = "id_dentista")
    private Long dentistId;

    @Id
    @Column(name = "ano")
    private Integer year;

    @Id
    @Column(name = "mes")
    private Integer month;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clinica", insertable = false, updatable = false)
    private ClinicEntity clinic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dentista", insertable = false, updatable = false)
    private DentistEntity dentist;

    @Column(name = "dias_registrados")
    private Long registeredDays;

    @Column(name = "total_clinico")
    private BigDecimal clinicalTotal;

    @Column(name = "total_protese")
    private BigDecimal prosthesisTotal;

    @Column(name = "bruto_clinico")
    private BigDecimal clinicalGross;

    @Column(name = "bruto_pediatria_cirurgia")
    private BigDecimal pediatricSurgeryGross;

    @Column(name = "bruto_geral")
    private BigDecimal generalGross;

    @Column(name = "total_despesas")
    private BigDecimal totalExpenses;

    @Column(name = "desconto_cartoes")
    private BigDecimal cardDiscount;

    @Column(name = "desconto_cheque_pre")
    private BigDecimal predatedCheckDiscount;

    @Column(name = "desconto_cheque_vista")
    private BigDecimal cashCheckDiscount;

    @Column(name = "desconto_vales")
    private BigDecimal voucherDiscount;

    @Column(name = "desconto_outros")
    private BigDecimal otherDiscount;

    @Column(name = "total_liquido")
    private BigDecimal netTotal;

    @Column(name = "saldo_final")
    private BigDecimal finalBalance;

    @Column(name = "qtd_pacientes")
    private Long patientCount;

    @Column(name = "qtd_moldagens")
    private Long moldingCount;

    @Column(name = "qtd_mordidas")
    private Long biteCount;

    @Column(name = "qtd_provas")
    private Long fittingCount;

    @Column(name = "qtd_entregas")
    private Long deliveryCount;

    @Column(name = "qtd_repeticoes")
    private Long repetitionCount;

    public MonthlyDashboardEntity() {
    }

    public Long getClinicId() {
        return clinicId;
    }

    public Long getDentistId() {
        return dentistId;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public ClinicEntity getClinic() {
        return clinic;
    }

    public DentistEntity getDentist() {
        return dentist;
    }

    public Long getRegisteredDays() {
        return registeredDays;
    }

    public BigDecimal getClinicalTotal() {
        return clinicalTotal;
    }

    public BigDecimal getProsthesisTotal() {
        return prosthesisTotal;
    }

    public BigDecimal getClinicalGross() {
        return clinicalGross;
    }

    public BigDecimal getPediatricSurgeryGross() {
        return pediatricSurgeryGross;
    }

    public BigDecimal getGeneralGross() {
        return generalGross;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getCardDiscount() {
        return cardDiscount;
    }

    public BigDecimal getPredatedCheckDiscount() {
        return predatedCheckDiscount;
    }

    public BigDecimal getCashCheckDiscount() {
        return cashCheckDiscount;
    }

    public BigDecimal getVoucherDiscount() {
        return voucherDiscount;
    }

    public BigDecimal getOtherDiscount() {
        return otherDiscount;
    }

    public BigDecimal getNetTotal() {
        return netTotal;
    }

    public BigDecimal getFinalBalance() {
        return finalBalance;
    }

    public Long getPatientCount() {
        return patientCount;
    }

    public Long getMoldingCount() {
        return moldingCount;
    }

    public Long getBiteCount() {
        return biteCount;
    }

    public Long getFittingCount() {
        return fittingCount;
    }

    public Long getDeliveryCount() {
        return deliveryCount;
    }

    public Long getRepetitionCount() {
        return repetitionCount;
    }
}
