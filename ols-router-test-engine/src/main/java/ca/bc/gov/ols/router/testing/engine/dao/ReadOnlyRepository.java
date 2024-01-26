package ca.bc.gov.ols.router.testing.engine.dao;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;


@NoRepositoryBean
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {
}
