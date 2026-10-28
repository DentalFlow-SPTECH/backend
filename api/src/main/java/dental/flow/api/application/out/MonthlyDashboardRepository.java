package dental.flow.api.application.out;

import dental.flow.api.domain.dashboard.MonthlyDashboard;
import dental.flow.api.domain.dashboard.DailyDashboardPoint;
import dental.flow.api.domain.dashboard.AppointmentMetrics;

import java.util.List;

public interface MonthlyDashboardRepository {
    List<MonthlyDashboard> findByPeriod(Integer year, Integer month, Long clinicId, Long dentistId);
    List<DailyDashboardPoint> findDailyEvolution(Integer year, Integer month, Long clinicId, Long dentistId);
    AppointmentMetrics findAppointmentMetrics(Integer year, Integer month, Long clinicId, Long dentistId);
}
