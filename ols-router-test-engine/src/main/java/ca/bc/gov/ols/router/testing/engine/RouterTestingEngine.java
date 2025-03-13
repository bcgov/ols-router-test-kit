package ca.bc.gov.ols.router.testing.engine;

import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.old.router.testing.engine.utils.ParameterStringBuilder;
import ca.bc.gov.ols.router.testing.engine.dao.DatasetRepository;
import ca.bc.gov.ols.router.testing.engine.dao.EnvironmentRepository;
import ca.bc.gov.ols.router.testing.engine.dao.ResultRepository;
import ca.bc.gov.ols.router.testing.engine.dao.RunRepository;
import ca.bc.gov.ols.router.testing.engine.dao.TestRepository;
import ca.bc.gov.ols.router.testing.engine.entity.Environment;
import ca.bc.gov.ols.router.testing.engine.entity.Result;
import ca.bc.gov.ols.router.testing.engine.entity.Run;
import ca.bc.gov.ols.router.testing.engine.entity.Test;

@SpringBootApplication
@ComponentScan({ "ca.bc.gov.ols.router.testing.engine.dao" })
@EntityScan("ca.bc.gov.ols.router.testing.engine.entity")
@EnableJpaRepositories("ca.bc.gov.ols.router.testing.engine.dao")
@EnableAutoConfiguration
public class RouterTestingEngine {

	private static final String USER_AGENT = "Mozilla/5.0";

