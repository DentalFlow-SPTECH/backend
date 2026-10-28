package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.mapper.MonthlyDashboardPersistenceMapper;
import dental.flow.api.application.out.MonthlyDashboardRepository;
import dental.flow.api.boot.exception.DatabaseException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.dashboard.MonthlyDashboard;
import dental.flow.api.domain.dashboard.DailyDashboardPoint;
import dental.flow.api.domain.dashboard.AppointmentMetrics;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MonthlyDashboardRepositoryImpl implements MonthlyDashboardRepository {
    private final MonthlyDashboardJpaRepository repository;

    public MonthlyDashboardRepositoryImpl(MonthlyDashboardJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MonthlyDashboard> findByPeriod(Integer year, Integer month, Long clinicId, Long dentistId) {
        try {
            return repository.findByPeriod(year, month, clinicId, dentistId)
                    .stream()
                    .map(MonthlyDashboardPersistenceMapper::toDomain)
                    .toList();
        } catch (DataAccessException exception) {
            throw new DatabaseException(
                    Failures.DATABASE_ERROR,
                    "Erro ao consultar o dashboard mensal.",
                    exception
            );
        }
    }

    @Override
    public List<DailyDashboardPoint> findDailyEvolution(Integer year, Integer month, Long clinicId, Long dentistId) {
        try {
            return repository.findDailyEvolution(year, month, clinicId, dentistId).stream()
                    .map(point -> new DailyDashboardPoint(
                            point.getMapDate(), value(point.getGeneralGross()), value(point.getTotalExpenses()),
                            value(point.getTotalDiscounts()), value(point.getNetTotal()),
                            value(point.getFinalBalance()), value(point.getPatientCount())))
                    .toList();
        } catch (DataAccessException exception) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Erro ao consultar a evolução diária.", exception);
        }
    }

    @Override
    public AppointmentMetrics findAppointmentMetrics(Integer year, Integer month, Long clinicId, Long dentistId) {
        try {
            AppointmentMetricsProjection metrics = repository.findAppointmentMetrics(year, month, clinicId, dentistId);
            if (metrics == null) {
                return AppointmentMetrics.empty();
            }
            return new AppointmentMetrics(
                    value(metrics.getTotalAppointments()), value(metrics.getScheduledAppointments()),
                    value(metrics.getConfirmedAppointments()), value(metrics.getInProgressAppointments()),
                    value(metrics.getCompletedAppointments()), value(metrics.getCancelledAppointments()),
                    value(metrics.getNoShowAppointments()));
        } catch (DataAccessException exception) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Erro ao consultar os indicadores da agenda.", exception);
        }
    }

    private static java.math.BigDecimal value(java.math.BigDecimal value) {
        return value == null ? java.math.BigDecimal.ZERO : value;
    }

    private static Long value(Long value) {
        return value == null ? 0L : value;
    }
}
