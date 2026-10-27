package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.DentistEntity;
import dental.flow.api.adapters.rest.out.persistence.mapper.DentistPersistenceMapper;
import dental.flow.api.application.out.RegisterDentistRepository;
import dental.flow.api.boot.exception.DatabaseException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.dentist.Dentist;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RegisterDentistRepositoryImpl implements RegisterDentistRepository {
    private final DentistJpaRepository repository;

    public RegisterDentistRepositoryImpl(DentistJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public String register(Dentist dentist) {
        try {
            DentistEntity entity = DentistPersistenceMapper.toEntity(dentist);
            DentistEntity saved = repository.saveAndFlush(entity);
            return "Dentist id:" + saved.getId() + " registered successfully";
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error registering dentist: " + e.getMessage());
        }
    }

    @Override
    public Boolean existsByCro(String cro) {
        return repository.existsByCro(cro);
    }

    @Override
    public Optional<Dentist> findById(Long id) {
        return repository.findById(id).map(DentistPersistenceMapper::toDomain);
    }

    @Override
    public List<Dentist> findByClinicId(Long clinicId) {
        return repository.findByClinic_Id(clinicId).stream().map(DentistPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Dentist update(Dentist dentist) {
        try {
            DentistEntity entity = DentistPersistenceMapper.toEntity(dentist);
            return DentistPersistenceMapper.toDomain(repository.saveAndFlush(entity));
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error updating dentist: " + e.getMessage());
        }
    }
}
