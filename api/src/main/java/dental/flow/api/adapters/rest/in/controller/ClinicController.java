package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterClinicRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.ClinicResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.ClinicWebMapper;
import dental.flow.api.application.service.RegisterClinicService;
import dental.flow.api.boot.security.ClinicAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clinic")
@Tag(name = "Clínicas", description = "Cadastro e manutenção das clínicas")
public class ClinicController {

    private final RegisterClinicService registerClinicService;

    public ClinicController(RegisterClinicService registerClinicService) {
        this.registerClinicService = registerClinicService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma clínica")
    public ResponseEntity<ApiResponse<ClinicResponseDTO>> register(@RequestBody RegisterClinicRequestDTO requestDTO) {
        ClinicResponseDTO data = registerClinicService.register(ClinicWebMapper.toCommand(requestDTO));
        return ResponseEntity.status(201).body(ApiResponse.created("Clínica cadastrada com sucesso.", data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar uma clínica")
    public ResponseEntity<ApiResponse<ClinicResponseDTO>> getById(@PathVariable Long id) {
        ClinicAccess.requireClinic(id);
        ClinicResponseDTO data = registerClinicService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok("Clínica consultada com sucesso.", data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma clínica")
    public ResponseEntity<ApiResponse<ClinicResponseDTO>> update(@PathVariable Long id,
                                                                 @RequestBody RegisterClinicRequestDTO requestDTO) {
        ClinicAccess.requireClinic(id);
        ClinicResponseDTO data = registerClinicService.update(id, ClinicWebMapper.toCommand(requestDTO));
        return ResponseEntity.ok(ApiResponse.ok("Clínica atualizada com sucesso.", data));
    }
}
