package ca.bc.gov.ols.router.testing.engine.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import ca.bc.gov.ols.router.testing.engine.entity.Run;
import jakarta.annotation.Resource;

@Resource
@Component
public interface RunRepository extends JpaRepository<Run, Integer> {

	List<Run> findByStatusOrderByQueuedDateTime(String string);
}
