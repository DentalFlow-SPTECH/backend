package dental.flow.api.domain.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyDashboardPoint(
        LocalDate date,
        BigDecimal generalGross,
        BigDecimal totalExpenses,
        BigDecimal totalDiscounts,
        BigDecimal netTotal,
        BigDecimal finalBalance,
        Long patientCount
) {
}
