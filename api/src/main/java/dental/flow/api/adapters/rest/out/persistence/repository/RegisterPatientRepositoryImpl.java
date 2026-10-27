package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.PatientEntity;
import dental.flow.api.adapters.rest.out.persistence.mapper.PatientPersistenceMapper;
import dental.flow.api.application.out.RegisterPatientRepository;
import dental.flow.api.boot.exception.DatabaseException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.patient.Patient;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RegisterPatientRepositoryImpl implements RegisterPatientRepository {

    private final PatientJpaRepository patientJpaRepository;

    public RegisterPatientRepositoryImpl(PatientJpaRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    @Override
    public String register(Patient patient) {
        try {
            PatientEntity entity = PatientPersistenceMapper.toEntity(patient);
            PatientEntity saved = patientJpaRepository.saveAndFlush(entity);
            return "Patient id:" + saved.getId() + " registered successfully";
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error registering patient: " + e.getMessage());
        }
    }

    @Override
    public Boolean existsByClinicIdAndCpf(Long clinicId, String cpf) {
        return patientJpaRepository.existsByClinic_IdAndCpf(clinicId, cpf);
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientJpaRepository.findById(id).map(PatientPersistenceMapper::toDomain);
    }

    @Override
    public List<Patient> findByClinicId(Long clinicId) {
        return patientJpaRepository.findByClinic_Id(clinicId)
                .stream()
                .map(PatientPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Patient update(Patient patient) {
        try {
            PatientEntity entity = PatientPersistenceMapper.toEntity(patient);
            entity.setId(patient.getId());
            return PatientPersistenceMapper.toDomain(patientJpaRepository.saveAndFlush(entity));
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error updating patient: " + e.getMessage());
        }
    }
}
