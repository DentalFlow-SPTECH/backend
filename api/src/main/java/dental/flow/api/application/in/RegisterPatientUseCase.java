package dental.flow.api.application.in;

import dental.flow.api.adapters.rest.in.controller.dto.response.PatientResponseDTO;
import dental.flow.api.application.command.RegisterPatientCommand;

public interface RegisterPatientUseCase {
    PatientResponseDTO register(RegisterPatientCommand command);
}
