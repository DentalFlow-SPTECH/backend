package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterDentistRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.DentistResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.DentistWebMapper;
import dental.flow.api.application.service.RegisterDentistService;
import dental.flow.api.boot.security.ClinicAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dentist")
@Tag(name = "Dentistas", description = "Cadastro e consulta dos dentistas")
public class DentistController {
    private final RegisterDentistService service;

    public DentistController(RegisterDentistService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um dentista")
    public ResponseEntity<ApiResponse<DentistResponseDTO>> register(@RequestBody RegisterDentistRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        DentistResponseDTO data = service.register(DentistWebMapper.toCommand(requestDTO));
        return ResponseEntity.status(201).body(ApiResponse.created("Dentista cadastrado com sucesso.", data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar um dentista")
    public ResponseEntity<ApiResponse<DentistResponseDTO>> getById(@PathVariable Long id) {
        DentistResponseDTO data = service.getById(id);
        ClinicAccess.requireClinic(data.clinicId());
        return ResponseEntity.ok(ApiResponse.ok("Dentista consultado com sucesso.", data));
    }

    @GetMapping
    @Operation(summary = "Listar dentistas da clínica")
    public ResponseEntity<ApiResponse<List<DentistResponseDTO>>> listByClinic(@RequestParam Long clinicId) {
        ClinicAccess.requireClinic(clinicId);
        List<DentistResponseDTO> data = service.listByClinic(clinicId);
        return ResponseEntity.ok(ApiResponse.ok("Dentistas consultados com sucesso.", data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um dentista")
    public ResponseEntity<ApiResponse<DentistResponseDTO>> update(@PathVariable Long id,
                                                                  @RequestBody RegisterDentistRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        DentistResponseDTO data = service.update(id, DentistWebMapper.toCommand(requestDTO));
        return ResponseEntity.ok(ApiResponse.ok("Dentista atualizado com sucesso.", data));
    }
}
