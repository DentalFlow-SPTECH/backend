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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "procedimento")
public class ProcedureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_procedimento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clinica", nullable = false)
    private ClinicEntity clinic;

    @Column(name = "nome", nullable = false, length = 100)
    private String name;

    @Column(name = "valor")
    private BigDecimal price;

    @Column(name = "duracao_minutos")
    private Integer durationMinutes;

    @OneToMany(mappedBy = "procedure")
    private List<AppointmentEntity> appointments = new ArrayList<>();

    public ProcedureEntity() {}

    public ProcedureEntity(Long clinicId, String name, BigDecimal price, Integer durationMinutes) {
        this.setClinicId(clinicId);
        this.name = name;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public ClinicEntity getClinic() { return clinic; }
    public void setClinic(ClinicEntity clinic) { this.clinic = clinic; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public List<AppointmentEntity> getAppointments() { return appointments; }
    public void setAppointments(List<AppointmentEntity> appointments) { this.appointments = appointments; }
}
