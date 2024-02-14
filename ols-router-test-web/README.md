# ols-router-test-web
The springboot server application which provides an API for accessing the router test database.

It uses a number of shared libraries with the old-router-test-engine as well where the database access and objects are defined. The main() function is in:
package ca.bc.gov.ols.router.testing.web;
RouterTestingApplication.java


The database URL and access is read from environment variables you must have setup in your run configuration:
spring.datasource.url
spring.datasource.username
spring.datasource.password


