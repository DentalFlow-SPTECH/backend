package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.DentistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DentistJpaRepository extends JpaRepository<DentistEntity, Long> {
    Boolean existsByCro(String cro);

    List<DentistEntity> findByClinic_Id(Long clinicId);
}
