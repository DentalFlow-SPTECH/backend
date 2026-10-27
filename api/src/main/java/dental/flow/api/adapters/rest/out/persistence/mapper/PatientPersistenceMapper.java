package dental.flow.api.adapters.rest.out.persistence.mapper;

import dental.flow.api.adapters.rest.out.persistence.entity.PatientEntity;
import dental.flow.api.domain.patient.Patient;

public class PatientPersistenceMapper {

    public static Patient toDomain(PatientEntity entity) {
        Patient patient = new Patient(
                entity.getClinicId(),
                entity.getName(),
                entity.getCpf(),
                entity.getAddress(),
                entity.getNeighborhood(),
                entity.getZipCode(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getBirthDate()
        );
        patient.setId(entity.getId());
        return patient;
    }

    public static PatientEntity toEntity(Patient patient) {
        PatientEntity entity = new PatientEntity(
                patient.getClinicId(),
                patient.getName(),
                patient.getCpf(),
                patient.getAddress(),
                patient.getNeighborhood(),
                patient.getZipCode(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getBirthDate()
        );
        entity.setId(patient.getId());
        return entity;
    }
}
