package dental.flow.api.adapters.rest.out.persistence.mapper;

import dental.flow.api.adapters.rest.out.persistence.entity.MonthlyDashboardEntity;
import dental.flow.api.domain.dashboard.MonthlyDashboard;

import java.math.BigDecimal;

public final class MonthlyDashboardPersistenceMapper {
    private MonthlyDashboardPersistenceMapper() {
    }

    public static MonthlyDashboard toDomain(MonthlyDashboardEntity entity) {
        return new MonthlyDashboard(
                entity.getClinicId(),
                entity.getClinic().getName(),
                entity.getDentistId(),
                entity.getDentist().getName(),
                entity.getYear(),
                entity.getMonth(),
                value(entity.getRegisteredDays()),
                value(entity.getClinicalTotal()),
                value(entity.getProsthesisTotal()),
                value(entity.getClinicalGross()),
                value(entity.getPediatricSurgeryGross()),
                value(entity.getGeneralGross()),
                value(entity.getTotalExpenses()),
                value(entity.getCardDiscount()),
                value(entity.getPredatedCheckDiscount()),
                value(entity.getCashCheckDiscount()),
                value(entity.getVoucherDiscount()),
                value(entity.getOtherDiscount()),
                value(entity.getNetTotal()),
                value(entity.getFinalBalance()),
                value(entity.getPatientCount()),
                value(entity.getMoldingCount()),
                value(entity.getBiteCount()),
                value(entity.getFittingCount()),
                value(entity.getDeliveryCount()),
                value(entity.getRepetitionCount())
        );
    }

    private static BigDecimal value(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private static Long value(Long value) {
        return value == null ? 0L : value;
    }
}
