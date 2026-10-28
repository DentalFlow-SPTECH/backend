package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterClinicRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.ClinicResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.ClinicWebMapper;
import dental.flow.api.application.service.RegisterClinicService;
import dental.flow.api.boot.security.ClinicAccess;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clinic")
public class ClinicController {

    private final RegisterClinicService registerClinicService;

    public ClinicController(RegisterClinicService registerClinicService) {
        this.registerClinicService = registerClinicService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClinicResponseDTO>> register(@RequestBody RegisterClinicRequestDTO requestDTO) {
        ClinicResponseDTO data = registerClinicService.register(ClinicWebMapper.toCommand(requestDTO));
        return ResponseEntity.status(201).body(ApiResponse.created("Clínica cadastrada com sucesso.", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClinicResponseDTO>> getById(@PathVariable Long id) {
        ClinicAccess.requireClinic(id);
        ClinicResponseDTO data = registerClinicService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok("Clínica consultada com sucesso.", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClinicResponseDTO>> update(@PathVariable Long id,
                                                               @RequestBody RegisterClinicRequestDTO requestDTO) {
        ClinicAccess.requireClinic(id);
        ClinicResponseDTO data = registerClinicService.update(id, ClinicWebMapper.toCommand(requestDTO));
        return ResponseEntity.ok(ApiResponse.ok("Clínica atualizada com sucesso.", data));
    }
}
