package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterProcedureRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.ProcedureResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.ProcedureWebMapper;
import dental.flow.api.application.service.RegisterProcedureService;
import dental.flow.api.boot.security.ClinicAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/procedure")
@Tag(name = "Procedimentos", description = "Cadastro e consulta dos procedimentos odontológicos")
public class ProcedureController {
    private final RegisterProcedureService service;

    public ProcedureController(RegisterProcedureService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um procedimento")
    public ResponseEntity<ApiResponse<ProcedureResponseDTO>> register(@RequestBody RegisterProcedureRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        ProcedureResponseDTO data = service.register(ProcedureWebMapper.toCommand(requestDTO));
        return ResponseEntity.status(201).body(ApiResponse.created("Procedimento cadastrado com sucesso.", data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar um procedimento")
    public ResponseEntity<ApiResponse<ProcedureResponseDTO>> getById(@PathVariable Long id) {
        ProcedureResponseDTO data = service.getById(id);
        ClinicAccess.requireClinic(data.clinicId());
        return ResponseEntity.ok(ApiResponse.ok("Procedimento consultado com sucesso.", data));
    }

    @GetMapping
    @Operation(summary = "Listar procedimentos da clínica")
    public ResponseEntity<ApiResponse<List<ProcedureResponseDTO>>> listByClinic(@RequestParam Long clinicId) {
        ClinicAccess.requireClinic(clinicId);
        return ResponseEntity.ok(ApiResponse.ok("Procedimentos consultados com sucesso.", service.listByClinic(clinicId)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um procedimento")
    public ResponseEntity<ApiResponse<ProcedureResponseDTO>> update(@PathVariable Long id,
                                                                    @RequestBody RegisterProcedureRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        ProcedureResponseDTO data = service.update(id, ProcedureWebMapper.toCommand(requestDTO));
        return ResponseEntity.ok(ApiResponse.ok("Procedimento atualizado com sucesso.", data));
    }
}
