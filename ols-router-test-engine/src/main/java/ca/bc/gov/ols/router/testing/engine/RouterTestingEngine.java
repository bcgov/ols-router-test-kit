package ca.bc.gov.ols.router.testing.engine;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

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

	@Autowired
	public static ResultRepository resultRepository;
	@Autowired
	public static DatasetRepository datasetRepository;
	@Autowired
	public static EnvironmentRepository environmentRepository;
	@Autowired
	public static RunRepository runRepository;
	@Autowired
	public static TestRepository testRepository;

	@Autowired
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

		List<Run> runList = runRepository.findByStatusOrderByQueuedDateTime("queued");
		int count = 0;
		List<Result> results = null;

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
			results = runTests(saveResults, list, run);

			run.setStatus("complete");
			run.setRunDate(LocalDate.now());
			runRepository.save(run);
			count++;
		}
		System.out.println(count + " total runs found and completed.");
	}

	private static List<Result> runTests(Boolean saveResults, List<Test> list, Run run) {

		HashMap<String, String> envParameters = getEnvParameters(run.getEnvironmentId());
		envParameters.put("saveResults", saveResults.toString());

		ArrayList<Result> results = new ArrayList<Result>();

		Map<String, String> parameters = new HashMap<String, String>();

		int testId = -1;
		int count = 0;
		int tenPercent = list.size() / 10;
		System.out.println("Status: each * is 10% complete for the current run:");
		for (Test test : list) {
			if (count == tenPercent) {
				System.out.print(" * ");
				count = 0;
			}
			testId = test.getTestId();
			if ("Custom".equals(test.getGroupName())) {// Custom tests, use the test-specific parameters
				parameters = test.getParameterCopy();
				if (!run.getForwardRoute()) {
					parameters.put("points", test.getPointsReversed().replace(' ', ','));
				} else {
					parameters.put("points", test.getPoints().replace(' ', ','));
				}

			} else { // non-Custom tests, use the default parameters from the overall 'run'
				parameters = run.getParameterCopy();
				if (!run.getForwardRoute()) {
					parameters.put("points", test.getPointsReversed().replace(' ', ','));
				} else {
					parameters.put("points", test.getPoints().replace(' ', ','));
				}
			}
			try {
				Result result = runSingleTest(envParameters, parameters, run.getRunId(), testId);

				// save the results to the DB if we're supposed to
				if ("true".equals(envParameters.get("saveResults"))) {
					resultRepository.save(result);
					// System.out.println("saved test result.");
				}
				results.add(result);
			} catch (IOException e) {

				System.out.println("IO Error, invalid URL or parameters");
				System.out.println("Test ID:" + testId);
				try {
					System.out
							.println("Failed when testing URL: " + ParameterStringBuilder.getParamsString(parameters));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

				e.printStackTrace();
				return null;
			}
			count++;
		}
		System.out.println("");
		return results;
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

		con.setDoOutput(true);
		con.setRequestProperty("User-Agent", USER_AGENT);
		// con.setRequestProperty("Content-Type", "application/json");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);

		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
		out.flush();
		out.close();

		// System.out.println("testing URL: " +
		// ParameterStringBuilder.getParamsString(parameters));

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer originalContent = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			originalContent.append(inputLine);
		}
		in.close();
		con.disconnect();

		String content = originalContent.toString();

		Gson gson = new Gson();
		Map<Object, Object> attributes = gson.fromJson(content, Map.class);

		String partitionIndices = "";
		String partitionSignature = "";

		String partition = (String) attributes.get("partition");
		if ("isTruckRoute".equals(partition)) {
			ArrayList list = (ArrayList) attributes.get("partitions");

			for (int i = 0; i < list.size(); i++) {
				LinkedTreeMap ele = (LinkedTreeMap) list.get(i);
				Map<Object, Object> m = gson.fromJson(content, Map.class);
				Integer index = ((Double) ele.get("index")).intValue();

				partitionIndices = partitionIndices + index + "|";

				boolean isTruck = (boolean) ele.get(partition);
				if (isTruck) {
					partitionSignature = partitionSignature + "1";
				} else {
					partitionSignature = partitionSignature + "0";
				}
			}
			partitionIndices = StringUtils.chop(partitionIndices); // get rid of the trailing separator, '|' once the
																	// string is complete.
		}

		ArrayList pointList = (ArrayList) attributes.get("route");
		Coordinate[] pointArray = new Coordinate[pointList.size()];

		// convert to Coordinate[] for Geometry type construction, can't seem to cast to
		// the correct object type just using .toArray(), so we do this instead:
		for (int i = 0; i < pointList.size(); i++) {
			ArrayList point = (ArrayList) pointList.get(i);
			Coordinate c = new Coordinate((Double) point.get(0), (Double) point.get(1));
			pointArray[i] = c;
		}
		Geometry route = new GeometryFactory().createLineString(pointArray);

		Result result = new Result(runId, testId, (double) attributes.get("executionTime"),
				(double) attributes.get("distance"), route, (double) attributes.get("time"), partitionSignature,
				partitionIndices);

		return result;

	}

	private static HashMap<String, String> getEnvParameters(Integer envId) {
		Environment env = environmentRepository.findById(envId).get();

		HashMap<String, String> envParameters = new HashMap<String, String>();

		envParameters.put("baseurl", env.getBaseApiUrl() + "directions.json?");
		envParameters.put("truckurl", env.getBaseApiUrl() + "truck/directions.json?");

		return envParameters;
	}

}