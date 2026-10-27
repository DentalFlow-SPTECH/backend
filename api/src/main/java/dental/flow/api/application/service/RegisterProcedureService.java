package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.ProcedureResponseDTO;
import dental.flow.api.application.command.RegisterProcedureCommand;
import dental.flow.api.application.in.RegisterProcedureUseCase;
import dental.flow.api.application.out.RegisterProcedureRepository;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.boot.exception.NotFoundException;
import dental.flow.api.domain.procedure.Procedure;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterProcedureService implements RegisterProcedureUseCase {
    private final RegisterProcedureRepository repository;

    public RegisterProcedureService(RegisterProcedureRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProcedureResponseDTO register(RegisterProcedureCommand command) {
        Procedure procedure = new Procedure(command.clinicId(), command.name(), command.price(), command.durationMinutes());
        String message = repository.register(procedure);
        Long id = extractId(message);
        Procedure saved = repository.findById(id).orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Procedimento não encontrado após cadastro."));
        return ProcedureResponseDTO.from(saved);
    }

    @Override
    public ProcedureResponseDTO getById(Long id) {
        Procedure procedure = repository.findById(id).orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Procedimento não encontrado."));
        return ProcedureResponseDTO.from(procedure);
    }

    public List<ProcedureResponseDTO> listByClinic(Long clinicId) {
        return repository.findByClinicId(clinicId).stream().map(ProcedureResponseDTO::from).toList();
    }

    @Override
    public ProcedureResponseDTO update(Long id, RegisterProcedureCommand command) {
        Procedure existing = repository.findById(id).orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Procedimento não encontrado."));
        Procedure procedure = new Procedure(command.clinicId(), command.name(), command.price(), command.durationMinutes());
        procedure.setId(existing.getId());
        return ProcedureResponseDTO.from(repository.update(procedure));
    }

    private Long extractId(String message) {
        String digits = message.replaceAll("[^0-9]", "");
        return digits.isBlank() ? null : Long.valueOf(digits);
    }
}
