package dental.flow.api.application.in;

import dental.flow.api.adapters.rest.in.controller.dto.response.MonthlyDashboardResponseDTO;

public interface QueryMonthlyDashboardUseCase {
    MonthlyDashboardResponseDTO query(Integer year, Integer month, Long clinicId, Long dentistId);
}
