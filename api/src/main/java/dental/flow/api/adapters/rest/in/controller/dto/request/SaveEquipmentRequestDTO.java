package dental.flow.api.adapters.rest.in.controller.dto.request;

import java.math.BigDecimal;

public record SaveEquipmentRequestDTO(String name, String description, BigDecimal acquisitionValue,
                                      Integer usefulLifeUses, Integer completedUses, Boolean active) {
}
