package dental.flow.api.application.out;

import dental.flow.api.domain.appointment.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegisterAppointmentRepository {
    String register(Appointment appointment);
    Optional<Appointment> findById(Long id);
    List<Appointment> findByDentistIdAndDate(Long dentistId, LocalDate date);
    Appointment update(Appointment appointment);
}
