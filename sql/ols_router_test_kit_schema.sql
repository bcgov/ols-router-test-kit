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


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- Name: get_binary_partition_sig(jsonb, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_binary_partition_sig(json_data jsonb, key_name text) RETURNS text
    LANGUAGE plpgsql IMMUTABLE
    AS $$
DECLARE
    result TEXT := '';
    item jsonb;
    prev_value TEXT := '';
BEGIN
    -- Iterate through each element in the JSON array
    FOR item IN SELECT * FROM jsonb_array_elements(json_data)
    LOOP
        -- Determine the current value based on the specified key
        IF item->>key_name = 'true' THEN
            -- Append '1' if the previous value was '0' or if the result is empty
            IF prev_value = '0' OR result = '' THEN
                result := result || '1';
                prev_value := '1';
            END IF;
        ELSIF item->>key_name = 'false' THEN
            -- Append '0' if the previous value was '1' or if the result is empty
            IF prev_value = '1' OR result = '' THEN
                result := result || '0';
                prev_value := '0';
            END IF;
        END IF;
    END LOOP;
    
    IF result <> '' THEN
        -- Remove trailing duplicates to ensure the result alternates
        result := regexp_replace(result, '(.)\1+', '\1', 'g');
    END IF;
    
    RETURN result;
END;
$$;


ALTER FUNCTION public.get_binary_partition_sig(json_data jsonb, key_name text) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: code_versions; Type: TABLE; Schema: public; Owner: router
--

CREATE TABLE public.code_versions (
    code_id integer NOT NULL,
    code_base character varying(10),
    github_commit_id character varying(7),
    version_num character varying(50),
    description character varying(300)
);


ALTER TABLE public.code_versions OWNER TO router;

--
-- Name: TABLE code_versions; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON TABLE public.code_versions IS 'Provides details of the version of code used by the router under test, which is useful to relate changes in test results to code changes over time.';


--
-- Name: COLUMN code_versions.code_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.code_versions.code_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN code_versions.code_base; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.code_versions.code_base IS 'A general identifier for major code changes, e.g., RP (Route Planner),  NG (Route Planner Next Generation), onRoute (onRouteBC work). ';


--
-- Name: COLUMN code_versions.github_commit_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.code_versions.github_commit_id IS 'The first 7 digits of the GitHub commit ID, which is useful to identify the exact version of the code, e.g. e60a7cd.';


--
-- Name: COLUMN code_versions.version_num; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.code_versions.version_num IS 'The Maven version number of the code (available from the API response), e.g., 2.1.8, 2.2.1-SNAPSHOT.';


--
-- Name: COLUMN code_versions.description; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.code_versions.description IS 'A description of the changes made in the code version.';


--
-- Name: code_versions_code_id_seq; Type: SEQUENCE; Schema: public; Owner: router
--

CREATE SEQUENCE public.code_versions_code_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.code_versions_code_id_seq OWNER TO router;

--
-- Name: code_versions_code_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: router
--

ALTER SEQUENCE public.code_versions_code_id_seq OWNED BY public.code_versions.code_id;


--
-- Name: datasets; Type: TABLE; Schema: public; Owner: router
--

CREATE TABLE public.datasets (
    dataset_id integer NOT NULL,
    bc_subset_ind boolean,
    road_source character varying(10),
    road_network_timestamp timestamp with time zone,
    description character varying(240)
);


ALTER TABLE public.datasets OWNER TO router;

--
-- Name: TABLE datasets; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON TABLE public.datasets IS 'Describes a Routable BC dataset based on the data pieces that went into it, with the purpose being to track the data that was used by the router under test. This is useful for comparisons between the same version of data in different environments or different versions of data in the same environment.';


--
-- Name: COLUMN datasets.dataset_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.datasets.dataset_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN datasets.bc_subset_ind; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.datasets.bc_subset_ind IS 'Indicates whether the data is a subset of BC, i.e., true (subset of BC), false (all of BC).';


--
-- Name: COLUMN datasets.road_source; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.datasets.road_source IS 'The source of the road data, e.g., ITN (Integration Transporation Network). Road source is usually ITN. Some early tests were made using Google and Translink CVRP (Commercial Vehicle Route Planner) road data.';


--
-- Name: COLUMN datasets.road_network_timestamp; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.datasets.road_network_timestamp IS 'The vintage of the road data, defined by the ITN version timestamp. Historically, for monthly data releases this has usually been the first day of the month that the ITN data was released.';


--
-- Name: COLUMN datasets.description; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.datasets.description IS 'A description of what additional data was included, e.g., Truck Notices, RDM. This is mostly relevant the first time such additional data is added to the Router.';


--
-- Name: datasets_dataset_id_seq; Type: SEQUENCE; Schema: public; Owner: router
--

CREATE SEQUENCE public.datasets_dataset_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.datasets_dataset_id_seq OWNER TO router;

--
-- Name: datasets_dataset_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: router
--

ALTER SEQUENCE public.datasets_dataset_id_seq OWNED BY public.datasets.dataset_id;


--
-- Name: environments; Type: TABLE; Schema: public; Owner: router
--

CREATE TABLE public.environments (
    env_id integer NOT NULL,
    platform character varying(50),
    environment character varying(50),
    base_api_url character varying(100),
    api_key character varying(50),
    usable_as_map_platform_ind boolean
);


ALTER TABLE public.environments OWNER TO router;

--
-- Name: TABLE environments; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON TABLE public.environments IS 'Describes the specific router instance under test.';


--
-- Name: COLUMN environments.env_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.environments.env_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN environments.platform; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.environments.platform IS 'The platform that the test is run against, e.g., Devel, Deliv, Test, Prod, data integration.';


--
-- Name: COLUMN environments.environment; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.environments.environment IS 'The environment where the router is under test, e.g., DataBC, Translink, Refractions, Google.';


--
-- Name: COLUMN environments.base_api_url; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.environments.base_api_url IS 'The base URL of the router API under test.';


--
-- Name: COLUMN environments.api_key; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.environments.api_key IS 'The API key to access the router.';


--
-- Name: COLUMN environments.usable_as_map_platform_ind; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.environments.usable_as_map_platform_ind IS 'Indicates whether the environment is suitable for use in mapping, i.e., true (yes), false (no).';


--
-- Name: environments_env_id_seq; Type: SEQUENCE; Schema: public; Owner: router
--

CREATE SEQUENCE public.environments_env_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.environments_env_id_seq OWNER TO router;

--
-- Name: environments_env_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: router
--

ALTER SEQUENCE public.environments_env_id_seq OWNED BY public.environments.env_id;


--
-- Name: results; Type: TABLE; Schema: public; Owner: router
--

CREATE TABLE public.results (
    result_id integer NOT NULL,
    run_id integer,
    test_id integer,
    calc_time real,
    distance real,
    route_geometry public.geometry(Geometry,4326),
    duration real,
    partition_info jsonb
);


ALTER TABLE public.results OWNER TO router;

--
-- Name: TABLE results; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON TABLE public.results IS 'Records the results of a single test within a test run. ';


--
-- Name: COLUMN results.result_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.results.result_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN results.run_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.results.run_id IS 'Identifies the test run information used to generate the result. It is a foreign key reference to the runs table.';


--
-- Name: COLUMN results.test_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.results.test_id IS 'Identifies the test case used to generate the result. It is a foreign key reference to the tests table.';


--
-- Name: COLUMN results.calc_time; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.results.calc_time IS 'The request execution time (in milliseconds) returned by the router for the test.';


--
-- Name: COLUMN results.distance; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.results.distance IS 'The travel distance (in kilometres) returned by the router for the test. ';


--
-- Name: COLUMN results.route_geometry; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.results.route_geometry IS 'The linestring geometry of the route returned by the router.';


--
-- Name: COLUMN results.duration; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.results.duration IS 'The travel time (in seconds) returned by the router for the test.';


--
-- Name: COLUMN results.partition_info; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.results.partition_info IS 'The parition info is a set of json objects each with three key value pairs: index, distance and partition type/value. The index value represents the route node index where the partition starts.  The distance value is a measure of the length of the partition in kilometres. The partition key is a string label of the type, and its value represents the state or name of the partition.  Example: [{"index": "0", "distance": "", "isTruckRoute": false}, {"index": "28", "distance": "", "isTruckRoute": true}]';


--
-- Name: results_result_id_seq; Type: SEQUENCE; Schema: public; Owner: router
--

CREATE SEQUENCE public.results_result_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.results_result_id_seq OWNER TO router;

--
-- Name: results_result_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: router
--

ALTER SEQUENCE public.results_result_id_seq OWNED BY public.results.result_id;


--
-- Name: runs; Type: TABLE; Schema: public; Owner: router
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


ALTER TABLE public.runs OWNER TO router;

--
-- Name: TABLE runs; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON TABLE public.runs IS 'Defines a test run as a group of tests with a specific environment, dataset, and code version. The group of tests to run is specified using the group_name.';


--
-- Name: COLUMN runs.run_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.run_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN runs.description; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.description IS 'A description of the test run, explaining why it is performed.';


--
-- Name: COLUMN runs.dataset_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.dataset_id IS 'Identifies the dataset for the test run. It is a foreign key reference to the datasets table.';


--
-- Name: COLUMN runs.code_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.code_id IS 'Identifies the version of code for the test run. It is a foreign key reference to the code_versions table.';


--
-- Name: COLUMN runs.forward_route_ind; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.forward_route_ind IS 'Indicates whether the test run is in the forward or reverse direction, which his applied to all tests in the group, i.e., true (forward), false (reverse).';


--
-- Name: COLUMN runs.run_timestamp; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.run_timestamp IS 'The date and time the test run was performed.';


--
-- Name: COLUMN runs.group_name; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.group_name IS 'The name of the test group for the test(s) that are being run.';


--
-- Name: COLUMN runs.env_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.env_id IS 'Identifies the environment for the test run. It is a foreign key reference to the environments table.';


--
-- Name: COLUMN runs.parameters; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.parameters IS 'A JSON object of router parameter names and values, to be applied to each test run in the test group. However, parameters defined for a test will override any same-named run parameters.';


--
-- Name: COLUMN runs.queued_timestamp; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.queued_timestamp IS 'The date and time when the run was queued to be run.';


--
-- Name: COLUMN runs.status; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.runs.status IS 'The status of the run, i.e., queued, running, complete.';


--
-- Name: runs_run_id_seq; Type: SEQUENCE; Schema: public; Owner: router
--

CREATE SEQUENCE public.runs_run_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.runs_run_id_seq OWNER TO router;

--
-- Name: runs_run_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: router
--

ALTER SEQUENCE public.runs_run_id_seq OWNED BY public.runs.run_id;


--
-- Name: tests; Type: TABLE; Schema: public; Owner: router
--

CREATE TABLE public.tests (
    test_id integer NOT NULL,
    description character varying(240),
    group_name character varying(50),
    result_id_fwd_ref integer,
    notes character varying(300),
    good_demo_case_ind boolean,
    points character varying(400),
    parameters json,
    created_timestamp timestamp with time zone
);


ALTER TABLE public.tests OWNER TO router;

--
-- Name: TABLE tests; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON TABLE public.tests IS 'Defines a single routing test case, including the origin point, any number of waypoints, and the destination point, as well as specific router parameters and descriptive information about the test.';


--
-- Name: COLUMN tests.test_id; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.tests.test_id IS 'A system generated unique identification number, used as the primary key identifier.';


--
-- Name: COLUMN tests.description; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.tests.description IS 'A description of the purpose of the test.';


--
-- Name: COLUMN tests.group_name; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.tests.group_name IS 'The name of the test group that the test is assigned to, e.g., Custom, Generated Site, Generated Muni. ';


--
-- Name: COLUMN tests.result_id_fwd_ref; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.tests.result_id_fwd_ref IS 'An optional reference to a result (run in the forward direction) which is considered to be a standard for comparison against other results (run in the forward direction) of the test case.';


--
-- Name: COLUMN tests.result_id_rev_ref; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.tests.result_id_rev_ref IS 'An optional reference to a result (run in the reverse direction) which is considered to be a standard for comparison against other results (run in the reverse direction) of the test case.';


--
-- Name: COLUMN tests.notes; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.tests.notes IS 'Additional descriptive notes about the test.';


--
-- Name: COLUMN tests.good_demo_case_ind; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.tests.good_demo_case_ind IS 'Indicates whether the test is an interesting case for demonstrations, i.e., true (good test case for demonstrations), false.';


--
-- Name: COLUMN tests.points; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.tests.points IS 'A list of origin point, any number of waypoints, and destination point, in the same format as the router points parameter (lon1,lat1,lon2,lat2,...), e.g.,-121.768099,49.2374933,-119.2702664,52.8281629,-129.9980382,58.438932.';


--
-- Name: COLUMN tests.parameters; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.tests.parameters IS 'A JSON object of router parameter names and values, to be included as part of the test request. Test parameters override any same-named parameters defined for a run.';


--
-- Name: COLUMN tests.created_timestamp; Type: COMMENT; Schema: public; Owner: router
--

COMMENT ON COLUMN public.tests.created_timestamp IS 'The date and time the test was created.';


--
-- Name: tests_backup; Type: TABLE; Schema: public; Owner: router
--

CREATE TABLE public.tests_backup (
    test_id integer,
    longitude1 double precision,
    latitude1 double precision,
    longitude2 double precision,
    latitude2 double precision,
    description character varying(240),
    group_name character varying(50),
    enable_string character varying(30),
    height real,
    length real,
    width real,
    weight integer,
    follow_truck_route boolean,
    criteria character varying(12),
    fwd_ref_result_id integer,
    rev_ref_result_id integer,
    start_time character varying(30),
    notes character varying(300),
    good_demo_case boolean,
    correct_side_routing boolean,
    long_lat_list character varying(200),
    truck_route_multiplier integer,
    partition character varying(50),
    extra_parameters character varying(1000)
);


ALTER TABLE public.tests_backup OWNER TO router;

--
-- Name: tests_test_id_seq; Type: SEQUENCE; Schema: public; Owner: router
--

CREATE SEQUENCE public.tests_test_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tests_test_id_seq OWNER TO router;

--
-- Name: tests_test_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: router
--

ALTER SEQUENCE public.tests_test_id_seq OWNED BY public.tests.test_id;


--
-- Name: code_versions code_id; Type: DEFAULT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.code_versions ALTER COLUMN code_id SET DEFAULT nextval('public.code_versions_code_id_seq'::regclass);


--
-- Name: datasets dataset_id; Type: DEFAULT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.datasets ALTER COLUMN dataset_id SET DEFAULT nextval('public.datasets_dataset_id_seq'::regclass);


--
-- Name: environments env_id; Type: DEFAULT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.environments ALTER COLUMN env_id SET DEFAULT nextval('public.environments_env_id_seq'::regclass);


--
-- Name: results result_id; Type: DEFAULT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.results ALTER COLUMN result_id SET DEFAULT nextval('public.results_result_id_seq'::regclass);


--
-- Name: runs run_id; Type: DEFAULT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.runs ALTER COLUMN run_id SET DEFAULT nextval('public.runs_run_id_seq'::regclass);


--
-- Name: tests test_id; Type: DEFAULT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.tests ALTER COLUMN test_id SET DEFAULT nextval('public.tests_test_id_seq'::regclass);


--
-- Name: code_versions code_versions_pkey; Type: CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.code_versions
    ADD CONSTRAINT code_versions_pkey PRIMARY KEY (code_id);


--
-- Name: datasets datasets_pkey; Type: CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.datasets
    ADD CONSTRAINT datasets_pkey PRIMARY KEY (dataset_id);


--
-- Name: environments environments_pkey; Type: CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.environments
    ADD CONSTRAINT environments_pkey PRIMARY KEY (env_id);


--
-- Name: results results_pkey; Type: CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.results
    ADD CONSTRAINT results_pkey PRIMARY KEY (result_id);


--
-- Name: runs runs_pkey; Type: CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.runs
    ADD CONSTRAINT runs_pkey PRIMARY KEY (run_id);


--
-- Name: tests tests_pkey; Type: CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.tests
    ADD CONSTRAINT tests_pkey PRIMARY KEY (test_id);


--
-- Name: results results_run_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.results
    ADD CONSTRAINT results_run_id_fkey FOREIGN KEY (run_id) REFERENCES public.runs(run_id);


--
-- Name: results results_test_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.results
    ADD CONSTRAINT results_test_id_fkey FOREIGN KEY (test_id) REFERENCES public.tests(test_id);


--
-- Name: runs runs_code_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.runs
    ADD CONSTRAINT runs_code_id_fkey FOREIGN KEY (code_id) REFERENCES public.code_versions(code_id);


--
-- Name: runs runs_dataset_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.runs
    ADD CONSTRAINT runs_dataset_id_fkey FOREIGN KEY (dataset_id) REFERENCES public.datasets(dataset_id);


--
-- Name: runs runs_env_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: router
--

ALTER TABLE ONLY public.runs
    ADD CONSTRAINT runs_env_id_fkey FOREIGN KEY (env_id) REFERENCES public.environments(env_id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: pg_database_owner
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--


