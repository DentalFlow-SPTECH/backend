package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.AppointmentResponseDTO;
import dental.flow.api.application.command.RegisterAppointmentCommand;
import dental.flow.api.application.in.RegisterAppointmentUseCase;
import dental.flow.api.application.out.RegisterAppointmentRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.boot.exception.NotFoundException;
import dental.flow.api.domain.appointment.Appointment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegisterAppointmentService implements RegisterAppointmentUseCase {
    private final RegisterAppointmentRepository repository;

    public RegisterAppointmentService(RegisterAppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public AppointmentResponseDTO register(RegisterAppointmentCommand command) {
        Appointment appointment = new Appointment(
                command.clinicId(),
                command.patientId(),
                command.dentistId(),
                command.procedureId(),
                command.userId(),
                command.scheduledAt(),
                command.status(),
                command.notes()
        );

        String message = repository.register(appointment);
        Long id = extractId(message);
        Appointment saved = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Agendamento não encontrado após cadastro."));
        return AppointmentResponseDTO.from(saved);
    }

    @Override
    public AppointmentResponseDTO getById(Long id) {
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Agendamento não encontrado."));
        return AppointmentResponseDTO.from(appointment);
    }

    @Override
    public List<AppointmentResponseDTO> listByDateAndDentist(LocalDate date, Long clinicId, Long dentistId) {
        return repository.findByClinicIdAndDentistIdAndDate(clinicId, dentistId, date)
                .stream()
                .sorted((a, b) -> a.getScheduledAt().compareTo(b.getScheduledAt()))
                .map(AppointmentResponseDTO::from)
                .toList();
    }

    @Override
    public AppointmentResponseDTO update(Long id, RegisterAppointmentCommand command) {
        Appointment existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Agendamento não encontrado."));

        Appointment appointment = new Appointment(
                command.clinicId(),
                command.patientId(),
                command.dentistId(),
                command.procedureId(),
                command.userId(),
                command.scheduledAt(),
                command.status(),
                command.notes()
        );
        appointment.setId(existing.getId());

        if (existing.getDentistId().equals(appointment.getDentistId()) && existing.getScheduledAt().toLocalDate().equals(appointment.getScheduledAt().toLocalDate())) {
            // no-op for now; scheduled conflict validation remains service-layer controlled when repository is expanded
        }

        return AppointmentResponseDTO.from(repository.update(appointment));
    }

    @Override
    public AppointmentResponseDTO updateStatus(Long id, String status) {
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Failures.NOT_FOUND, "Agendamento não encontrado."));
        String current = appointment.getStatus();
        if ("CANCELADO".equals(current) || "FALTOU".equals(current)) {
            throw new BusinessException(Failures.BUSINESS_FAILURE, "Não é possível reabrir um agendamento cancelado ou faltoso.");
        }
        appointment.setStatus(status);
        return AppointmentResponseDTO.from(repository.update(appointment));
    }

    private Long extractId(String message) {
        String digits = message.replaceAll("[^0-9]", "");
        return digits.isBlank() ? null : Long.valueOf(digits);
    }
}
