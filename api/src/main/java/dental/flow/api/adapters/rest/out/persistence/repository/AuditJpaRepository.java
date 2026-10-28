package dental.flow.api.adapters.rest.out.persistence.repository;
import dental.flow.api.adapters.rest.out.persistence.entity.AuditEntity; import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditJpaRepository extends JpaRepository<AuditEntity,Long>{}