	public static final GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);

	public static ResultRepository resultRepository;
	public static DatasetRepository datasetRepository;
	public static EnvironmentRepository environmentRepository;
	public static RunRepository runRepository;
	public static TestRepository testRepository;
	
	public RouterTestingEngine(ResultRepository resultRepository, DatasetRepository datasetRepository,
			EnvironmentRepository environmentRepository, RunRepository runRepository, TestRepository testRepository) {
		// initialize all our database access repositories so we can easily query
		// anything with a single static variable per table.
		RouterTestingEngine.resultRepository = resultRepository;
		RouterTestingEngine.datasetRepository = datasetRepository;
		RouterTestingEngine.environmentRepository = environmentRepository;
		RouterTestingEngine.runRepository = runRepository;
		RouterTestingEngine.testRepository = testRepository;
	}

	/*
	 * This Test engine looks in the application.properties specified Database and
	 * checks for any "Runs" that have a status of "queued" if so, it runs the
	 * specified test, sets the "run" object's 'run_date' to today and 'status' to
	 * 'complete'
	 * 
	 * Results are saved by default Set an environment variable
	 * OLS-ROUTER-SAVE-RESULTS in your system variables to be "false" if you do no
	 * want to save results
	 */
	public static void main(String[] args) {
		SpringApplication.run(RouterTestingEngine.class, args);
		Boolean saveResults = true;

		// check the system variables for the possible setting to not save these results
		if ("false".equalsIgnoreCase(System.getenv("OLS-ROUTER-SAVE-RESULTS"))) {
			saveResults = false;
		}
		
		// check the system variables for the desired frequency to check for new test run requests
		int runEveryXSec;
		String valueStr= System.getenv("OLS-ROUTER-RUN-EVERY-X-SEC");
		if (valueStr == null || valueStr.isBlank()) {
			runEveryXSec = 30;//use default
	    } else {
	        // Convert the value to an integer
	        try {
	            runEveryXSec  = Integer.parseInt(valueStr);
	        } catch (NumberFormatException e) {
	        	runEveryXSec = 30;//use default
	        }
	    }
		
		
		//Clean up and mark as failed any runs that were in-progress, but since we are starting this again, they won't be finishing
		List<Run> cleanupList = runRepository.findByStatusOrderByQueuedTimestamp("inprogress");
		for (Run run : cleanupList) {
			run.setStatus("failed");
			
			//TODO set number_of_results if we add that to the DB
			
			runRepository.save(run);
		}
		
		
		//Infinite Loop to keep checking for new test run results:
		while (true) {
		
			List<Run> runList = runRepository.findByStatusOrderByQueuedTimestamp("queued");
			int totalCount = runList.size();
			int count = 0;
			Boolean success = false;
	
			for (Run run : runList) {
	
				// double check the status in case it has changed while running previous tests
				// etc, end the process if it has changed.
				Run latestRun = runRepository.findById(run.getRunId()).get();
				if (!"queued".contentEquals(latestRun.getStatus())) {
					System.out.println("RunID: " + run.getRunId()
							+ " was no longer in status 'queued' when we attempted to run it. A different server processed it or a user changed the status unexpectantly. Ending the current Engine's test runs.");
					return;
				}
	
				run.setStatus("inprogress");
				runRepository.save(run);
	
				String groupName = run.getGroupName();
	
				ArrayList<Test> list = new ArrayList<Test>();
				if ("ALL".equals(groupName)) {
					list = (ArrayList<Test>) testRepository.findAll();
				} else {
					list = (ArrayList<Test>) testRepository.findAllByGroupName(groupName);
				}
				System.out.println("Running RunID: " + run.getRunId() + " which entails: " + list.size() + " tests.");
				success = runTests(saveResults, list, run);
	
				if(success == true) {
				
					run.setStatus("complete");
					run.setRunTimestamp(ZonedDateTime.now());
					runRepository.save(run);
					count++;
				}else {
					run.setStatus("failed");
					runRepository.save(run);
				}
			}
			System.out.println(count + " / " +  totalCount + " runs completed successfully.");
			
			// Sleep for runEveryXSec seconds
            try {
                Thread.sleep(runEveryXSec * 1000); // 30 seconds in milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
	}

	private static Boolean runTests(Boolean saveResults, List<Test> list, Run run) {
		
		HashMap<String, String> envParameters = getEnvParameters(run.getEnvironmentId());
		envParameters.put("saveResults", saveResults.toString());

		Map<String, String> parameters = new HashMap<String, String>();

		int testId = -1;
		int count = 0;
		int tenPercent = list.size() / 10;
		System.out.println("Status: each * is 10% complete for the current run:");
		
		//setup an overall exception max, so we fail if we keep getting DNS errors (or others) consistently throughout the retry process.
		int exceptionCount = 0;
		final int MAX_EXCEPTIONS = 100;
		
		for (Test test : list) {
			if (count == tenPercent) { //run for every 10% of progress we achieve
				System.out.print(" * ");
				count = 0;
				System.gc();//encourage garbage collection while running tests
				//TODO set number_of_results if we add that to the DB
			}
			testId = test.getTestId();
			
			if ("Custom".equals(test.getGroupName())) {// Custom tests, use the test-specific parameters
				parameters = test.getParameterCopy();
				if (!run.getForwardRouteInd()) {
					parameters.put("points", test.getPointsReversed().replace(' ', ','));
				} else {
					parameters.put("points", test.getPoints().replace(' ', ','));
				}

			} else { // non-Custom tests, use the default parameters from the overall 'run'
				parameters = run.getParameterCopy();
				if (!run.getForwardRouteInd()) {
					parameters.put("points", test.getPointsReversed().replace(' ', ','));
				} else {
					parameters.put("points", test.getPoints().replace(' ', ','));
				}
			}
			
			//setup a retry system for each test that waits longer each time it fails
			int[] retryDelays = {1, 20, 60, 180, 360}; // Retry delays in seconds


			for (int attempt = 0; attempt < retryDelays.length; attempt++) {
			    try {
			        Result result = runSingleTest(envParameters, parameters, run.getRunId(), testId);

			        // Save the results to the DB if we're supposed to
			        if ("true".equals(envParameters.get("saveResults"))) {
			            resultRepository.save(result);
			        } else {
			            System.out.println("Result: " + result);
			        }

			        break;//successful run, get out of exception-retry loop and go to the next test.

			    } catch (IOException e) {
			        exceptionCount++;
			        if (exceptionCount >= MAX_EXCEPTIONS) {
				        System.out.println("Aborting overall process during Test ID: " + testId);
			            System.out.println("Too many exceptions encountered throughout overall run(100). Aborting.");
			            e.printStackTrace();
			            return false;
			        }
			        

			        try {
			            System.out.println("Failed when testing URL: " + ParameterStringBuilder.getParamsString(parameters));
			        } catch (UnsupportedEncodingException e1) {
			            e1.printStackTrace();
			        }


			        if (attempt < retryDelays.length - 1) { // If not the last attempt, wait before retrying
			            try {
			            	System.out.println("Re-Trying request, attempt #" + (attempt+1) + "/5");
			                Thread.sleep(retryDelays[attempt] * 1000L);
			            } catch (InterruptedException ie) {
			                Thread.currentThread().interrupt();
			                return false;
			            }
			        }else {
			        	System.out.println("Aborting overall process during Test ID: " + testId);
			        	System.out.println("Reached max wait time on this request. Aborting.");
			        	e.printStackTrace();
			        	return false; // Longest wait/retry failed
			        }
			    }
			}

			

			
			parameters.clear();
			count++;
		}
		System.out.println("");
		return true;
	}

	private static Result runSingleTest(Map<String, String> envParameters, Map<String, String> parameters,
			Integer runId, Integer testId) throws IOException {
		String baseUrl = "";
		if ("true".equals(parameters.get("followTruckRoute"))) {
			baseUrl = envParameters.get("truckurl");
		} else {// sometimes this param is not listed at all, but we assume 'false' in that case
				// as well with this 'else' logic
			baseUrl = envParameters.get("baseurl");
		}
		
		URL url = new URL(baseUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		// con.setRequestMethod("GET");
		
		con.setRequestProperty("apikey", envParameters.get("apikey"));

		con.setDoOutput(true);
		con.setRequestProperty("User-Agent", USER_AGENT);
		// con.setRequestProperty("Content-Type", "application/json");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);

		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
		out.flush();
		out.close();

		//System.out.println("Inside singletest testing URL: " + ParameterStringBuilder.getParamsString(parameters));

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer originalContent = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			originalContent.append(inputLine);
		}
		in.close();
		con.disconnect();

		String content = originalContent.toString();
		ObjectMapper objectMapper = new ObjectMapper();

		// Convert the JSON content to a Map
		Map<String, Object> attributes = objectMapper.readValue(content, Map.class);
		
		Object partitions = attributes.get("partitions");
		JsonNode partitionInfo = null;

		if (partitions instanceof List) {
		    List<Map<String, Object>> partitionsList = (List<Map<String, Object>>) partitions;
		    partitionInfo = objectMapper.valueToTree(partitionsList);
		}

		ArrayList pointList = (ArrayList) attributes.get("route");
		Coordinate[] pointArray = new Coordinate[pointList.size()];

		// convert to Coordinate[] for Geometry type construction, can't seem to cast to
		// the correct object type just using .toArray(), so we do this instead:
		for (int i = 0; i < pointList.size(); i++) {
			ArrayList point = (ArrayList) pointList.get(i);
			Coordinate c = new Coordinate(
				    ((Number) point.get(0)).doubleValue(), 
				    ((Number) point.get(1)).doubleValue()
				);
			pointArray[i] = c;
		}
		Geometry route = gf.createLineString(pointArray);

		double dis = ((Number)attributes.get("distance")).doubleValue();
		if (dis > 0){
			dis = dis * 1000; //only convert to m if value is positive.
		}else if (dis >= -0.001){
			dis = -1;
		}

		Result result = new Result(
			    runId,
			    testId,
			    ((Number) attributes.get("executionTime")).doubleValue(),
			    dis,
			    route,
			    ((Number) attributes.get("time")).doubleValue(),
			    partitionInfo
			);

		return result;

	}

	private static HashMap<String, String> getEnvParameters(Integer envId){

		HashMap<String, String> envParameters = new HashMap<String, String>();
		
		if (envId == null) {
		return envParameters;
		}
		Environment env = environmentRepository.findById(envId).get();


		envParameters.put("baseurl", env.getBaseApiUrl() + "directions.json?");
		envParameters.put("truckurl", env.getBaseApiUrl() + "truck/directions.json?");
		envParameters.put("apikey", env.getApiKey());

		return envParameters;
	}

}