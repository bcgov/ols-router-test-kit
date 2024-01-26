package ca.bc.gov.ols.router.testing.web.controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ca.bc.gov.ols.router.testing.engine.dao.DatasetRepository;
import ca.bc.gov.ols.router.testing.engine.dao.EnvironmentRepository;
import ca.bc.gov.ols.router.testing.engine.dao.ResultRepository;
import ca.bc.gov.ols.router.testing.engine.dao.RunListRepository;
import ca.bc.gov.ols.router.testing.engine.dao.RunRepository;
import ca.bc.gov.ols.router.testing.engine.dao.TestRepository;
import ca.bc.gov.ols.router.testing.engine.entity.ComparedResult;
import ca.bc.gov.ols.router.testing.engine.entity.Dataset;
import ca.bc.gov.ols.router.testing.engine.entity.Environment;
import ca.bc.gov.ols.router.testing.engine.entity.Result;
import ca.bc.gov.ols.router.testing.engine.entity.Run;
import ca.bc.gov.ols.router.testing.engine.entity.RunList;
import ca.bc.gov.ols.router.testing.engine.entity.Test;
import ca.bc.gov.ols.router.testing.engine.entity.View;
import ca.bc.gov.ols.router.testing.web.exceptions.InvalidParameterException;


//@CrossOrigin(maxAge = 3600)
@RestController
@CrossOrigin
public class ApiController {
	
	private final ResultRepository resultRepository;
	private final TestRepository testRepository;
	private final RunListRepository runListRepository;
	private final RunRepository runRepository;
	private final EnvironmentRepository environmentRepository;
	private final DatasetRepository datasetRepository;

	public ApiController(ResultRepository resultRepository, TestRepository testRepository, RunListRepository runListRepository, RunRepository runRepository, EnvironmentRepository environmentRepository,DatasetRepository datasetRepository) {
		this.resultRepository = resultRepository;
		this.testRepository = testRepository;
		this.runListRepository = runListRepository;
		this.runRepository = runRepository;
		this.environmentRepository = environmentRepository;
		this.datasetRepository=  datasetRepository;
		
		
	}

	/* Returns a paged, sorted list of Result entities from the DB in Json format
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. resultId 
	 * @param <optional> filterColumn - the name of the column to filter: runId or testId is supported so far 
	 * @param <optional> filterValue - the integer value to filter on
	*/
	
