package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterPatientRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.PatientResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.PatientWebMapper;
import dental.flow.api.application.service.RegisterPatientService;
import dental.flow.api.boot.security.ClinicAccess;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final RegisterPatientService registerPatientService;

    public PatientController(RegisterPatientService registerPatientService) {
        this.registerPatientService = registerPatientService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponseDTO>> register(@RequestBody RegisterPatientRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        PatientResponseDTO data = registerPatientService.register(PatientWebMapper.toCommand(requestDTO));
        return ResponseEntity.status(201).body(ApiResponse.created("Paciente cadastrado com sucesso.", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> getById(@PathVariable Long id) {
        PatientResponseDTO data = registerPatientService.getById(id);
        ClinicAccess.requireClinic(data.clinicId());
        return ResponseEntity.ok(ApiResponse.ok("Paciente consultado com sucesso.", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponseDTO>>> listByClinicAndName(
            @RequestParam Long clinicId,
            @RequestParam(required = false) String name
    ) {
        ClinicAccess.requireClinic(clinicId);
        List<PatientResponseDTO> data = registerPatientService.listByClinicAndName(clinicId, name);
        return ResponseEntity.ok(ApiResponse.ok("Pacientes consultados com sucesso.", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> update(@PathVariable Long id,
                                                                @RequestBody RegisterPatientRequestDTO requestDTO) {
        ClinicAccess.requireClinic(requestDTO.clinicId());
        PatientResponseDTO data = registerPatientService.update(id, PatientWebMapper.toCommand(requestDTO));
        return ResponseEntity.ok(ApiResponse.ok("Paciente atualizado com sucesso.", data));
    }
}
