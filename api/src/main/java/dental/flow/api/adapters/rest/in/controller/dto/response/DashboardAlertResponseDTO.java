package dental.flow.api.adapters.rest.in.controller.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record DashboardAlertResponseDTO(
        String code,
        String severity,
        String title,
        String message,
        BigDecimal value,
        BigDecimal threshold
) {
    public static List<DashboardAlertResponseDTO> evaluate(MonthlyDashboardSummaryResponseDTO summary,
                                                           DashboardKpiResponseDTO kpis) {
        List<DashboardAlertResponseDTO> alerts = new ArrayList<>();
        if (kpis.operatingDays() == 0) {
            alerts.add(new DashboardAlertResponseDTO("NO_FINANCIAL_DATA", "INFO", "Sem mapa diário",
                    "Nenhum mapa clínico diário foi registrado no período.", BigDecimal.ZERO, null));
            return alerts;
        }
        addAbove(alerts, "HIGH_EXPENSE_RATE", "CRITICAL", "Despesas elevadas",
                "As despesas consumiram mais de 30% da receita bruta.", kpis.expenseRate(), "30.00");
        addAbove(alerts, "HIGH_DISCOUNT_RATE", "WARNING", "Descontos elevados",
                "Os descontos consumiram mais de 15% da receita bruta.", kpis.discountRate(), "15.00");
        addAbove(alerts, "HIGH_NO_SHOW_RATE", "WARNING", "Muitas faltas",
                "Mais de 10% dos agendamentos terminaram em falta.", kpis.noShowRate(), "10.00");
        addAbove(alerts, "HIGH_REPETITION_RATE", "WARNING", "Repetições acima do esperado",
                "Mais de 5% dos pacientes geraram repetição, indicando possível retrabalho.", kpis.repetitionRate(), "5.00");
        if (summary.finalBalance().signum() < 0) {
            alerts.add(new DashboardAlertResponseDTO("NEGATIVE_BALANCE", "CRITICAL", "Saldo negativo",
                    "O fechamento do período terminou com saldo negativo.", summary.finalBalance(), BigDecimal.ZERO));
        }
        return List.copyOf(alerts);
    }

    private static void addAbove(List<DashboardAlertResponseDTO> alerts, String code, String severity,
                                 String title, String message, BigDecimal value, String thresholdValue) {
        BigDecimal threshold = new BigDecimal(thresholdValue);
        if (value.compareTo(threshold) > 0) {
            alerts.add(new DashboardAlertResponseDTO(code, severity, title, message, value, threshold));
        }
    }
}
