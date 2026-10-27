package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.ProcedureEntity;
import dental.flow.api.adapters.rest.out.persistence.mapper.ProcedurePersistenceMapper;
import dental.flow.api.application.out.RegisterProcedureRepository;
import dental.flow.api.boot.exception.DatabaseException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.procedure.Procedure;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RegisterProcedureRepositoryImpl implements RegisterProcedureRepository {
    private final ProcedureJpaRepository repository;

    public RegisterProcedureRepositoryImpl(ProcedureJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public String register(Procedure procedure) {
        try {
            ProcedureEntity entity = ProcedurePersistenceMapper.toEntity(procedure);
            ProcedureEntity saved = repository.saveAndFlush(entity);
            return "Procedure id:" + saved.getId() + " registered successfully";
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error registering procedure: " + e.getMessage());
        }
    }

    @Override
    public Optional<Procedure> findById(Long id) {
        return repository.findById(id).map(ProcedurePersistenceMapper::toDomain);
    }

    @Override
    public List<Procedure> findByClinicId(Long clinicId) {
        return repository.findByClinic_Id(clinicId).stream().map(ProcedurePersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Procedure update(Procedure procedure) {
        try {
            ProcedureEntity entity = ProcedurePersistenceMapper.toEntity(procedure);
            return ProcedurePersistenceMapper.toDomain(repository.saveAndFlush(entity));
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error updating procedure: " + e.getMessage());
        }
    }
}
