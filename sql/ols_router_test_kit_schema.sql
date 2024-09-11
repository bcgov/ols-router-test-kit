--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;

-- Create the databse objects as the app owner user
-- this needs to match the owner user previously created
SET ROLE ols_router_test_kit_owner;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: code_versions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.code_versions (
    code_id integer NOT NULL,
    code_base character varying(10),
    github_commit_id character varying(7),
    version_num character varying(50),
    description character varying(300)
);


--
-- Name: TABLE code_versions; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.code_versions IS 'Provides details of the version of code used by the router under test, which is useful to relate changes in test results to code changes over time.';


--
-- Name: COLUMN code_versions.code_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.code_versions.code_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN code_versions.code_base; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.code_versions.code_base IS 'A general identifier for major code changes, e.g., RP (Route Planner),  NG (Route Planner Next Generation), onRoute (onRouteBC work). ';


--
-- Name: COLUMN code_versions.github_commit_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.code_versions.github_commit_id IS 'The first 7 digits of the GitHub commit ID, which is useful to identify the exact version of the code, e.g. e60a7cd.';


--
-- Name: COLUMN code_versions.version_num; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.code_versions.version_num IS 'The Maven version number of the code (available from the API response), e.g., 2.1.8, 2.2.1-SNAPSHOT.';


--
-- Name: COLUMN code_versions.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.code_versions.description IS 'A description of the changes made in the code version.';


--
-- Name: code_versions_code_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.code_versions_code_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: code_versions_code_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.code_versions_code_id_seq OWNED BY public.code_versions.code_id;


--
-- Name: datasets; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.datasets (
    dataset_id integer NOT NULL,
    bc_subset_ind boolean,
    road_source character varying(10),
    road_network_timestamp timestamp with time zone,
    description character varying(240)
);


--
-- Name: TABLE datasets; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.datasets IS 'Describes a Routable BC dataset based on the data pieces that went into it, with the purpose being to track the data that was used by the router under test. This is useful for comparisons between the same version of data in different environments or different versions of data in the same environment.';


--
-- Name: COLUMN datasets.dataset_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.datasets.dataset_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN datasets.bc_subset_ind; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.datasets.bc_subset_ind IS 'Indicates whether the data is a subset of BC, i.e., true (subset of BC), false (all of BC).';


--
-- Name: COLUMN datasets.road_source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.datasets.road_source IS 'The source of the road data, e.g., ITN (Integration Transporation Network). Road source is usually ITN. Some early tests were made using Google and Translink CVRP (Commercial Vehicle Route Planner) road data.';


--
-- Name: COLUMN datasets.road_network_timestamp; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.datasets.road_network_timestamp IS 'The vintage of the road data, defined by the ITN version timestamp. Historically, for monthly data releases this has usually been the first day of the month that the ITN data was released.';


--
-- Name: COLUMN datasets.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.datasets.description IS 'A description of what additional data was included, e.g., Truck Notices, RDM. This is mostly relevant the first time such additional data is added to the Router.';


--
-- Name: datasets_dataset_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.datasets_dataset_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: datasets_dataset_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.datasets_dataset_id_seq OWNED BY public.datasets.dataset_id;


--
-- Name: environments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.environments (
    env_id integer NOT NULL,
    platform character varying(50),
    environment character varying(50),
    base_api_url character varying(100),
    api_key character varying(50)
);


--
-- Name: TABLE environments; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.environments IS 'Describes the specific router instance under test.';


--
-- Name: COLUMN environments.env_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.environments.env_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN environments.platform; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.environments.platform IS 'The platform that the test is run against, e.g., Devel, Deliv, Test, Prod, data integration.';


--
-- Name: COLUMN environments.environment; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.environments.environment IS 'The environment where the router is under test, e.g., DataBC, Translink, Refractions, Google.';


--
-- Name: COLUMN environments.base_api_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.environments.base_api_url IS 'The base URL of the router API under test.';


--
-- Name: COLUMN environments.api_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.environments.api_key IS 'The API key to access the router.';


--
-- Name: environments_env_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.environments_env_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: environments_env_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.environments_env_id_seq OWNED BY public.environments.env_id;


--
-- Name: results; Type: TABLE; Schema: public; Owner: -
--
CREATE TABLE public.results (
    result_id integer NOT NULL,
    run_id integer,
    test_id integer,
    calc_time real,
    distance real,
    route_geometry public.geometry(Geometry, 3005),
    duration real,
    partition_info jsonb 
);



--
-- Name: TABLE results; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.results IS 'Records the results of a single test within a test run. ';


