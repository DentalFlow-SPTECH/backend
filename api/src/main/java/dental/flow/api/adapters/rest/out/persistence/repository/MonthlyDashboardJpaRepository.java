package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.MonthlyDashboardEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.MonthlyDashboardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MonthlyDashboardJpaRepository extends JpaRepository<MonthlyDashboardEntity, MonthlyDashboardId> {
    @Query("""
            select dashboard
            from MonthlyDashboardEntity dashboard
            join fetch dashboard.clinic
            join fetch dashboard.dentist
            where dashboard.year = :year
              and dashboard.month = :month
              and (:clinicId is null or dashboard.clinicId = :clinicId)
              and (:dentistId is null or dashboard.dentistId = :dentistId)
            order by dashboard.clinicId, dashboard.dentistId
            """)
    List<MonthlyDashboardEntity> findByPeriod(
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("clinicId") Long clinicId,
            @Param("dentistId") Long dentistId
    );

    @Query(value = """
            SELECT data_mapa AS mapDate,
                   COALESCE(SUM(bruto_geral), 0) AS generalGross,
                   COALESCE(SUM(total_despesas), 0) AS totalExpenses,
                   COALESCE(SUM(COALESCE(desconto_cartoes, 0)
                              + COALESCE(desconto_cheque_pre, 0)
                              + COALESCE(desconto_cheque_vista, 0)
                              + COALESCE(desconto_vales, 0)
                              + COALESCE(desconto_outros, 0)), 0) AS totalDiscounts,
                   COALESCE(SUM(total_liquido), 0) AS netTotal,
                   COALESCE(SUM(saldo_final), 0) AS finalBalance,
                   COALESCE(SUM(qtd_pacientes), 0) AS patientCount
              FROM mapa_diario
             WHERE YEAR(data_mapa) = :year
               AND MONTH(data_mapa) = :month
               AND id_clinica = :clinicId
               AND (:dentistId IS NULL OR id_dentista = :dentistId)
             GROUP BY data_mapa
             ORDER BY data_mapa
            """, nativeQuery = true)
    List<DailyDashboardProjection> findDailyEvolution(
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("clinicId") Long clinicId,
            @Param("dentistId") Long dentistId
    );

    @Query(value = """
            SELECT COUNT(*) AS totalAppointments,
                   COALESCE(SUM(CASE WHEN status = 'AGENDADO' THEN 1 ELSE 0 END), 0) AS scheduledAppointments,
                   COALESCE(SUM(CASE WHEN status = 'CONFIRMADO' THEN 1 ELSE 0 END), 0) AS confirmedAppointments,
                   COALESCE(SUM(CASE WHEN status = 'EM_ATENDIMENTO' THEN 1 ELSE 0 END), 0) AS inProgressAppointments,
                   COALESCE(SUM(CASE WHEN status = 'CONCLUIDO' THEN 1 ELSE 0 END), 0) AS completedAppointments,
                   COALESCE(SUM(CASE WHEN status = 'CANCELADO' THEN 1 ELSE 0 END), 0) AS cancelledAppointments,
                   COALESCE(SUM(CASE WHEN status = 'FALTOU' THEN 1 ELSE 0 END), 0) AS noShowAppointments
              FROM agendamento
             WHERE YEAR(data_hora) = :year
               AND MONTH(data_hora) = :month
               AND id_clinica = :clinicId
               AND (:dentistId IS NULL OR id_dentista = :dentistId)
            """, nativeQuery = true)
    AppointmentMetricsProjection findAppointmentMetrics(
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("clinicId") Long clinicId,
            @Param("dentistId") Long dentistId
    );
}
