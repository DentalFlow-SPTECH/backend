package dental.flow.api.adapters.rest.out.persistence.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DailyDashboardProjection {
    LocalDate getMapDate();
    BigDecimal getGeneralGross();
    BigDecimal getTotalExpenses();
    BigDecimal getTotalDiscounts();
    BigDecimal getNetTotal();
    BigDecimal getFinalBalance();
    Long getPatientCount();
}