--
-- Name: COLUMN results.result_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.results.result_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN results.run_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.results.run_id IS 'Identifies the test run information used to generate the result. It is a foreign key reference to the runs table.';


--
-- Name: COLUMN results.test_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.results.test_id IS 'Identifies the test case used to generate the result. It is a foreign key reference to the tests table.';


--
-- Name: COLUMN results.calc_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.results.calc_time IS 'The request execution time (in milliseconds) returned by the router for the test.';


--
-- Name: COLUMN results.distance; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.results.distance IS 'The travel distance (in kilometres) returned by the router for the test. ';


--
-- Name: COLUMN results.route_geometry; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.results.route_geometry IS 'The linestring geometry of the route returned by the router.';


--
-- Name: COLUMN results.duration; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.results.duration IS 'The travel time (in seconds) returned by the router for the test.';


--
-- Name: COLUMN results.partition_signature; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.results.partition_signature IS 'Only for truck route tests (isTruckRoute partition), the signature is a sequence of alternating 1s and 0s that represents the alternating partitions of the route as it transitions between 1 (true, partition is a truck route) and 0 (false, partition is not a truck route), e.g., 010.';


--
-- Name: COLUMN results.partition_indices; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.results.partition_indices IS 'Only for truck route tests (isTruckRoute partition), a list of the index values for the points in the route path where the partition value changes, separated by the pipe symobl, e.g., 0|20|70.';


--
-- Name: COLUMN results.partition_lengths; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.results.partition_lengths IS 'Only for truck route tests (isTruckRoute partition), a list of the distances (in kilometres) of each partition, separated by the pipe symbol, e.g., 1.2|5.7|3.2.';


--
-- Name: results_result_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.results_result_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: results_result_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.results_result_id_seq OWNED BY public.results.result_id;


--
-- Name: runs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.runs (
    run_id integer NOT NULL,
    description character varying(240),
    dataset_id integer,
    code_id integer,
    forward_route_ind boolean,
    run_timestamp timestamp with time zone,
    group_name character varying(50),
    env_id integer,
    parameters json,
    queued_timestamp timestamp with time zone,
    status character varying(10)
);


--
-- Name: TABLE runs; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.runs IS 'Defines a test run as a group of tests with a specific environment, dataset, and code version. The group of tests to run is specified using the group_name.';


--
-- Name: COLUMN runs.run_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.run_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN runs.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.description IS 'A description of the test run, explaining why it is performed.';


--
-- Name: COLUMN runs.dataset_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.dataset_id IS 'Identifies the dataset for the test run. It is a foreign key reference to the datasets table.';


--
-- Name: COLUMN runs.code_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.code_id IS 'Identifies the version of code for the test run. It is a foreign key reference to the code_versions table.';


--
-- Name: COLUMN runs.forward_route_ind; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.forward_route_ind IS 'Indicates whether the test run is in the forward or reverse direction, which his applied to all tests in the group, i.e., true (forward), false (reverse).';


--
-- Name: COLUMN runs.run_timestamp; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.run_timestamp IS 'The date and time the test run was performed.';


--
-- Name: COLUMN runs.group_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.group_name IS 'The name of the test group for the test(s) that are being run.';


--
-- Name: COLUMN runs.env_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.env_id IS 'Identifies the environment for the test run. It is a foreign key reference to the environments table.';


--
-- Name: COLUMN runs.parameters; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.parameters IS 'A JSON object of router parameter names and values, to be applied to each test run in the test group. However, parameters defined for a test will override any same-named run parameters.';


--
-- Name: COLUMN runs.queued_timestamp; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.queued_timestamp IS 'The date and time when the run was queued to be run.';


--
-- Name: COLUMN runs.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.runs.status IS 'The status of the run, i.e., queued, running, complete.';


--
-- Name: runs_run_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.runs_run_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: runs_run_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.runs_run_id_seq OWNED BY public.runs.run_id;


--
-- Name: tests; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tests (
    test_id integer NOT NULL,
    description character varying(240),
    group_name character varying(50),
    result_id_fwd_ref integer,
    result_id_rev_ref integer,
    notes character varying(300),
    good_demo_case_ind boolean,
    points character varying(400),
    parameters json,
    created_timestamp timestamp with time zone
);


--
-- Name: TABLE tests; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.tests IS 'Defines a single routing test case, including the origin point, any number of waypoints, and the destination point, as well as specific router parameters and descriptive information about the test.';


--
-- Name: COLUMN tests.test_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tests.test_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN tests.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tests.description IS 'A description of the purpose of the test.';


