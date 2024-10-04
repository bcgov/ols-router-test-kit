package ca.bc.gov.ols.router.testing.engine.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import ca.bc.gov.ols.router.testing.engine.entity.CodeVersion;
import jakarta.annotation.Resource;

@Resource
@Component
public interface CodeVersionRepository extends JpaRepository<CodeVersion, Integer> {
	List<CodeVersion> findAll(Sort sort);
	
	@Query("SELECT c FROM CodeVersion c ORDER BY c.versionNum DESC")
    List<CodeVersion> findAllWithCustomSorting();
	
	
	Optional<CodeVersion> findByVersionNum(String versionNum);
	
	Optional<CodeVersion> findByVersionNumAndGithubCommitId(String versionNum, String githubCommitId);
}
