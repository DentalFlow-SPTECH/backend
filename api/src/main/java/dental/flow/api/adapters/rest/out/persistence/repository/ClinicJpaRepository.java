package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.ClinicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicJpaRepository extends JpaRepository<ClinicEntity, Long> {
    Boolean existsByCnpj(String cnpj);
}
