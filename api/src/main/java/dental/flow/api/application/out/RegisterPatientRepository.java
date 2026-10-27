package dental.flow.api.application.out;

import dental.flow.api.domain.patient.Patient;

import java.util.List;
import java.util.Optional;

public interface RegisterPatientRepository {
    String register(Patient patient);
    Boolean existsByClinicIdAndCpf(Long clinicId, String cpf);
    Optional<Patient> findById(Long id);
    List<Patient> findByClinicId(Long clinicId);
    Patient update(Patient patient);
}
