package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.MonthlyDashboardResponseDTO;
import dental.flow.api.application.out.MonthlyDashboardRepository;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.domain.dashboard.AppointmentMetrics;
import dental.flow.api.domain.dashboard.DailyDashboardPoint;
import dental.flow.api.domain.dashboard.MonthlyDashboard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryMonthlyDashboardServiceTest {
    @Mock
    private MonthlyDashboardRepository repository;

    @InjectMocks
    private QueryMonthlyDashboardService service;

    @Test
    void shouldReturnConsolidatedMonthlyDashboard() {
        MonthlyDashboard first = dashboard(1L, "Clínica Centro", 10L, "Dra. Ana", "1000.00", "100.00", "50.00");
        MonthlyDashboard second = dashboard(1L, "Clínica Centro", 11L, "Dr. Bruno", "500.00", "40.00", "10.00");
        when(repository.findByPeriod(2026, 10, 1L, null)).thenReturn(List.of(first, second));
        when(repository.findDailyEvolution(2026, 10, 1L, null)).thenReturn(List.of(
                new DailyDashboardPoint(LocalDate.of(2026, 10, 1), new BigDecimal("1500.00"),
                        new BigDecimal("140.00"), new BigDecimal("60.00"), new BigDecimal("1360.00"),
                        new BigDecimal("1300.00"), 20L)));
        when(repository.findAppointmentMetrics(2026, 10, 1L, null))
                .thenReturn(new AppointmentMetrics(20L, 2L, 1L, 0L, 14L, 1L, 2L));

        MonthlyDashboardResponseDTO response = service.query(2026, 10, 1L, null);

        assertEquals(new BigDecimal("1500.00"), response.summary().generalGross());
        assertEquals(new BigDecimal("140.00"), response.summary().totalExpenses());
        assertEquals(new BigDecimal("60.00"), response.summary().totalDiscounts());
        assertEquals(new BigDecimal("200.00"), response.summary().totalOutflows());
        assertEquals(new BigDecimal("75.00"), response.kpis().averageTicket());
        assertEquals(new BigDecimal("10.00"), response.kpis().noShowRate());
        assertEquals(1, response.dailyEvolution().size());
        assertEquals(14L, response.appointmentFunnel().completed());
        assertEquals(2, response.breakdown().size());
    }

    @Test
    void shouldRejectInvalidMonthBeforeQueryingRepository() {
        assertThrows(DomainException.class, () -> service.query(2026, 13, null, null));
        verify(repository, never()).findByPeriod(2026, 13, null, null);
    }

    @Test
    void shouldRejectEveryInvalidFilter() {
        assertThrows(DomainException.class, () -> service.query(null, 10, null, null));
        assertThrows(DomainException.class, () -> service.query(0, 10, null, null));
        assertThrows(DomainException.class, () -> service.query(2026, null, null, null));
        assertThrows(DomainException.class, () -> service.query(2026, 0, null, null));
        assertThrows(DomainException.class, () -> service.query(2026, 10, 0L, null));
        assertThrows(DomainException.class, () -> service.query(2026, 10, null, -1L));
        verify(repository, never()).findByPeriod(org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldReturnZeroSummaryWhenPeriodHasNoData() {
        when(repository.findByPeriod(2026, 9, null, null)).thenReturn(List.of());
        when(repository.findDailyEvolution(2026, 9, null, null)).thenReturn(List.of());
        when(repository.findAppointmentMetrics(2026, 9, null, null)).thenReturn(AppointmentMetrics.empty());
        MonthlyDashboardResponseDTO response = service.query(2026, 9, null, null);
        assertEquals(BigDecimal.ZERO, response.summary().totalOutflows());
        assertEquals(0L, response.summary().patientCount());
        assertEquals(List.of(), response.breakdown());
        assertEquals("NO_FINANCIAL_DATA", response.alerts().getFirst().code());
    }

    private MonthlyDashboard dashboard(
            Long clinicId,
            String clinicName,
            Long dentistId,
            String dentistName,
            String generalGross,
            String expenses,
            String cardDiscount
    ) {
        return new MonthlyDashboard(
                clinicId,
                clinicName,
                dentistId,
                dentistName,
                2026,
                10,
                20L,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal(generalGross),
                new BigDecimal(expenses),
                new BigDecimal(cardDiscount),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal(generalGross).subtract(new BigDecimal(expenses)),
                new BigDecimal(generalGross).subtract(new BigDecimal(expenses)).subtract(new BigDecimal(cardDiscount)),
                10L,
                0L,
                0L,
                0L,
                0L,
                0L
        );
    }
}
