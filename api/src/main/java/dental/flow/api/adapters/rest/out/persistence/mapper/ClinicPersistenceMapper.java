package dental.flow.api.adapters.rest.out.persistence.mapper;

import dental.flow.api.adapters.rest.out.persistence.entity.ClinicEntity;
import dental.flow.api.domain.clinic.Clinic;

public class ClinicPersistenceMapper {

    public static Clinic toDomain(ClinicEntity entity) {
        Clinic clinic = new Clinic(entity.getName(), entity.getCnpj(), entity.getAddress(), entity.getPhone());
        clinic.setId(entity.getId());
        return clinic;
    }

    public static ClinicEntity toEntity(Clinic clinic) {
        ClinicEntity entity = new ClinicEntity(clinic.getName(), clinic.getCnpj(), clinic.getAddress(), clinic.getPhone());
        entity.setId(clinic.getId());
        return entity;
    }
}
