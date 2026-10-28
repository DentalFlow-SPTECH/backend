package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.request.*;
import dental.flow.api.adapters.rest.in.controller.dto.response.*;
import dental.flow.api.application.in.InventoryUseCase;
import dental.flow.api.application.out.InventoryRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.inventory.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryService implements InventoryUseCase {
    private final InventoryRepository repository;
    public InventoryService(InventoryRepository repository) { this.repository = repository; }

    @Override @Transactional
    public InventoryProductResponseDTO createProduct(Long clinicId, Long userId, SaveInventoryProductRequestDTO request) {
        InventoryProduct product = product(clinicId, request, request.initialStock());
        ensureProductName(product, null);
        InventoryProduct saved = repository.saveProduct(product, userId, "CRIAR");
        if (saved.getCurrentStock().signum() > 0) {
            repository.moveStock(saved.getId(), clinicId, userId, "AJUSTE", saved.getCurrentStock(), "Estoque inicial");
        }
        return InventoryProductResponseDTO.from(saved);
    }
    @Override public InventoryProductResponseDTO updateProduct(Long id, Long clinicId, Long userId, SaveInventoryProductRequestDTO request) {
        InventoryProduct existing = findProduct(id, clinicId);
        InventoryProduct updated = product(clinicId, request, existing.getCurrentStock()); updated.setId(id);
        ensureProductName(updated, id); return InventoryProductResponseDTO.from(repository.saveProduct(updated, userId, "ATUALIZAR"));
    }
    @Override public InventoryProductResponseDTO getProduct(Long id, Long clinicId) { return InventoryProductResponseDTO.from(findProduct(id, clinicId)); }
    @Override public List<InventoryProductResponseDTO> listProducts(Long clinicId, Boolean active) {
        return repository.listProducts(clinicId, active).stream().map(InventoryProductResponseDTO::from).toList();
    }
    @Override public StockMovementResponseDTO moveStock(Long id, Long clinicId, Long userId, StockMovementRequestDTO request) {
        InventoryProduct product = findProduct(id, clinicId);
        if (!product.getActive()) throw new BusinessException(Failures.BUSINESS_FAILURE, "Produto inativo não pode movimentar estoque.");
        StockMovement movement = new StockMovement(null, id, userId, request.type(), request.quantity(),
                product.getUnitCost(), request.reason(), null);
        return StockMovementResponseDTO.from(repository.moveStock(id, clinicId, userId, movement.type(), movement.quantity(), movement.reason()));
    }
    @Override public List<StockMovementResponseDTO> listMovements(Long id, Long clinicId) {
        return repository.listMovements(id, clinicId).stream().map(StockMovementResponseDTO::from).toList();
    }
    @Override public EquipmentResponseDTO createEquipment(Long clinicId, Long userId, SaveEquipmentRequestDTO request) {
        Equipment item = equipment(clinicId, request); ensureEquipmentName(item, null);
        return EquipmentResponseDTO.from(repository.saveEquipment(item, userId, "CRIAR"));
    }
    @Override public EquipmentResponseDTO updateEquipment(Long id, Long clinicId, Long userId, SaveEquipmentRequestDTO request) {
        findEquipment(id, clinicId); Equipment item = equipment(clinicId, request); item.setId(id);
        ensureEquipmentName(item, id); return EquipmentResponseDTO.from(repository.saveEquipment(item, userId, "ATUALIZAR"));
    }
    @Override public EquipmentResponseDTO getEquipment(Long id, Long clinicId) { return EquipmentResponseDTO.from(findEquipment(id, clinicId)); }
    @Override public List<EquipmentResponseDTO> listEquipment(Long clinicId, Boolean active) {
        return repository.listEquipment(clinicId, active).stream().map(EquipmentResponseDTO::from).toList();
    }

    private InventoryProduct findProduct(Long id, Long clinicId) { return repository.findProduct(id, clinicId)
            .orElseThrow(() -> new BusinessException(Failures.NOT_FOUND, "Produto não encontrado.")); }
    private Equipment findEquipment(Long id, Long clinicId) { return repository.findEquipment(id, clinicId)
            .orElseThrow(() -> new BusinessException(Failures.NOT_FOUND, "Equipamento não encontrado.")); }
    private void ensureProductName(InventoryProduct p, Long id) { if (repository.productNameExists(p.getClinicId(), p.getName(), id))
        throw new BusinessException(Failures.BUSINESS_FAILURE, "Já existe um produto com este nome na clínica."); }
    private void ensureEquipmentName(Equipment e, Long id) { if (repository.equipmentNameExists(e.getClinicId(), e.getName(), id))
        throw new BusinessException(Failures.BUSINESS_FAILURE, "Já existe um equipamento com este nome na clínica."); }
    private InventoryProduct product(Long clinicId, SaveInventoryProductRequestDTO r, BigDecimal stock) {
        return new InventoryProduct(clinicId, r.name(), r.unit(), r.unitCost(), stock, r.minimumStock(), r.active());
    }
    private Equipment equipment(Long clinicId, SaveEquipmentRequestDTO r) {
        return new Equipment(clinicId, r.name(), r.description(), r.acquisitionValue(), r.usefulLifeUses(), r.completedUses(), r.active());
    }
}
