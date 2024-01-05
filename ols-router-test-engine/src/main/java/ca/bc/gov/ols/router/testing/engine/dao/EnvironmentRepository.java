package ca.bc.gov.ols.router.testing.engine.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import ca.bc.gov.ols.router.testing.engine.entity.Environment;
import jakarta.annotation.Resource;

@Resource
@Component
public interface EnvironmentRepository extends JpaRepository<Environment, Integer> {}
