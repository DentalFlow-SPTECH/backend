package dental.flow.api.application.out;

import dental.flow.api.domain.dentist.Dentist;

import java.util.List;
import java.util.Optional;

public interface RegisterDentistRepository {
    String register(Dentist dentist);
    Boolean existsByCro(String cro);
    Optional<Dentist> findById(Long id);
    List<Dentist> findByClinicId(Long clinicId);
    Dentist update(Dentist dentist);
}
