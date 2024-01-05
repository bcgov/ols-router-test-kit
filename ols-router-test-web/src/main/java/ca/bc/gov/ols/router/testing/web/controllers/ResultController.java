package ca.bc.gov.ols.router.testing.web.controllers;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.ols.router.testing.engine.dao.ResultRepository;
import ca.bc.gov.ols.router.testing.engine.entity.Result;

@RestController
public class ResultController {

	private final ResultRepository resultRepository;

	public ResultController(ResultRepository resultRepository) {
		this.resultRepository = resultRepository;
	}

	@GetMapping("/results")
	public List<Result> findAllResults() {
		PageRequest page_req = PageRequest.of(0, 10, Direction.DESC, "resultId");
		Page<Result> page_res = resultRepository.findAll(page_req);
		List<Result> list = page_res.getContent();
		return list;
		//return this.resultRepository.findAll(26); not in teh pagingsortingrepo type I guess
		//return this.resultRepository.findAll();
	}
	
	@GetMapping("/test")
	public ResponseEntity<String> test() {
		
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
