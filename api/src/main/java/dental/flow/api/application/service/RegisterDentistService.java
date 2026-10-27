package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.DentistResponseDTO;
import dental.flow.api.application.command.RegisterDentistCommand;
import dental.flow.api.application.in.RegisterDentistUseCase;
import dental.flow.api.application.out.RegisterDentistRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.boot.exception.NotFoundException;
import dental.flow.api.domain.dentist.Dentist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterDentistService implements RegisterDentistUseCase {
    private final RegisterDentistRepository repository;

    public RegisterDentistService(RegisterDentistRepository repository) {
        this.repository = repository;
    }

    @Override
    public DentistResponseDTO register(RegisterDentistCommand command) {
        Dentist dentist = new Dentist(command.clinicId(), command.name(), command.cro(), command.specialty(), command.phone());
        if (repository.existsByCro(dentist.getCro())) {
            throw new BusinessException(Failures.BUSINESS_FAILURE, "Já existe um dentista cadastrado com este CRO.");
        }

        String message = repository.register(dentist);
        Long id = extractId(message);
        Dentist saved = repository.findById(id).orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Dentista não encontrado após cadastro."));
        return DentistResponseDTO.from(saved);
    }

    @Override
    public DentistResponseDTO getById(Long id) {
        Dentist dentist = repository.findById(id).orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Dentista não encontrado."));
        return DentistResponseDTO.from(dentist);
    }

    public List<DentistResponseDTO> listByClinic(Long clinicId) {
        return repository.findByClinicId(clinicId).stream().map(DentistResponseDTO::from).toList();
    }

    @Override
    public DentistResponseDTO update(Long id, RegisterDentistCommand command) {
        Dentist existing = repository.findById(id).orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Dentista não encontrado."));
        Dentist dentist = new Dentist(command.clinicId(), command.name(), command.cro(), command.specialty(), command.phone());
        dentist.setId(existing.getId());

        if (!existing.getCro().equalsIgnoreCase(dentist.getCro()) && repository.existsByCro(dentist.getCro())) {
            throw new BusinessException(Failures.BUSINESS_FAILURE, "Já existe um dentista cadastrado com este CRO.");
        }

        return DentistResponseDTO.from(repository.update(dentist));
    }

    private Long extractId(String message) {
        String digits = message.replaceAll("[^0-9]", "");
        return digits.isBlank() ? null : Long.valueOf(digits);
    }
}
