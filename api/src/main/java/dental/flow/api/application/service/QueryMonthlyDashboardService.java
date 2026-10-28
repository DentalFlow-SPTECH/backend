package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.MonthlyDashboardResponseDTO;
import dental.flow.api.application.in.QueryMonthlyDashboardUseCase;
import dental.flow.api.application.out.MonthlyDashboardRepository;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.dashboard.AppointmentMetrics;
import dental.flow.api.domain.dashboard.DailyDashboardPoint;
import dental.flow.api.domain.dashboard.MonthlyDashboard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QueryMonthlyDashboardService implements QueryMonthlyDashboardUseCase {
    private final MonthlyDashboardRepository repository;

    public QueryMonthlyDashboardService(MonthlyDashboardRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public MonthlyDashboardResponseDTO query(Integer year, Integer month, Long clinicId, Long dentistId) {
        validateFilters(year, month, clinicId, dentistId);
        List<MonthlyDashboard> dashboards = repository.findByPeriod(year, month, clinicId, dentistId);
        List<DailyDashboardPoint> dailyEvolution = repository.findDailyEvolution(year, month, clinicId, dentistId);
        AppointmentMetrics appointmentMetrics = repository.findAppointmentMetrics(year, month, clinicId, dentistId);
        return MonthlyDashboardResponseDTO.from(
                year, month, clinicId, dentistId, dashboards, dailyEvolution, appointmentMetrics);
    }

    private void validateFilters(Integer year, Integer month, Long clinicId, Long dentistId) {
        if (year == null || year <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O ano deve ser informado e maior que zero.");
        }
        if (month == null || month < 1 || month > 12) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O mês deve estar entre 1 e 12.");
        }
        if (clinicId != null && clinicId <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O identificador da clínica deve ser maior que zero.");
        }
        if (dentistId != null && dentistId <= 0) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O identificador do dentista deve ser maior que zero.");
        }
    }
}
