package dental.flow.api.application.out;

import dental.flow.api.domain.clinic.Clinic;

import java.util.Optional;

public interface RegisterClinicRepository {
    String register(Clinic clinic);
    Boolean existsByCnpj(String cnpj);
    Optional<Clinic> findById(Long id);
    Clinic update(Clinic clinic);
}
