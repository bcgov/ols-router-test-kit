package ca.bc.gov.ols.router.testing.engine.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import ca.bc.gov.ols.router.testing.engine.entity.RunList;
import jakarta.annotation.Resource;

@Resource
@Component
public interface RunListRepository extends ReadOnlyRepository<RunList, Long> {

	Page<RunList> findAll(Pageable pageable);
	Integer count();

}
