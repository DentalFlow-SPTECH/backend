package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterDentistRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.DentistResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.DentistWebMapper;
import dental.flow.api.application.service.RegisterDentistService;
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
@RequestMapping("/dentist")
public class DentistController {
    private final RegisterDentistService service;

    public DentistController(RegisterDentistService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DentistResponseDTO>> register(@RequestBody RegisterDentistRequestDTO requestDTO) {
        DentistResponseDTO data = service.register(DentistWebMapper.toCommand(requestDTO));
        return ResponseEntity.status(201).body(ApiResponse.created("Dentista cadastrado com sucesso.", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DentistResponseDTO>> getById(@PathVariable Long id) {
        DentistResponseDTO data = service.getById(id);
        return ResponseEntity.ok(ApiResponse.ok("Dentista consultado com sucesso.", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DentistResponseDTO>>> listByClinic(@RequestParam Long clinicId) {
        List<DentistResponseDTO> data = service.listByClinic(clinicId);
        return ResponseEntity.ok(ApiResponse.ok("Dentistas consultados com sucesso.", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DentistResponseDTO>> update(@PathVariable Long id,
                                                                @RequestBody RegisterDentistRequestDTO requestDTO) {
        DentistResponseDTO data = service.update(id, DentistWebMapper.toCommand(requestDTO));
        return ResponseEntity.ok(ApiResponse.ok("Dentista atualizado com sucesso.", data));
    }
}
