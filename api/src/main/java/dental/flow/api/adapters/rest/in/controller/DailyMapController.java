package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.SaveDailyMapRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.DailyMapResponseDTO;
import dental.flow.api.application.in.DailyMapUseCase;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.boot.security.ClinicAccess;
import dental.flow.api.boot.security.DentalFlowUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/daily-map")
@Tag(name = "Mapa diário", description = "Registro operacional e financeiro do dia do dentista")
public class DailyMapController {
    private final DailyMapUseCase service;

    public DailyMapController(DailyMapUseCase service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar o mapa diário")
    public ResponseEntity<ApiResponse<DailyMapResponseDTO>> create(@RequestBody SaveDailyMapRequestDTO request) {
        DentalFlowUserPrincipal user = authenticatedDentist();
        DailyMapResponseDTO data = service.create(
                user.clinicId(),
                user.dentistId(),
                user.userId(),
                request
        );
        return ResponseEntity.status(201)
                .body(ApiResponse.created("Mapa diário criado com sucesso.", data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um mapa diário aberto")
    public ResponseEntity<ApiResponse<DailyMapResponseDTO>> update(@PathVariable Long id, @RequestBody SaveDailyMapRequestDTO request) {
        DentalFlowUserPrincipal user = authenticatedDentist();
        DailyMapResponseDTO data = service.update(
                id,
                user.clinicId(),
                user.dentistId(),
                user.userId(),
                request
        );
        return ResponseEntity.ok(ApiResponse.ok("Mapa diário atualizado com sucesso.", data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar um mapa diário")
    public ResponseEntity<ApiResponse<DailyMapResponseDTO>> get(@PathVariable Long id) {
        DentalFlowUserPrincipal user = authenticatedDentist();
        DailyMapResponseDTO data = service.get(id, user.clinicId(), user.dentistId());
        return ResponseEntity.ok(ApiResponse.ok("Mapa diário consultado com sucesso.", data));
    }

    @GetMapping(params = "date")
    @Operation(summary = "Consultar o mapa diário por data")
    public ResponseEntity<ApiResponse<DailyMapResponseDTO>> getByDate(@RequestParam LocalDate date) {
        DentalFlowUserPrincipal user = authenticatedDentist();
        DailyMapResponseDTO data = service.getByDate(date, user.clinicId(), user.dentistId());
        return ResponseEntity.ok(ApiResponse.ok("Mapa diário consultado com sucesso.", data));
    }

    @GetMapping
    @Operation(summary = "Listar mapas diários por período")
    public ResponseEntity<ApiResponse<List<DailyMapResponseDTO>>> list(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        DentalFlowUserPrincipal user = authenticatedDentist();
        List<DailyMapResponseDTO> data = service.list(start, end, user.clinicId(), user.dentistId());
        return ResponseEntity.ok(ApiResponse.ok("Mapas diários consultados com sucesso.", data));
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "Fechar um mapa diário")
    public ResponseEntity<ApiResponse<DailyMapResponseDTO>> close(@PathVariable Long id) {
        DentalFlowUserPrincipal user = authenticatedDentist();
        DailyMapResponseDTO data = service.close(
                id,
                user.clinicId(),
                user.dentistId(),
                user.userId()
        );
        return ResponseEntity.ok(ApiResponse.ok("Mapa diário fechado com sucesso.", data));
    }

    private DentalFlowUserPrincipal authenticatedDentist() {
        DentalFlowUserPrincipal user = ClinicAccess.currentUser();
        if (user.dentistId() == null) {
            throw new BusinessException(Failures.BUSINESS_FAILURE, "Usuário sem vínculo com dentista.");
        }
        return user;
    }
}
