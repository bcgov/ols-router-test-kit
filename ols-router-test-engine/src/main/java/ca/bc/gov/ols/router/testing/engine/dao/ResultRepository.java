package ca.bc.gov.ols.router.testing.engine.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import ca.bc.gov.ols.router.testing.engine.entity.Result;
import jakarta.annotation.Resource;

@Resource
@Component
public interface ResultRepository extends JpaRepository <Result, Integer> {}
