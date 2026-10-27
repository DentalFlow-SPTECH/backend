package dental.flow.api.application.out;

import dental.flow.api.domain.procedure.Procedure;

import java.util.List;
import java.util.Optional;

public interface RegisterProcedureRepository {
    String register(Procedure procedure);
    Optional<Procedure> findById(Long id);
    List<Procedure> findByClinicId(Long clinicId);
    Procedure update(Procedure procedure);
}
