package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterAppointmentRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.AppointmentResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.AppointmentWebMapper;
import dental.flow.api.application.service.RegisterAppointmentService;
import dental.flow.api.boot.security.ClinicAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointment")
@Tag(name = "Agendamentos", description = "Cadastro, consulta e atualização da agenda da clínica")
public class AppointmentController {
    private final RegisterAppointmentService service;

    public AppointmentController(RegisterAppointmentService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um agendamento")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> register(@RequestBody RegisterAppointmentRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        AppointmentResponseDTO data = service.register(AppointmentWebMapper.toCommand(requestDTO));
        return ResponseEntity.status(201).body(ApiResponse.created("Agendamento cadastrado com sucesso.", data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar um agendamento")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> getById(@PathVariable Long id) {
        AppointmentResponseDTO data = service.getById(id);
        ClinicAccess.requireClinic(data.clinicId());
        return ResponseEntity.ok(ApiResponse.ok("Agendamento consultado com sucesso.", data));
    }

    @GetMapping
    @Operation(summary = "Listar a agenda por data e dentista")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> listByDateAndDentist(
            @RequestParam LocalDate date,
            @RequestParam(required = false) Long dentistId
    ) {
        Long clinicId = ClinicAccess.currentUser().clinicId();
        List<AppointmentResponseDTO> data = service.listByDateAndDentist(date, clinicId, dentistId);
        return ResponseEntity.ok(ApiResponse.ok("Agendamentos consultados com sucesso.", data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um agendamento")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> update(@PathVariable Long id,
                                                                      @RequestBody RegisterAppointmentRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        AppointmentResponseDTO data = service.update(id, AppointmentWebMapper.toCommand(requestDTO));
        return ResponseEntity.ok(ApiResponse.ok("Agendamento atualizado com sucesso.", data));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar o status de um agendamento")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> updateStatus(@PathVariable Long id,
                                                                            @RequestParam String status) {
        AppointmentResponseDTO data = service.updateStatus(id, status);
        ClinicAccess.requireClinic(data.clinicId());
        return ResponseEntity.ok(ApiResponse.ok("Status do agendamento atualizado com sucesso.", data));
    }
}
