package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterPatientRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.PatientResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.PatientWebMapper;
import dental.flow.api.application.service.RegisterPatientService;
import dental.flow.api.boot.security.ClinicAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@Tag(name = "Pacientes", description = "Cadastro, pesquisa e atualização dos pacientes")
public class PatientController {

    private final RegisterPatientService registerPatientService;

    public PatientController(RegisterPatientService registerPatientService) {
        this.registerPatientService = registerPatientService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um paciente")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> register(@RequestBody RegisterPatientRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        PatientResponseDTO data = registerPatientService.register(PatientWebMapper.toCommand(requestDTO));
        return ResponseEntity.status(201).body(ApiResponse.created("Paciente cadastrado com sucesso.", data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar um paciente")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> getById(@PathVariable Long id) {
        PatientResponseDTO data = registerPatientService.getById(id);
        ClinicAccess.requireClinic(data.clinicId());
        return ResponseEntity.ok(ApiResponse.ok("Paciente consultado com sucesso.", data));
    }

    @GetMapping
    @Operation(summary = "Pesquisar pacientes da clínica")
    public ResponseEntity<ApiResponse<List<PatientResponseDTO>>> listByClinicAndName(
            @RequestParam Long clinicId,
            @RequestParam(required = false) String name
    ) {
        ClinicAccess.requireClinic(clinicId);
        List<PatientResponseDTO> data = registerPatientService.listByClinicAndName(clinicId, name);
        return ResponseEntity.ok(ApiResponse.ok("Pacientes consultados com sucesso.", data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um paciente")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> update(@PathVariable Long id,
                                                                  @RequestBody RegisterPatientRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        PatientResponseDTO data = registerPatientService.update(id, PatientWebMapper.toCommand(requestDTO));
        return ResponseEntity.ok(ApiResponse.ok("Paciente atualizado com sucesso.", data));
    }
}
