
-- change these passwords to appropriate secrets here
-- or later run: ALTER USER ols_router_test_kit_app PASSWORD 'newsecret';

CREATE USER ols_router_test_kit_owner WITH PASSWORD 'secret1';
CREATE USER ols_router_test_kit_app WITH PASSWORD 'secret2';


CREATE DATABASE ols_router_test_kit2 WITH OWNER ols_router_test_kit_owner;