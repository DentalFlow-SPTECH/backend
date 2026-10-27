package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.AppointmentEntity;
import dental.flow.api.adapters.rest.out.persistence.mapper.AppointmentPersistenceMapper;
import dental.flow.api.application.out.RegisterAppointmentRepository;
import dental.flow.api.boot.exception.DatabaseException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.appointment.Appointment;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class RegisterAppointmentRepositoryImpl implements RegisterAppointmentRepository {
    private final AppointmentJpaRepository appointmentJpaRepository;

    public RegisterAppointmentRepositoryImpl(AppointmentJpaRepository appointmentJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
    }

    @Override
    public String register(Appointment appointment) {
        try {
            AppointmentEntity entity = AppointmentPersistenceMapper.toEntity(appointment);
            AppointmentEntity saved = appointmentJpaRepository.saveAndFlush(entity);
            return "Appointment id:" + saved.getId() + " registered successfully";
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error registering appointment: " + e.getMessage());
        }
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointmentJpaRepository.findById(id).map(AppointmentPersistenceMapper::toDomain);
    }

    @Override
    public List<Appointment> findByDentistIdAndDate(Long dentistId, LocalDate date) {
        return appointmentJpaRepository.findByDentist_IdAndScheduledAtBetween(
                dentistId,
                date.atStartOfDay(),
                date.atTime(LocalTime.MAX)
        ).stream().map(AppointmentPersistenceMapper::toDomain).toList();
    }

    @Override
    public Appointment update(Appointment appointment) {
        try {
            AppointmentEntity entity = AppointmentPersistenceMapper.toEntity(appointment);
            entity.setId(appointment.getId());
            return AppointmentPersistenceMapper.toDomain(appointmentJpaRepository.saveAndFlush(entity));
        } catch (DataAccessException e) {
            throw new DatabaseException(Failures.DATABASE_ERROR, "Error updating appointment: " + e.getMessage());
        }
    }
}
