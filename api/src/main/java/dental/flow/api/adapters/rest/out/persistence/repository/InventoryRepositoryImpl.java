package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.*;
import dental.flow.api.application.out.InventoryRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.inventory.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {
    private final InventoryProductJpaRepository products;
    private final EquipmentJpaRepository equipment;
    private final StockMovementJpaRepository movements;
    private final AuditJpaRepository audits;

    public InventoryRepositoryImpl(InventoryProductJpaRepository products, EquipmentJpaRepository equipment,
                                   StockMovementJpaRepository movements, AuditJpaRepository audits) {
        this.products = products;
        this.equipment = equipment;
        this.movements = movements;
        this.audits = audits;
    }

    @Override @Transactional public InventoryProduct saveProduct(InventoryProduct product, Long userId, String action) {
        String previous = product.getId() == null ? null : products.findById(product.getId())
                .map(value -> productSummary(toDomain(value))).orElse(null);
        InventoryProductEntity entity = toEntity(product);
        InventoryProduct saved = toDomain(products.saveAndFlush(entity));
        audit("insumo", saved.getId(), action, previous, productSummary(saved), userId);
        return saved;
    }
    @Override public Optional<InventoryProduct> findProduct(Long id, Long clinicId) {
        return products.findByIdAndClinicId(id, clinicId).map(InventoryRepositoryImpl::toDomain);
    }
    @Override public List<InventoryProduct> listProducts(Long clinicId, Boolean active) {
        List<InventoryProductEntity> found = active == null ? products.findByClinicIdOrderByName(clinicId)
                : products.findByClinicIdAndActiveOrderByName(clinicId, active);
        return found.stream().map(InventoryRepositoryImpl::toDomain).toList();
    }
    @Override public boolean productNameExists(Long clinicId, String name, Long ignoredId) {
        return products.existsByClinicIdAndNameIgnoreCaseAndIdNot(clinicId, name, ignoredId == null ? -1L : ignoredId);
    }

    @Override
    @Transactional
    public StockMovement moveStock(Long productId, Long clinicId, Long userId, String type,
                                   BigDecimal quantity, String reason) {
        InventoryProductEntity product = products.findLockedByIdAndClinicId(productId, clinicId)
                .orElseThrow(() -> new BusinessException(Failures.NOT_FOUND, "Produto não encontrado."));
        BigDecimal next = switch (type) {
            case "ENTRADA" -> product.getCurrentStock().add(quantity);
            case "SAIDA" -> product.getCurrentStock().subtract(quantity);
            case "AJUSTE" -> quantity;
            default -> throw new BusinessException(Failures.BUSINESS_FAILURE, "Tipo de movimentação inválido.");
        };
        if (next.signum() < 0) {
            throw new BusinessException(Failures.BUSINESS_FAILURE, "Estoque insuficiente para a saída informada.");
        }
        product.setCurrentStock(next);
        products.save(product);
        StockMovementEntity movement = new StockMovementEntity();
        movement.setProductId(productId);
        movement.setUserId(userId);
        movement.setType(type);
        movement.setQuantity(quantity);
        movement.setUnitCostAtTime(product.getUnitCost());
        movement.setReason(reason);
        movement.setOccurredAt(LocalDateTime.now());
        return toDomain(movements.saveAndFlush(movement));
    }

    @Override public List<StockMovement> listMovements(Long productId, Long clinicId) {
        if (products.findByIdAndClinicId(productId, clinicId).isEmpty()) {
            throw new BusinessException(Failures.NOT_FOUND, "Produto não encontrado.");
        }
        return movements.findByProductIdOrderByOccurredAtDesc(productId).stream()
                .map(InventoryRepositoryImpl::toDomain).toList();
    }
    @Override @Transactional public Equipment saveEquipment(Equipment item, Long userId, String action) {
        String previous = item.getId() == null ? null : equipment.findById(item.getId())
                .map(value -> equipmentSummary(toDomain(value))).orElse(null);
        Equipment saved = toDomain(equipment.saveAndFlush(toEntity(item)));
        audit("equipamento", saved.getId(), action, previous, equipmentSummary(saved), userId);
        return saved;
    }
    @Override public Optional<Equipment> findEquipment(Long id, Long clinicId) {
        return equipment.findByIdAndClinicId(id, clinicId).map(InventoryRepositoryImpl::toDomain);
    }
    @Override public List<Equipment> listEquipment(Long clinicId, Boolean active) {
        List<EquipmentEntity> found = active == null ? equipment.findByClinicIdOrderByName(clinicId)
                : equipment.findByClinicIdAndActiveOrderByName(clinicId, active);
        return found.stream().map(InventoryRepositoryImpl::toDomain).toList();
    }
    @Override public boolean equipmentNameExists(Long clinicId, String name, Long ignoredId) {
        return equipment.existsByClinicIdAndNameIgnoreCaseAndIdNot(clinicId, name, ignoredId == null ? -1L : ignoredId);
    }

    private static InventoryProductEntity toEntity(InventoryProduct value) {
        InventoryProductEntity entity = new InventoryProductEntity(); entity.setId(value.getId());
        entity.setClinicId(value.getClinicId()); entity.setName(value.getName()); entity.setUnit(value.getUnit());
        entity.setUnitCost(value.getUnitCost()); entity.setCurrentStock(value.getCurrentStock());
        entity.setMinimumStock(value.getMinimumStock()); entity.setActive(value.getActive()); return entity;
    }
    private static InventoryProduct toDomain(InventoryProductEntity value) {
        InventoryProduct product = new InventoryProduct(value.getClinicId(), value.getName(), value.getUnit(),
                value.getUnitCost(), value.getCurrentStock(), value.getMinimumStock(), value.getActive());
        product.setId(value.getId()); return product;
    }
    private static EquipmentEntity toEntity(Equipment value) {
        EquipmentEntity entity = new EquipmentEntity(); entity.setId(value.getId()); entity.setClinicId(value.getClinicId());
        entity.setName(value.getName()); entity.setDescription(value.getDescription());
        entity.setAcquisitionValue(value.getAcquisitionValue()); entity.setUsefulLifeUses(value.getUsefulLifeUses());
        entity.setCompletedUses(value.getCompletedUses()); entity.setCostPerUse(value.getCostPerUse());
        entity.setActive(value.getActive()); return entity;
    }
    private static Equipment toDomain(EquipmentEntity value) {
        Equipment item = new Equipment(value.getClinicId(), value.getName(), value.getDescription(),
                value.getAcquisitionValue(), value.getUsefulLifeUses(), value.getCompletedUses(), value.getActive());
        item.setId(value.getId()); return item;
    }
    private static StockMovement toDomain(StockMovementEntity value) {
        return new StockMovement(value.getId(), value.getProductId(), value.getUserId(), value.getType(),
                value.getQuantity(), value.getUnitCostAtTime(), value.getReason(), value.getOccurredAt());
    }
    private void audit(String table, Long id, String action, String previous, String next, Long userId) {
        AuditEntity audit = new AuditEntity(); audit.setTableName(table); audit.setRecordId(id);
        audit.setAction(action); audit.setPreviousValue(previous); audit.setNewValue(next);
        audit.setUserId(userId); audit.setOccurredAt(LocalDateTime.now()); audits.save(audit);
    }
    private static String productSummary(InventoryProduct p) {
        return "nome=" + p.getName() + ";custo=" + p.getUnitCost() + ";minimo=" + p.getMinimumStock() + ";ativo=" + p.getActive();
    }
    private static String equipmentSummary(Equipment e) {
        return "nome=" + e.getName() + ";aquisicao=" + e.getAcquisitionValue() + ";vidaUtil=" + e.getUsefulLifeUses() + ";ativo=" + e.getActive();
    }
}
