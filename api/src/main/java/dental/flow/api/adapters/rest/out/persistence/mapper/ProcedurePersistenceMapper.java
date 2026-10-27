package dental.flow.api.adapters.rest.out.persistence.mapper;

import dental.flow.api.adapters.rest.out.persistence.entity.ProcedureEntity;
import dental.flow.api.domain.procedure.Procedure;

public class ProcedurePersistenceMapper {
    public static Procedure toDomain(ProcedureEntity entity) {
        Procedure procedure = new Procedure(entity.getClinicId(), entity.getName(), entity.getPrice(), entity.getDurationMinutes());
        procedure.setId(entity.getId());
        return procedure;
    }

    public static ProcedureEntity toEntity(Procedure procedure) {
        ProcedureEntity entity = new ProcedureEntity(procedure.getClinicId(), procedure.getName(), procedure.getPrice(), procedure.getDurationMinutes());
        entity.setId(procedure.getId());
        return entity;
    }
}
