# OLS Router Test Kit SQL Scripts

Scripts to create and populate the OLS Router Test Kit database. 

1. Run the create_users.sql to create the database users and the database itself.
   Run this as the postgres root user, connected to the postgres database.

2. Create the schema by running ols_router_test_kit_schema.sql as the postgres user, connected to the newly created database: ols_router_test_kit

3. Load the sample data by running ols_router_test_kit_sample_data.sql, 
   connected to the ols_router_test_kit database as the ols_router_test_kit_owner user.
