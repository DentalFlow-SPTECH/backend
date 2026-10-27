package dental.flow.api.adapters.rest.out.persistence.mapper;

import dental.flow.api.adapters.rest.out.persistence.entity.DentistEntity;
import dental.flow.api.domain.dentist.Dentist;

public class DentistPersistenceMapper {
    public static Dentist toDomain(DentistEntity entity) {
        Dentist dentist = new Dentist(entity.getClinicId(), entity.getName(), entity.getCro(), entity.getSpecialty(), entity.getPhone());
        dentist.setId(entity.getId());
        return dentist;
    }

    public static DentistEntity toEntity(Dentist dentist) {
        DentistEntity entity = new DentistEntity(dentist.getClinicId(), dentist.getName(), dentist.getCro(), dentist.getSpecialty(), dentist.getPhone());
        entity.setId(dentist.getId());
        return entity;
    }
}
