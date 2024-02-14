# ols-router-test-engine
The springboot application which runs router tests. Currently (feb14, 2024) when the application is run, it checks the router test database for any "Runs" that have a status of "queued", and runs all the associated tests for those runs then stops. If the application needs to be setup to periodically run, it could be done with various outside tools to schedule this application, or done within the app itself by adding an infinite while loop and some wait period etc.

The main() function is in:
package ca.bc.gov.ols.router.testing.engine;
RouterTestingEngine.java


The database URL and access is read from environment variables you must have setup in your run configuration:
spring.datasource.url
spring.datasource.username
spring.datasource.password

Optional environment variable:
OLS-ROUTER-SAVE-RESULTS - This can be set to "false" if you wish to run all the test but not save the results in the database, which can be useful for testing without affecting any production/real test data. Typically one wants to save the results, so if you don't provide this the default is always "true".