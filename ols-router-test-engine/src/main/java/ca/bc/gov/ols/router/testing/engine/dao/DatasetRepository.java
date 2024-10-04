package ca.bc.gov.ols.router.testing.engine.dao;

import java.util.List;
import java.util.Optional;
import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import ca.bc.gov.ols.router.testing.engine.entity.Dataset;
import jakarta.annotation.Resource;

@Resource
@Component
public interface DatasetRepository extends JpaRepository<Dataset, Integer> {
	@Query("SELECT d FROM Dataset d ORDER BY d.roadSource ASC, d.roadNetworkTimestamp DESC")
    List<Dataset> findAllWithCustomSorting();

	Optional<Dataset> findByRoadNetworkTimestamp(ZonedDateTime roadNetworkTimestamp);
}
