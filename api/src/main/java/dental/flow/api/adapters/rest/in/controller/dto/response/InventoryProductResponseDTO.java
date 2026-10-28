package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.inventory.InventoryProduct;

import java.math.BigDecimal;

public record InventoryProductResponseDTO(Long id, Long clinicId, String name, String unit,
                                          BigDecimal unitCost, BigDecimal currentStock,
                                          BigDecimal minimumStock, Boolean lowStock, Boolean active) {
    public static InventoryProductResponseDTO from(InventoryProduct product) {
        return new InventoryProductResponseDTO(product.getId(), product.getClinicId(), product.getName(),
                product.getUnit(), product.getUnitCost(), product.getCurrentStock(), product.getMinimumStock(),
                product.lowStock(), product.getActive());
    }
}
