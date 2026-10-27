package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.ClinicResponseDTO;
import dental.flow.api.application.command.RegisterClinicCommand;
import dental.flow.api.application.in.RegisterClinicUseCase;
import dental.flow.api.application.out.RegisterClinicRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.boot.exception.NotFoundException;
import dental.flow.api.domain.clinic.Clinic;
import org.springframework.stereotype.Service;

@Service
public class RegisterClinicService implements RegisterClinicUseCase {

    private final RegisterClinicRepository registerClinicRepository;

    public RegisterClinicService(RegisterClinicRepository registerClinicRepository) {
        this.registerClinicRepository = registerClinicRepository;
    }

    @Override
    public ClinicResponseDTO register(RegisterClinicCommand command) {
        Clinic clinic = new Clinic(command.name(), command.cnpj(), command.address(), command.phone());
        if (clinic.getCnpj() != null && registerClinicRepository.existsByCnpj(clinic.getCnpj())) {
            throw new BusinessException(Failures.BUSINESS_FAILURE, "Já existe uma clínica cadastrada com este CNPJ.");
        }

        String message = registerClinicRepository.register(clinic);
        Long id = extractId(message);
        Clinic saved = registerClinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Clínica não encontrada após cadastro."));
        return ClinicResponseDTO.from(saved);
    }

    @Override
    public ClinicResponseDTO getById(Long id) {
        Clinic clinic = registerClinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Clínica não encontrada."));
        return ClinicResponseDTO.from(clinic);
    }

    @Override
    public ClinicResponseDTO update(Long id, RegisterClinicCommand command) {
        Clinic existing = registerClinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Clínica não encontrada."));

        Clinic clinic = new Clinic(command.name(), command.cnpj(), command.address(), command.phone());
        clinic.setId(existing.getId());

        if (clinic.getCnpj() != null && !clinic.getCnpj().equalsIgnoreCase(existing.getCnpj())
                && registerClinicRepository.existsByCnpj(clinic.getCnpj())) {
            throw new BusinessException(Failures.BUSINESS_FAILURE, "Já existe uma clínica cadastrada com este CNPJ.");
        }

        return ClinicResponseDTO.from(registerClinicRepository.update(clinic));
    }

    private Long extractId(String message) {
        String digits = message.replaceAll("[^0-9]", "");
        return digits.isBlank() ? null : Long.valueOf(digits);
    }
}
