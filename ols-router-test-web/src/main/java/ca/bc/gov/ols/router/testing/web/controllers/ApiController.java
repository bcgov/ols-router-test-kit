package ca.bc.gov.ols.router.testing.web.controllers;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.linestring;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;
import java.time.ZonedDateTime;

import org.geolatte.geom.Feature;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.json.GeoJsonFeature;
import org.geolatte.geom.json.GeoJsonFeatureCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ca.bc.gov.old.router.testing.web.utilities.RouterApiClient;
import ca.bc.gov.old.router.testing.web.utilities.RouteResponse;
import ca.bc.gov.old.router.testing.web.utilities.RouterStatusResponse;

import ca.bc.gov.ols.router.testing.engine.dao.CodeVersionRepository;
import ca.bc.gov.ols.router.testing.engine.dao.DatasetRepository;
import ca.bc.gov.ols.router.testing.engine.dao.EnvironmentRepository;
import ca.bc.gov.ols.router.testing.engine.dao.ResultRepository;
import ca.bc.gov.ols.router.testing.engine.dao.RunRepository;
import ca.bc.gov.ols.router.testing.engine.dao.TestRepository;
import ca.bc.gov.ols.router.testing.engine.entity.CodeVersion;
import ca.bc.gov.ols.router.testing.engine.entity.Dataset;
import ca.bc.gov.ols.router.testing.engine.entity.Environment;
import ca.bc.gov.ols.router.testing.engine.entity.Result;
import ca.bc.gov.ols.router.testing.engine.entity.Run;
import ca.bc.gov.ols.router.testing.engine.entity.Test;
import ca.bc.gov.ols.router.testing.engine.entity.View;
import ca.bc.gov.ols.router.testing.web.exceptions.InvalidParameterException;
import jakarta.persistence.Column;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


//@CrossOrigin(maxAge = 3600)
@RestController
@CrossOrigin
public class ApiController {
	
	private final ResultRepository resultRepository;
	private final TestRepository testRepository;
	private final RunRepository runRepository;
	private final EnvironmentRepository environmentRepository;
	private final DatasetRepository datasetRepository;
	private final CodeVersionRepository codeVersionRepository;

	public ApiController(ResultRepository resultRepository, 
				TestRepository testRepository, 
				RunRepository runRepository, 
				EnvironmentRepository environmentRepository,
				DatasetRepository datasetRepository,
				CodeVersionRepository codeVersionRepository) {
		this.resultRepository = resultRepository;
		this.testRepository = testRepository;
		this.runRepository = runRepository;
		this.environmentRepository = environmentRepository;
		this.datasetRepository=  datasetRepository;
		this.codeVersionRepository = codeVersionRepository;
	}

