package dental.flow.api.adapters.rest.in.controller.dto.request;

import java.math.BigDecimal;

public record SaveInventoryProductRequestDTO(String name, String unit, BigDecimal unitCost,
                                             BigDecimal initialStock, BigDecimal minimumStock, Boolean active) {
}
