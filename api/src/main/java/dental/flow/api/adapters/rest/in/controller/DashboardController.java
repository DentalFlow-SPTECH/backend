package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.MonthlyDashboardResponseDTO;
import dental.flow.api.application.in.QueryMonthlyDashboardUseCase;
import dental.flow.api.boot.security.ClinicAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Indicadores mensais, gráficos e alertas da clínica")
public class DashboardController {
    private final QueryMonthlyDashboardUseCase queryMonthlyDashboardUseCase;

    public DashboardController(QueryMonthlyDashboardUseCase queryMonthlyDashboardUseCase) {
        this.queryMonthlyDashboardUseCase = queryMonthlyDashboardUseCase;
    }

    @GetMapping
    @Operation(summary = "Consultar o dashboard mensal")
    public ResponseEntity<ApiResponse<MonthlyDashboardResponseDTO>> getMonthlyDashboard(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long clinicId,
            @RequestParam(required = false) Long dentistId
    ) {
        Long scopedClinicId = ClinicAccess.requireClinic(clinicId);
        MonthlyDashboardResponseDTO data = queryMonthlyDashboardUseCase.query(
                year,
                month,
                scopedClinicId,
                dentistId
        );
        return ResponseEntity.ok(ApiResponse.ok("Dashboard mensal consultado com sucesso.", data));
    }
}
