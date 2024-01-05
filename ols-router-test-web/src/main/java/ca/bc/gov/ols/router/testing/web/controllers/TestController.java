package ca.bc.gov.ols.router.testing.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.ols.router.testing.web.RouterTestingApplication;
/*
@RestController
@RequestMapping("/")
@CrossOrigin
*/
public class TestController {

	// can potentially open the db connection in the app, then use this reference to retrieve it
	// but there is probably a better way to manage the db connection using spring
	@Autowired 
	private RouterTestingApplication app;
	
	@RequestMapping(value = "/test1")
	public ResponseEntity<String> getTests() {
		// TODO: query the database to get all the tests (probably needs pagination)
		// return the test info as JSON array
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