	@RequestMapping("/results")
	@JsonView(View.Default.class)
	//above JsonView line means we don't send the geom, it's big and not necessary for this call, remove that line and all fields are sent.
	//in the entity(Result) Class itself you can see the @JsonView things that define which fields are in which view
	public List<Result>getSortedPagedResults(@RequestParam int pageNumber, @RequestParam int perPage, @RequestParam String sortBy, @RequestParam Optional<Boolean> descending, @RequestParam Optional<String> filterColumn, @RequestParam Optional<Integer> filterValue) {
		Direction order;
		
		if (descending.isPresent() && descending.get()==true) {
			order = Direction.DESC;
		}else {
			order = Direction.ASC;
		}
		try {		
			PageRequest pageReq = PageRequest.of(pageNumber, perPage, order, sortBy);
			Page<Result> pageRes = null;
			if(!filterColumn.isEmpty() && "runId".equals(filterColumn.get())) {
				pageRes = resultRepository.findByRunIdIs(filterValue.get(), pageReq);
			}else if(!filterColumn.isEmpty() && "testId".equals(filterColumn.get())) {
				pageRes = resultRepository.findByTestIdIs(filterValue.get(), pageReq);
			}else {
				pageRes = resultRepository.findAll(pageReq);
			}
			List<Result> list = pageRes.getContent();
			return list;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
		
	}

	/*
	 * Returns a count of all bulk tests
	 * Useful for calculating "last page" in pagination UI 
	 * @param Optional<String> filterColumn - the column to filter on runId or testID is supported
	 * @param Optional<Integer> filterValue - Int value to filter on
	 */
	@RequestMapping("/resultsCount")
	public long getResultsCount(@RequestParam Optional<String> filterColumn, @RequestParam Optional<Integer> filterValue) {
		try {
			if(!filterColumn.isEmpty() && "runId".equals(filterColumn.get())) {
				return resultRepository.countByRunIdIs(filterValue.get());
			}else if(!filterColumn.isEmpty() && "testId".equals(filterColumn.get())) {
				return resultRepository.countByTestIdIs(filterValue.get());
			}else{
				return resultRepository.count() ;
			}
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	

	/*
	 * Returns a paged, sorted list of Bulk Test entities (group name != "custom" means they are bulk type) from the DB in Json format
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. testId 

	 */
	@RequestMapping("/bulkTests")
	public List<Test> getSortedPagedTests(@RequestParam int pageNumber, @RequestParam int perPage, @RequestParam String sortBy, @RequestParam Optional<Boolean> descending) {
		Direction order;

		if (descending.isPresent() && descending.get()==true) {
			order = Direction.DESC;
		}else {
			order = Direction.ASC;
		}
		try {
			PageRequest pageReq = PageRequest.of(pageNumber, perPage, order, sortBy);
			Page<Test> pageRes;
			pageRes = testRepository.findAllByGroupNameIsNot("Custom",pageReq);
			List<Test> list = pageRes.getContent();
			return list;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}

	/*
	 * Returns a count of all bulk tests
	 * Useful for calculating "last page" in pagination UI 
	 */
	@RequestMapping("/bulkTestsCount")
	public int getBulkTestCount() {
		try {
			return testRepository.countByGroupNameIsNot("Custom") ;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	

	/*
	 * Returns a paged, sorted list of Bulk Test entities (group name != "custom" means they are bulk type) from the DB in Json format
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. testId 

	 */
	@RequestMapping("/customTests")
	public List<Test> getSortedPagedCustomTests(@RequestParam int pageNumber, @RequestParam int perPage, @RequestParam String sortBy, @RequestParam Optional<Boolean> descending) {
		Direction order;

		if (descending.isPresent() && descending.get()==true) {
			order = Direction.DESC;
		}else {
			order = Direction.ASC;
		}
		try {
			PageRequest pageReq = PageRequest.of(pageNumber, perPage, order, sortBy);
			Page<Test> pageRes;
			pageRes = testRepository.findAllByGroupName("Custom",pageReq);
			List<Test> list = pageRes.getContent();
			return list;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}

	/*
	 * Returns a count of all bulk tests
	 * Useful for calculating "last page" in pagination UI 
	 */
	@RequestMapping("/customTestsCount")
	public int getCustomTestCount() {
		try {
			return testRepository.countByGroupName("Custom") ;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/*
	 * Returns a single test object
	 * @parameter testId - the test id that you want  
	 */
	@RequestMapping("/test")
	public Optional<Test> getCustomTestCount(@RequestParam int testId) {
		try {
			return testRepository.findById(testId) ;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/*
	 * Returns a paged, sorted list of Run entities from the DB in Json format
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. runId 
	 */
	@RequestMapping("/runs")
	public List<RunList> getSortedPagedRuns(@RequestParam int pageNumber, @RequestParam int perPage, @RequestParam String sortBy, @RequestParam Optional<Boolean> descending) {
		Direction order;
		
		if (descending.isPresent() && descending.get()==true) {
			order = Direction.DESC;
		}else {
			order = Direction.ASC;
		}
		try {
			PageRequest pageReq = PageRequest.of(pageNumber, perPage, order, sortBy);
			Page<RunList> pageRes = runListRepository.findAll(pageReq);
			List<RunList> list = pageRes.getContent();
			return list;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/*
	 * Returns a count of all runs
	 * Useful for calculating "last page" in pagination UI 
	 */
	@RequestMapping("/runsCount")
	public int getRunsCount() {
		try {
			return runListRepository.count() ;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/*
	 * Returns a single runs
	 * @param runId - the id for the one you want  
	 */
	@RequestMapping("/run")
	public Run getRunsCount(@RequestParam int runId) {
		try {
			return runRepository.findById(runId).get();
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/*
	 * Returns a single runs
	 * @param runId - the id for the one you want  
	 */
	@RequestMapping("/environment")
	public Environment getEnvironment(@RequestParam int envId) {
		try {
			return environmentRepository.findById(envId).get();
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	
	
	/*
	 * Returns a single dataset
	 * @param datasetId - the id for the one you want  
	 */
	@RequestMapping("/dataset")
	public Dataset getDataset(@RequestParam int datasetId) {
		try {
			return datasetRepository.findById(datasetId).get();
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	
	/*
	 * Returns a comparison of result sets for 2 given run IDs 
	 * @param runIdA - the id for the first run  
	 * @param runIdB - the id for the second run
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. resultId
	 */
	@RequestMapping("/compareRuns")
	public List<Map> getComparedRuns(@RequestParam int runIdA, @RequestParam int runIdB,@RequestParam int pageNumber, @RequestParam int perPage, @RequestParam String sortBy, @RequestParam Optional<Boolean> descending) {
		Direction order;
		
		if (descending.isPresent() && descending.get()==true) {
			order = Direction.DESC;
		}else {
			order = Direction.ASC;
		}
		try {
			PageRequest pageReq = PageRequest.of(pageNumber, perPage, order, sortBy);
			List<Map> foo = resultRepository.compareResultsOfRunIds(runIdA, runIdB, pageReq);
			
			return foo;

		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/*
	 * Returns the count of rows for the comparison of 2 given run IDs 
	 * @param runIdA - the id for the first run  
	 * @param runIdB - the id for the second run
	 */
	@RequestMapping("/compareRunsCount")
	public Integer getComparedRuns(@RequestParam int runIdA, @RequestParam int runIdB) {
		try {
			return resultRepository.compareResultsOfRunIdsCount(runIdA, runIdB);

		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	

	/*
	 * Returns a comparison of result sets for 2 given run IDs 
	 * @param runIdA - the run id to compare against reference tests  
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. resultId
	 */
	@RequestMapping("/compareRunVsRef")
	public List<Map> getCompareRunVsRef(@RequestParam int runId, @RequestParam int pageNumber, @RequestParam int perPage, @RequestParam String sortBy, @RequestParam Optional<Boolean> descending) {
		Direction order;
		
		if (descending.isPresent() && descending.get()==true) {
			order = Direction.DESC;
		}else {
			order = Direction.ASC;
		}
		try {
			PageRequest pageReq = PageRequest.of(pageNumber, perPage, order, sortBy);
			List<Map> foo = resultRepository.compareRunVsRef(runId, pageReq);
			
			return foo;

		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	
	/*
	 * Returns the count of rows for the comparison of run vs Ref  
	 * @param runId - the id for the first run  

	 */
	@RequestMapping("/compareRunVsRefCount")
	public Integer getComparedRuns(@RequestParam int runId) {
		try {
			return resultRepository.compareRunVsRefCount(runId);

		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/*
	 * Returns specific results from the database 
	 * @param ids - a comma separated list of result_ids the users wants details on  	 */
	@RequestMapping("/getResults")
	public List<Result> getResults(@RequestParam String ids) {
		List<Result> resultList = new ArrayList<Result>();
		
		String[] idList = ids.split(",");
		for (String id : idList){
			try {
				Integer idInt = Integer.valueOf(id);
			
				Optional<Result> result = resultRepository.findById(idInt);
				if (!result.isEmpty()){
					resultList.add(result.get());
				}
			}catch (Exception e){
				throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
			}
		}
		return resultList;
	}
	
}
