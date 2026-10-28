package dental.flow.api.adapters.rest.in.controller.dto.response;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class DashboardMath {
    private DashboardMath() {
    }

    static BigDecimal percentage(BigDecimal numerator, BigDecimal denominator) {
        if (denominator == null || denominator.signum() == 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return numerator.multiply(BigDecimal.valueOf(100)).divide(denominator, 2, RoundingMode.HALF_UP);
    }

    static BigDecimal percentage(Long numerator, Long denominator) {
        return percentage(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }

    static BigDecimal divide(BigDecimal numerator, long denominator) {
        if (denominator == 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return numerator.divide(BigDecimal.valueOf(denominator), 2, RoundingMode.HALF_UP);
    }
}
