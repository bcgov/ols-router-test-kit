# ols-router-test-web-vue

This is the Vue.js frontend website that shows users results out of the router test database and allows them to create a new test "run" as well as various other tools.

## Recommended IDE Setup
[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur) + [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin).

We also use Node.js so ensure that is installed and get the latest version from the node.js site if not:
npm -v

Install the Axios library as we use that for web requests in this project, to install run:
npm i axios



## Customize configuration

See [Vite Configuration Reference](https://vitejs.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

Vite requires node 14 or above to run.


```sh
npm run dev
```

### Compile and Minify for Production

```sh
npm run build
```

Configuration to setup the servers you wish to access for this instance are in shared.js:

baseUrl:"http://office.refractions.net/~chodgson/gc/ols-demo/index.html",
ApiUrl: "http://localhost:8080",


The first is the location of the geocoder demo/map app where links to display routes on a map will be shown. Ensure that server is also using the same backend database as the old-test-web application is using.

The second, apiUrl, is the location of the old-router-test-web server which provides the API calls we use to populate most of this site.



to update pg_hba.conf with

# TYPE  DATABASE        USER            ADDRESS                 METHOD
local   all             all                                     trust
local   all "postgres" peer
hostssl replication "_crunchyrepl" all cert
hostssl "postgres" "_crunchyrepl" all cert
host all "_crunchyrepl" all reject
hostssl all "_crunchypgbouncer" all scram-sha-256
host all "_crunchypgbouncer" all reject
host all all 0.0.0.0/0 md5

then run in psql: 
SELECT pg_reload_conf();

alternatively we can do:
SET ROLE ols_router_test_kit_owner;
