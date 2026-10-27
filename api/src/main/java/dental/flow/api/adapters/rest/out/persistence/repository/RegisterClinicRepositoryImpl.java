package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.ClinicEntity;
import dental.flow.api.adapters.rest.out.persistence.mapper.ClinicPersistenceMapper;
import dental.flow.api.application.out.RegisterClinicRepository;
import dental.flow.api.boot.exception.DatabaseException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.clinic.Clinic;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RegisterClinicRepositoryImpl implements RegisterClinicRepository {

    private final ClinicJpaRepository clinicJpaRepository;

    public RegisterClinicRepositoryImpl(ClinicJpaRepository clinicJpaRepository) {
        this.clinicJpaRepository = clinicJpaRepository;
    }

    @Override
    public String register(Clinic clinic) {
        try {
            ClinicEntity entity = ClinicPersistenceMapper.toEntity(clinic);
            ClinicEntity saved = clinicJpaRepository.saveAndFlush(entity);
            return "Clinic id:" + saved.getId() + " registered successfully";
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error registering clinic: " + e.getMessage());
        }
    }

    @Override
    public Boolean existsByCnpj(String cnpj) {
        return clinicJpaRepository.existsByCnpj(cnpj);
    }

    @Override
    public Optional<Clinic> findById(Long id) {
        return clinicJpaRepository.findById(id).map(ClinicPersistenceMapper::toDomain);
    }

    @Override
    public Clinic update(Clinic clinic) {
        try {
            ClinicEntity entity = ClinicPersistenceMapper.toEntity(clinic);
            return ClinicPersistenceMapper.toDomain(clinicJpaRepository.saveAndFlush(entity));
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error updating clinic: " + e.getMessage());
        }
    }
}
