package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.ProcedureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcedureJpaRepository extends JpaRepository<ProcedureEntity, Long> {
    List<ProcedureEntity> findByClinic_Id(Long clinicId);
}
