package dental.flow.api.application.in;

import dental.flow.api.adapters.rest.in.controller.dto.request.SaveEquipmentRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.SaveInventoryProductRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.StockMovementRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.EquipmentResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.InventoryProductResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.StockMovementResponseDTO;

import java.util.List;

public interface InventoryUseCase {
    InventoryProductResponseDTO createProduct(Long clinicId, Long userId, SaveInventoryProductRequestDTO request);

    InventoryProductResponseDTO updateProduct(Long id, Long clinicId, Long userId, SaveInventoryProductRequestDTO request);

    InventoryProductResponseDTO getProduct(Long id, Long clinicId);

    List<InventoryProductResponseDTO> listProducts(Long clinicId, Boolean active);

    StockMovementResponseDTO moveStock(Long id, Long clinicId, Long userId, StockMovementRequestDTO request);

    List<StockMovementResponseDTO> listMovements(Long id, Long clinicId);

    EquipmentResponseDTO createEquipment(Long clinicId, Long userId, SaveEquipmentRequestDTO request);

    EquipmentResponseDTO updateEquipment(Long id, Long clinicId, Long userId, SaveEquipmentRequestDTO request);

    EquipmentResponseDTO getEquipment(Long id, Long clinicId);

    List<EquipmentResponseDTO> listEquipment(Long clinicId, Boolean active);
}
