package dental.flow.api.application.out;

import dental.flow.api.domain.inventory.Equipment;
import dental.flow.api.domain.inventory.InventoryProduct;
import dental.flow.api.domain.inventory.StockMovement;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {
    InventoryProduct saveProduct(InventoryProduct product, Long userId, String action);

    Optional<InventoryProduct> findProduct(Long id, Long clinicId);

    List<InventoryProduct> listProducts(Long clinicId, Boolean active);

    boolean productNameExists(Long clinicId, String name, Long ignoredId);

    StockMovement moveStock(Long productId, Long clinicId, Long userId, String type,
                            java.math.BigDecimal quantity, String reason);

    List<StockMovement> listMovements(Long productId, Long clinicId);

    Equipment saveEquipment(Equipment equipment, Long userId, String action);

    Optional<Equipment> findEquipment(Long id, Long clinicId);

    List<Equipment> listEquipment(Long clinicId, Boolean active);

    boolean equipmentNameExists(Long clinicId, String name, Long ignoredId);
}
