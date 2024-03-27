# ols-router-test-engine
The springboot application which runs router tests. It checks the router test database for any "Runs" that have a status of "queued", and runs all the associated tests for those runs then stops. See the OLS-ROUTER-RUN-EVERY-X-SEC description below to configure how long to wait before checking for more queued run requests.

The main() function is in:
package ca.bc.gov.ols.router.testing.engine;
RouterTestingEngine.java


The database URL and access is read from environment variables you must have setup in your run configuration:
spring.datasource.url
spring.datasource.username
spring.datasource.password

Optional environment variable:
OLS-ROUTER-SAVE-RESULTS - This can be set to "false" if you wish to run all the test but not save the results in the database, which can be useful for testing without affecting any production/real test data. Typically one wants to save the results, so if you don't provide this the default is always "true".


OLS-ROUTER-RUN-EVERY-X-SEC - This can be set to an integer. This is the time, in seconds, the engine will pause after each check of the database for queued test runs, beforing checking for more requests to run additional tests. Default if not given is 30 seconds.