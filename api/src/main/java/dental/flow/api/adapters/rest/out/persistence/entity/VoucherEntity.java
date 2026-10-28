package dental.flow.api.adapters.rest.out.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vale")
public class VoucherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vale")
    private Long id;
    @Column(name = "id_mapa_diario", nullable = false)
    private Long dailyMapId;
    @Column(name = "nome_responsavel")
    private String responsibleName;
    @Column(name = "numero_ficha")
    private String recordNumber;
    @Column(name = "valor")
    private BigDecimal value;
    @Column(name = "descricao")
    private String description;
    @Column(name = "conferido")
    private Boolean checked;

    public VoucherEntity() {
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

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String v) {
        responsibleName = v;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String v) {
        recordNumber = v;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal v) {
        value = v;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String v) {
        description = v;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean v) {
        checked = v;
    }
}
