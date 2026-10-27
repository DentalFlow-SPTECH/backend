package dental.flow.api.adapters.rest.out.persistence.mapper;

import dental.flow.api.adapters.rest.out.persistence.entity.AppointmentEntity;
import dental.flow.api.domain.appointment.Appointment;

public class AppointmentPersistenceMapper {
    public static Appointment toDomain(AppointmentEntity entity) {
        Appointment appointment = new Appointment(
                entity.getClinicId(),
                entity.getPatientId(),
                entity.getDentistId(),
                entity.getProcedureId(),
                entity.getUserId(),
                entity.getScheduledAt(),
                entity.getStatus(),
                entity.getNotes()
        );
        appointment.setId(entity.getId());
        return appointment;
    }

    public static AppointmentEntity toEntity(Appointment appointment) {
        AppointmentEntity entity = new AppointmentEntity(
                appointment.getClinicId(),
                appointment.getPatientId(),
                appointment.getDentistId(),
                appointment.getProcedureId(),
                appointment.getUserId(),
                appointment.getScheduledAt(),
                appointment.getStatus(),
                appointment.getNotes()
        );
        entity.setId(appointment.getId());
        return entity;
    }
}
