package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.PatientResponseDTO;
import dental.flow.api.application.command.RegisterPatientCommand;
import dental.flow.api.application.in.RegisterPatientUseCase;
import dental.flow.api.application.out.RegisterPatientRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.boot.exception.NotFoundException;
import dental.flow.api.domain.patient.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterPatientService implements RegisterPatientUseCase {

    private final RegisterPatientRepository registerPatientRepository;

    public RegisterPatientService(RegisterPatientRepository registerPatientRepository) {
        this.registerPatientRepository = registerPatientRepository;
    }

    @Override
    public PatientResponseDTO register(RegisterPatientCommand command) {
        Patient patient = new Patient(
                command.clinicId(),
                command.name(),
                command.cpf(),
                command.address(),
                command.neighborhood(),
                command.zipCode(),
                command.phone(),
                command.email(),
                command.birthDate()
        );

        if (registerPatientRepository.existsByClinicIdAndCpf(patient.getClinicId(), patient.getCpf())) {
            throw new BusinessException(Failures.BUSINESS_FAILURE, "Já existe um paciente cadastrado com este CPF na mesma clínica.");
        }

        String registrationMessage = registerPatientRepository.register(patient);
        Patient saved = registerPatientRepository.findById(Long.valueOf(registrationMessage.replaceAll("[^0-9]", "")))
                .orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Paciente não encontrado após cadastro."));

        return PatientResponseDTO.from(saved);
    }

    public PatientResponseDTO getById(Long id) {
        Patient patient = registerPatientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Paciente não encontrado."));
        return PatientResponseDTO.from(patient);
    }

    public List<PatientResponseDTO> listByClinicAndName(Long clinicId, String name) {
        return registerPatientRepository.findByClinicId(clinicId)
                .stream()
                .filter(patient -> name == null || name.isBlank() || patient.getName().toLowerCase().contains(name.toLowerCase()))
                .map(PatientResponseDTO::from)
                .toList();
    }

    public PatientResponseDTO update(Long id, RegisterPatientCommand command) {
        Patient existing = registerPatientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Paciente não encontrado."));

        Patient updated = new Patient(
                command.clinicId(),
                command.name(),
                command.cpf(),
                command.address(),
                command.neighborhood(),
                command.zipCode(),
                command.phone(),
                command.email(),
                command.birthDate()
        );
        updated.setId(existing.getId());

        if (!existing.getCpf().equals(updated.getCpf()) || !existing.getClinicId().equals(updated.getClinicId())) {
            if (registerPatientRepository.existsByClinicIdAndCpf(updated.getClinicId(), updated.getCpf())) {
                throw new BusinessException(Failures.BUSINESS_FAILURE, "Já existe um paciente cadastrado com este CPF na mesma clínica.");
            }
        }

        return PatientResponseDTO.from(registerPatientRepository.update(updated));
    }
}
