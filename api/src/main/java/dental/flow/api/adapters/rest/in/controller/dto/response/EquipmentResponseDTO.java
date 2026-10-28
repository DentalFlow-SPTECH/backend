package dental.flow.api.adapters.rest.in.controller.dto.response;

import dental.flow.api.domain.inventory.Equipment;

import java.math.BigDecimal;

public record EquipmentResponseDTO(Long id, Long clinicId, String name, String description,
                                   BigDecimal acquisitionValue, Integer usefulLifeUses,
                                   Integer completedUses, BigDecimal costPerUse, Boolean active) {
    public static EquipmentResponseDTO from(Equipment equipment) {
        return new EquipmentResponseDTO(equipment.getId(), equipment.getClinicId(), equipment.getName(),
                equipment.getDescription(), equipment.getAcquisitionValue(), equipment.getUsefulLifeUses(),
                equipment.getCompletedUses(), equipment.getCostPerUse(), equipment.getActive());
    }
}
