package dental.flow.api.adapters.rest.out.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

public class MonthlyDashboardId implements Serializable {
    private Long clinicId;
    private Long dentistId;
    private Integer year;
    private Integer month;

    public MonthlyDashboardId() {
    }

    public MonthlyDashboardId(Long clinicId, Long dentistId, Integer year, Integer month) {
        this.clinicId = clinicId;
        this.dentistId = dentistId;
        this.year = year;
        this.month = month;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof MonthlyDashboardId that)) {
            return false;
        }
        return Objects.equals(clinicId, that.clinicId)
                && Objects.equals(dentistId, that.dentistId)
                && Objects.equals(year, that.year)
                && Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clinicId, dentistId, year, month);
    }
}