	/**
	 * Returns a paged, sorted list of Result entities from the DB in Json format
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
	public List<Map>getSortedPagedResults(@RequestParam(defaultValue = "0")int pageNumber, @RequestParam(defaultValue = "10") int perPage, @RequestParam(defaultValue = "runId") String sortBy, @RequestParam Optional<Boolean> descending, @RequestParam Optional<String> filterColumn, @RequestParam Optional<Integer> filterValue) {
		Direction order;
		
		if (descending.isPresent() && descending.get()==true) {
			order = Direction.DESC;
		}else {
			order = Direction.ASC;
		}
		try {		
			PageRequest pageReq = PageRequest.of(pageNumber, perPage, order, sortBy);
			List<Map> pageRes = null;
			if(!filterColumn.isEmpty() && "runId".equals(filterColumn.get())) {
				pageRes = resultRepository.findByRunIdIsCustom(filterValue.get(), pageReq);
			}else if(!filterColumn.isEmpty() && "testId".equals(filterColumn.get())) {
				pageRes = resultRepository.findByTestIdIsCustom(filterValue.get(), pageReq);
			}else {
				pageRes = resultRepository.findAllCustom(pageReq);
			}
			return pageRes;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
		
	}
	
	/**
	 * Returns a paged, sorted list of Result entities from the DB in Json format
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. resultId 
	 * @param <optional> filterColumn - the name of the column to filter: runId or testId is supported so far 
	 * @param <optional> filterValue - the integer value to filter on
	*/
	@RequestMapping("/resultListForTest")
	@JsonView(View.Default.class)
	public List<Map>getResultListForTest(@RequestParam int testId, @RequestParam(defaultValue = "0")int pageNumber, @RequestParam(defaultValue = "10") int perPage, @RequestParam(defaultValue = "runId") String sortBy, @RequestParam Optional<Boolean> descending, @RequestParam Optional<String> filterColumn, @RequestParam Optional<Integer> filterValue) {
		Direction order;
		
		if (descending.isPresent() && descending.get()==true) {
			order = Direction.DESC;
		}else {
			order = Direction.ASC;
		}
		try {		
			PageRequest pageReq = PageRequest.of(pageNumber, perPage, order, sortBy);
			return resultRepository.getResultListForTest(testId, pageReq);
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/**
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

	/**
	 * Returns a paged, sorted list of Bulk Test entities (group name != "custom" means they are bulk type) from the DB in Json format
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. testId 
	 */
	@RequestMapping("/bulkTests")
	public List<Test> getSortedPagedTests(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int perPage, @RequestParam(defaultValue = "testId") String sortBy, @RequestParam Optional<Boolean> descending) {
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

	/**
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

	/**
	 * Returns a paged, sorted list of Bulk Test entities (group name != "custom" means they are bulk type) from the DB in Json format
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. testId 

	 */
	@RequestMapping("/customTests")
	public List<Test> getSortedPagedCustomTests(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int perPage, @RequestParam(defaultValue = "testId") String sortBy, @RequestParam Optional<Boolean> descending) {
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

	/**
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
	
	/**
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
	
	/**
	 * Returns a paged, sorted list of Run entities from the DB in Json format
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. runId 
	 */
	@RequestMapping("/runs")
	public List<Map> getSortedPagedRuns(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int perPage, @RequestParam(defaultValue = "runId") String sortBy, @RequestParam Optional<Boolean> descending) {
		Direction order;
		
		if (descending.isPresent() && descending.get()==true) {
			order = Direction.DESC;
		}else {
			order = Direction.ASC;
		}
		try {
			PageRequest pageReq = PageRequest.of(pageNumber, perPage, order, sortBy);
			List<Map> list= runRepository.getRunList(pageReq);
			return list;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/**
	 * Returns a count of all runs
	 * Useful for calculating "last page" in pagination UI 
	 */
	@RequestMapping("/runsCount")
	public int getRunsCount() {
		try {
			return runRepository.getRunListCount() ;
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/**
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
	
	/**
	 * Returns a single environment
	 * @param envId - the id for the one you want  
	 */
	@RequestMapping("/environment")
	public Environment getEnvironment(@RequestParam int envId) {
		try {
			return environmentRepository.findById(envId).get();
		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/**
	 * Returns All environments 
	 */
	@RequestMapping("/environments")
	public List<Environment> getEnvironments() {
		Sort sortBy = Sort.by(Sort.Order.asc("environment"));
		return environmentRepository.findAll(sortBy);
		
	}
	
	/**
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
	
	/**
	 * Returns a comparison of result sets for 2 given run IDs 
	 * @param runIdA - the id for the first run  
	 * @param runIdB - the id for the second run
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. resultId
	 */
	@RequestMapping("/compareRuns")
	public List<Map> getComparedRuns(@RequestParam int runIdA, @RequestParam int runIdB,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int perPage, @RequestParam(defaultValue = "distance_diff") String sortBy, @RequestParam Optional<Boolean> descending) {
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
	
	/**
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

	/**
	 * Returns a comparison of result sets for 2 given run IDs 
	 * @param runIdA - the run id to compare against reference tests  
	 * @param PageNumber - the page number of data you are requesting 
	 * @param perPage - results you want perPage
	 * @param sortBy - the name of the column to sort by, e.g. resultId
	 */
	@RequestMapping("/compareRunVsRef")
	public List<Map> getCompareRunVsRef(@RequestParam int runId, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int perPage, @RequestParam(defaultValue = "distance_diff") String sortBy, @RequestParam Optional<Boolean> descending) {
		Direction order;
		
		if (descending.isPresent() && descending.get()==true) {
			order = Direction.DESC;
		}else {
			order = Direction.ASC;
		}
		try {
			PageRequest pageReq = PageRequest.of(pageNumber, perPage, order, sortBy);
			return resultRepository.compareRunVsRef(runId, pageReq);

		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/**
	 * Returns the count of rows for the comparison of run vs Ref  
	 * @param runId - the id for the first run  
	 */
	@RequestMapping("/compareRunVsRefCount")
	public Integer getComparedRunCount(@RequestParam int runId) {
		try {
			return resultRepository.compareRunVsRefCount(runId);

		}catch (Exception e){
			throw new InvalidParameterException("Invalid parameter value given. " + e.getMessage());
		}
	}
	
	/**
	 * Returns specific results from the database 
	 * @param ids - a comma separated list of result_ids the users wants details on
  	 */
	@RequestMapping("/getResults")
	public List<Result> getResults(@RequestParam List<Integer> ids) {
		List<Result> resultList = new ArrayList<Result>();
		
		for (Integer id : ids){
			Optional<Result> result = resultRepository.findById(id);
			if (!result.isEmpty()){
				resultList.add(result.get());
			}
		}
		return resultList;
	}


	/**
	 * Returns specific results from the database in geojson structure 
	 * @param ids - a comma separated list of result_ids the users wants details on  	 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/resultsGeoJson")
	public GeoJsonFeatureCollection<G2D, Integer> getResultsGeoJson(@RequestParam List<Integer> ids) {
		List<Feature<G2D, Integer>> features = new ArrayList<Feature<G2D, Integer>>();
		
		List<Map<String,Object>> results = resultRepository.getGeoJsonByIds(ids);
		for(Map<String,Object> result : results){
			HashMap<String,Object> props = new HashMap<String, Object>();
			Geometry<G2D> g = null;
			String points = null;
			Integer id = null;
			for(Entry<String, Object> entry : result.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if(key != null) {
					switch(key) {
					case "geometry":
						g = (Geometry<G2D>)value;
						break;
					case "points":
						points = (String)value;
						break;
					case "result_id":
						id = (Integer)value;
						// no break because we also add the id to the props
					default:
						props.put(key, value);
					}
				}
			}
			// if the route failed the geometry will be null
			if((g == null || g.isEmpty()) && points != null && !points.isEmpty()) {
				// we will replace with a simple 2-point line with the first and last points from the test case
				String[] coords = points.replace(' ', ',').split(",");
				g = linestring(WGS84, g(Double.valueOf(coords[0]), Double.valueOf(coords[1])), 
						g(Double.valueOf(coords[coords.length-2]), Double.valueOf(coords[coords.length-1])));
			}
			GeoJsonFeature<G2D, Integer> f = new GeoJsonFeature<G2D, Integer>(g, id, props);
			
			features.add(f);
		}
		
		return new GeoJsonFeatureCollection<G2D, Integer>(features);
	}

	/**
	 * Returns group Name options from the list of existing ones in the test table 
	 */
	@RequestMapping("/groupNameOptions")
	public List<Map> getGroupNameOptions() {
		return testRepository.getGroupNameOptopns();
	}
	
	/**
	 * Returns All code versions 
	 */
	@RequestMapping("/codeVersions")
	public List<CodeVersion> getCodeVersion() {
		return codeVersionRepository.findAllWithCustomSorting();
	}
	
	/**
	 * Returns All datasets 
	 */
	@RequestMapping("/datasets")
	public List<Dataset> getdatasets() {
		return datasetRepository.findAllWithCustomSorting();
	}

	/*
	 * Updates a testId with a new Forward Reference ID
	 */
	@RequestMapping("/setTestForwardRef")
	public String setTestForwardRef(@RequestParam int testId, @RequestParam int resultId) {
		Optional<Test> res = testRepository.findById(testId);
		if (!res.isEmpty() ) {
			Test test = res.get();
			test.setForwardResultId(resultId);
			testRepository.save(test);
			return "Update Succeeded";
		} else {
			return "Update Failed"; 
		}
		
	}
	
	@PostMapping("/createEnvironment")
    public Environment createEnvironment(@RequestBody Environment environment) {
        return environmentRepository.save(environment);
    }

    @PostMapping("/createDataset")
    public Dataset createDataset(@RequestBody Dataset dataset) {
        return datasetRepository.save(dataset);
    }
    
    @PostMapping("/createTest")
    public Test createTest(@RequestBody Test test) {
    	 test.setCreatedTimestamp(ZonedDateTime.now());  // Set the current timestamp
        return testRepository.save(test);
    }

    @PostMapping("/createCodeVersion")
    public CodeVersion createCodeVersion(@RequestBody CodeVersion codeVersion) {
        return codeVersionRepository.save(codeVersion);
    }
    
    //If the datasetId or codeId attributes in the run object passed in are null, 
    //this function will make a call to the specified environment and fill out the 2 former attrs appropriately based on the results
    //The above is the recommended usage to ensure correct values for code version and dataset are stored in the test results.
    @PostMapping("/createRun")
    public ResponseEntity<?> createRun(@RequestBody Run run) {
    	
    	if (run.getDatasetId() == null || run.getCodeId() == null) {
	    	//check the router response and get the exact "codeId" And "datasetId" it is using, then put that in the Run object
	    	//First try the router's   /status/ API that was recently added, 
	    	//then fall back to an simple router request in case this version doesn't support the /status option yet

    		Dataset newDataset = null;
			CodeVersion newCodeVersion = null;
			RouterStatusResponse statusResponse = null;
            ZonedDateTime roadNetworkTimestamp = null;
            String codeVersion = null;
			
    		try{
    			RouterApiClient apiClient = new RouterApiClient();

    			Environment environment = environmentRepository.findById(run.getEnvironmentId())
    				    .orElseThrow(() -> new RuntimeException("Environment not found for ID: " + run.getEnvironmentId()));

   				String url = environment.getBaseApiUrl();
   				String key = environment.getApiKey();
	    	
		    	//Check the /status   API for details first
    			url = url + "status";
    			String response = apiClient.get(url);

	            ObjectMapper objectMapper = new ObjectMapper();
	            objectMapper.registerModule(new JavaTimeModule());
	            JsonNode rootNode = objectMapper.readTree(response);

    			// Check if gitCommitId exists, if not, it's an older API so skip to the fall back plan
    	        if (rootNode.has("gitCommitId")) {
	                statusResponse = objectMapper.readValue(response, RouterStatusResponse.class);
	                roadNetworkTimestamp = statusResponse.getRoadNetworkTimestamp();
	                codeVersion = statusResponse.getVersion();
	                String GitCommitId = statusResponse.getGitCommitId();
		    	
	                //Lookup the Dataset that matches the RoadNetworktimestamp or create a new one if it doesn't exist yet:
	                newDataset = findOrCreateDataset(roadNetworkTimestamp);
	                //Lookup Code Version number and create it if it doesn't exist:
	                newCodeVersion = findOrCreateCodeVersion(codeVersion, GitCommitId);
	
	                run.setDatasetId(newDataset.getDatasetId());
	        		run.setCodeId(newCodeVersion.getCodeId());
    	        }                
                
	    		//Check the regular router request if we failed to find values so far
	    		if (run.getDatasetId() == null || run.getCodeId() == null) {
		
	   				//make a very simple/short route request
	    			url = environment.getBaseApiUrl();
	    			url = url + "directions.json?apikey=" + key + "&points=-123.36487770080568,48.42547002823357,-123.37015628814699,48.41812208203614&criteria=fastest";
	    			
	    			//System.out.println("URL: " + url);
	    			response = apiClient.get(url);
		            //System.out.println("Response: " + response);
		            
	    			//get the roadNetworkTimestamp reported by the router
		            objectMapper = new ObjectMapper();
		            objectMapper.registerModule(new JavaTimeModule());
	                RouteResponse routeResponse = objectMapper.readValue(response, RouteResponse.class);
	                roadNetworkTimestamp = routeResponse.getRoadNetworkTimestamp();
	                codeVersion = routeResponse.getVersion();
	
	                //Lookup the Dataset that matches the RoadNetworktimestamp or create a new one if it doesn't exist yet:
	                newDataset = findOrCreateDataset(roadNetworkTimestamp);
	                //Lookup Code Version number and create it if it doesn't exist:
	                newCodeVersion = findOrCreateCodeVersion(codeVersion, null);

	                run.setDatasetId(newDataset.getDatasetId());
	        		run.setCodeId(newCodeVersion.getCodeId());
	    		}    	
    		} catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An error occurred while creating the Run: " + e.getMessage());
	        }
    		
    	}
    	
    	runRepository.save(run);
        return ResponseEntity.status(HttpStatus.CREATED).body(run);
    }
    
    
    // Method to find or create a Dataset by road network timestamp
    private Dataset findOrCreateDataset(ZonedDateTime roadNetworkTimestamp) {
        Optional<Dataset> existingDataset = datasetRepository.findByRoadNetworkTimestamp(roadNetworkTimestamp);
        if (existingDataset.isPresent()) {
            return existingDataset.get();
        } else {
            Dataset newDataset = new Dataset(false, "ITN", roadNetworkTimestamp, "Default ITN");
            return datasetRepository.save(newDataset);
        }
    }

    // Method to find or create a CodeVersion by version number and GitCommitID
    private CodeVersion findOrCreateCodeVersion(String codeVersion, String githubCommitId) {
        // Attempt to find a matching CodeVersion where both versionNum and githubCommitId match
        Optional<CodeVersion> existingVersion = codeVersionRepository.findByVersionNumAndGithubCommitId(codeVersion, githubCommitId);

        if (existingVersion.isPresent()) {
            // Return the existing CodeVersion if both codeVersion and githubCommitId match
            return existingVersion.get();
        } else {
            // Create a new CodeVersion if no match was found
            CodeVersion newCodeVersion = new CodeVersion(codeVersion, "", githubCommitId);
            return codeVersionRepository.save(newCodeVersion);
        }
    }



    
}
