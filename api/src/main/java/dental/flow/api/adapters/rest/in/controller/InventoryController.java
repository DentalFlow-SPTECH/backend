package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.*;
import dental.flow.api.adapters.rest.in.controller.dto.response.*;
import dental.flow.api.application.in.InventoryUseCase;
import dental.flow.api.boot.security.ClinicAccess;
import dental.flow.api.boot.security.DentalFlowUserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryUseCase service;
    public InventoryController(InventoryUseCase service) { this.service = service; }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<InventoryProductResponseDTO>> createProduct(@RequestBody SaveInventoryProductRequestDTO request) {
        DentalFlowUserPrincipal user = ClinicAccess.currentUser();
        return ResponseEntity.status(201).body(ApiResponse.created("Produto cadastrado com sucesso.",
                service.createProduct(user.clinicId(), user.userId(), request)));
    }
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<InventoryProductResponseDTO>>> listProducts(@RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(ApiResponse.ok("Produtos consultados com sucesso.",
                service.listProducts(ClinicAccess.currentUser().clinicId(), active)));
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<InventoryProductResponseDTO>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Produto consultado com sucesso.",
                service.getProduct(id, ClinicAccess.currentUser().clinicId())));
    }
    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<InventoryProductResponseDTO>> updateProduct(@PathVariable Long id, @RequestBody SaveInventoryProductRequestDTO request) {
        DentalFlowUserPrincipal user = ClinicAccess.currentUser();
        return ResponseEntity.ok(ApiResponse.ok("Produto atualizado com sucesso.",
                service.updateProduct(id, user.clinicId(), user.userId(), request)));
    }
    @PostMapping("/products/{id}/movements")
    public ResponseEntity<ApiResponse<StockMovementResponseDTO>> moveStock(@PathVariable Long id, @RequestBody StockMovementRequestDTO request) {
        DentalFlowUserPrincipal user = ClinicAccess.currentUser();
        return ResponseEntity.status(201).body(ApiResponse.created("Estoque movimentado com sucesso.",
                service.moveStock(id, user.clinicId(), user.userId(), request)));
    }
    @GetMapping("/products/{id}/movements")
    public ResponseEntity<ApiResponse<List<StockMovementResponseDTO>>> listMovements(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Movimentações consultadas com sucesso.",
                service.listMovements(id, ClinicAccess.currentUser().clinicId())));
    }
    @PostMapping("/equipment")
    public ResponseEntity<ApiResponse<EquipmentResponseDTO>> createEquipment(@RequestBody SaveEquipmentRequestDTO request) {
        DentalFlowUserPrincipal user = ClinicAccess.currentUser();
        return ResponseEntity.status(201).body(ApiResponse.created("Equipamento cadastrado com sucesso.",
                service.createEquipment(user.clinicId(), user.userId(), request)));
    }
    @GetMapping("/equipment")
    public ResponseEntity<ApiResponse<List<EquipmentResponseDTO>>> listEquipment(@RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(ApiResponse.ok("Equipamentos consultados com sucesso.",
                service.listEquipment(ClinicAccess.currentUser().clinicId(), active)));
    }
    @GetMapping("/equipment/{id}")
    public ResponseEntity<ApiResponse<EquipmentResponseDTO>> getEquipment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Equipamento consultado com sucesso.",
                service.getEquipment(id, ClinicAccess.currentUser().clinicId())));
    }
    @PutMapping("/equipment/{id}")
    public ResponseEntity<ApiResponse<EquipmentResponseDTO>> updateEquipment(@PathVariable Long id, @RequestBody SaveEquipmentRequestDTO request) {
        DentalFlowUserPrincipal user = ClinicAccess.currentUser();
        return ResponseEntity.ok(ApiResponse.ok("Equipamento atualizado com sucesso.",
                service.updateEquipment(id, user.clinicId(), user.userId(), request)));
    }
}
