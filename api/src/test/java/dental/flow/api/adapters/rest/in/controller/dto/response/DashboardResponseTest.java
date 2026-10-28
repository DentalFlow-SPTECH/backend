package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.dashboard.AppointmentMetrics;
import dental.flow.api.domain.dashboard.DailyDashboardPoint;
import dental.flow.api.domain.dashboard.MonthlyDashboard;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DashboardResponseTest {
    @Test
    void shouldCalculateKpisChartsAndActionableAlerts() {
        MonthlyDashboard dashboard = new MonthlyDashboard(
                1L, "Clínica", 2L, "Dra. Ana", 2026, 10, 20L,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                new BigDecimal("1000.00"), new BigDecimal("400.00"),
                new BigDecimal("200.00"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                new BigDecimal("600.00"), new BigDecimal("-50.00"),
                10L, 0L, 0L, 0L, 0L, 1L);
        DailyDashboardPoint point = new DailyDashboardPoint(
                LocalDate.of(2026, 10, 1), new BigDecimal("1000.00"), new BigDecimal("400.00"),
                new BigDecimal("200.00"), new BigDecimal("600.00"), new BigDecimal("-50.00"), 10L);

        MonthlyDashboardResponseDTO response = MonthlyDashboardResponseDTO.from(
                2026, 10, 1L, null, List.of(dashboard), List.of(point),
                new AppointmentMetrics(10L, 1L, 0L, 0L, 6L, 1L, 2L));

        assertEquals(new BigDecimal("100.00"), response.kpis().averageTicket());
        assertEquals(new BigDecimal("40.00"), response.kpis().expenseRate());
        assertEquals(new BigDecimal("20.00"), response.appointmentFunnel().noShowRate());
        assertEquals(new BigDecimal("600.00"), response.dailyEvolution().getFirst().totalOutflows());
        assertEquals(new BigDecimal("100.00"), response.breakdown().getFirst().averageTicket());
        assertTrue(response.alerts().stream().anyMatch(alert -> "NEGATIVE_BALANCE".equals(alert.code())));
        assertTrue(response.alerts().stream().anyMatch(alert -> "HIGH_EXPENSE_RATE".equals(alert.code())));
        assertTrue(response.alerts().stream().anyMatch(alert -> "HIGH_DISCOUNT_RATE".equals(alert.code())));
        assertTrue(response.alerts().stream().anyMatch(alert -> "HIGH_NO_SHOW_RATE".equals(alert.code())));
        assertTrue(response.alerts().stream().anyMatch(alert -> "HIGH_REPETITION_RATE".equals(alert.code())));
    }
}
