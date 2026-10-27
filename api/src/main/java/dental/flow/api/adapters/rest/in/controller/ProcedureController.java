package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterProcedureRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.ProcedureResponseDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.ProcedureWebMapper;
import dental.flow.api.application.service.RegisterProcedureService;
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
@RequestMapping("/procedure")
public class ProcedureController {
    private final RegisterProcedureService service;

    public ProcedureController(RegisterProcedureService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProcedureResponseDTO>> register(@RequestBody RegisterProcedureRequestDTO requestDTO) {
        ProcedureResponseDTO data = service.register(ProcedureWebMapper.toCommand(requestDTO));
        return ResponseEntity.status(201).body(ApiResponse.created("Procedimento cadastrado com sucesso.", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProcedureResponseDTO>> getById(@PathVariable Long id) {
        ProcedureResponseDTO data = service.getById(id);
        return ResponseEntity.ok(ApiResponse.ok("Procedimento consultado com sucesso.", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProcedureResponseDTO>>> listByClinic(@RequestParam Long clinicId) {
        return ResponseEntity.ok(ApiResponse.ok("Procedimentos consultados com sucesso.", service.listByClinic(clinicId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProcedureResponseDTO>> update(@PathVariable Long id,
                                                                    @RequestBody RegisterProcedureRequestDTO requestDTO) {
        ProcedureResponseDTO data = service.update(id, ProcedureWebMapper.toCommand(requestDTO));
        return ResponseEntity.ok(ApiResponse.ok("Procedimento atualizado com sucesso.", data));
    }
}