--
-- Name: COLUMN tests.group_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tests.group_name IS 'The name of the test group that the test is assigned to, e.g., Custom, Generated Site, Generated Muni. ';


--
-- Name: COLUMN tests.result_id_fwd_ref; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tests.result_id_fwd_ref IS 'An optional reference to a result (run in the forward direction) which is considered to be a standard for comparison against other results (run in the forward direction) of the test case.';


--
-- Name: COLUMN tests.result_id_rev_ref; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tests.result_id_rev_ref IS 'An optional reference to a result (run in the reverse direction) which is considered to be a standard for comparison against other results (run in the reverse direction) of the test case.';


--
-- Name: COLUMN tests.notes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tests.notes IS 'Additional descriptive notes about the test.';


--
-- Name: COLUMN tests.good_demo_case_ind; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tests.good_demo_case_ind IS 'Indicates whether the test is an interesting case for demonstrations, i.e., true (good test case for demonstrations), false.';


--
-- Name: COLUMN tests.points; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tests.points IS 'A list of origin point, any number of waypoints, and destination point, in the same format as the router points parameter (lon1,lat1,lon2,lat2,...), e.g.,-121.768099,49.2374933,-119.2702664,52.8281629,-129.9980382,58.438932.';


--
-- Name: COLUMN tests.parameters; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tests.parameters IS 'A JSON object of router parameter names and values, to be included as part of the test request. Test parameters override any same-named parameters defined for a run.';


--
-- Name: COLUMN tests.created_timestamp; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tests.created_timestamp IS 'The date and time the test was created.';


--
-- Name: tests_test_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tests_test_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: tests_test_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.tests_test_id_seq OWNED BY public.tests.test_id;


--
-- Name: code_versions code_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.code_versions ALTER COLUMN code_id SET DEFAULT nextval('public.code_versions_code_id_seq'::regclass);


--
-- Name: datasets dataset_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.datasets ALTER COLUMN dataset_id SET DEFAULT nextval('public.datasets_dataset_id_seq'::regclass);


--
-- Name: environments env_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.environments ALTER COLUMN env_id SET DEFAULT nextval('public.environments_env_id_seq'::regclass);


--
-- Name: results result_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.results ALTER COLUMN result_id SET DEFAULT nextval('public.results_result_id_seq'::regclass);


--
-- Name: runs run_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.runs ALTER COLUMN run_id SET DEFAULT nextval('public.runs_run_id_seq'::regclass);


--
-- Name: tests test_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tests ALTER COLUMN test_id SET DEFAULT nextval('public.tests_test_id_seq'::regclass);


--
-- Name: code_versions code_versions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.code_versions
    ADD CONSTRAINT code_versions_pkey PRIMARY KEY (code_id);


--
-- Name: datasets datasets_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.datasets
    ADD CONSTRAINT datasets_pkey PRIMARY KEY (dataset_id);


--
-- Name: environments environments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.environments
    ADD CONSTRAINT environments_pkey PRIMARY KEY (env_id);


--
-- Name: results results_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.results
    ADD CONSTRAINT results_pkey PRIMARY KEY (result_id);


--
-- Name: runs runs_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.runs
    ADD CONSTRAINT runs_pkey PRIMARY KEY (run_id);


--
-- Name: tests tests_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tests
    ADD CONSTRAINT tests_pkey PRIMARY KEY (test_id);


--
-- Name: results results_run_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.results
    ADD CONSTRAINT results_run_id_fkey FOREIGN KEY (run_id) REFERENCES public.runs(run_id);


--
-- Name: results results_test_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.results
    ADD CONSTRAINT results_test_id_fkey FOREIGN KEY (test_id) REFERENCES public.tests(test_id);


--
-- Name: runs runs_code_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.runs
    ADD CONSTRAINT runs_code_id_fkey FOREIGN KEY (code_id) REFERENCES public.code_versions(code_id);


--
-- Name: runs runs_dataset_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.runs
    ADD CONSTRAINT runs_dataset_id_fkey FOREIGN KEY (dataset_id) REFERENCES public.datasets(dataset_id);


--
-- Name: runs runs_env_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.runs
    ADD CONSTRAINT runs_env_id_fkey FOREIGN KEY (env_id) REFERENCES public.environments(env_id);


--
-- PostgreSQL database dump complete
--

-- these grants need to match the app user previously created
GRANT ALL ON SCHEMA public TO ols_router_test_kit_app;

GRANT ALL ON ALL TABLES IN SCHEMA public TO ols_router_test_kit_app;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO ols_router_test_kit_app;

