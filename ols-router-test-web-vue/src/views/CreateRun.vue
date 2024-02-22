<template>
    <div>
      
    &nbsp
      <form @submit.prevent="submitForm">
        <table class="table table-striped">
          <tbody>
            <td colspan="2" class="headerColour"><h2>Create New Run</h2></td>
            <tr>
              <td><label for="description">Description:</label></td>
              <td><input type="text" v-model="run.description"  ></td>
            </tr>
            <tr>
              <td><label for="groupName">Group Name:</label></td>
              <td>
                <select v-model="run.groupName" @change="handleGroupNameChange">
                    <option v-for="option in groupNameOptions" :key="option.value" :value="option.value" >
                        {{ option.label }}
                    </option>
                </select>
              </td>
            </tr>
            <tr>
              <td><label for="environmentId">Environment:</label><p class="smallText">(This WILL determine which server router requests are sent to)</p></td>
              <td>
                <select v-model="run.environmentId">
                  <option v-for="env in environments" :key="env.environmentId" :value="env.environmentId">{{ env.environment + " - " + env.platform}}</option>
                </select>
                <div class="errortext" v-if="!run.environmentId && dirty">Cannot be blank.</div>
              </td>
            </tr>
            <tr>
              <td><label for="codeId">Code:</label><p class="smallText">(This doesn't select or change the code used in the test)</p></td>
              <td>
                <select v-model="run.codeId">
                  <option v-for="code in codeVersions" :key="code.codeId" :value="code.codeId">{{ code.versionNum +" - " + code.description}}</option>
                </select>
                <div class="errortext" v-if="!run.codeId && dirty">Cannot be blank.</div>
              </td>
            </tr>
            <tr>
              <td><label for="datasetId">Dataset:</label><p class="smallText">(This doesn't select or change the dataset used in the test)</p></td>
              <td>
                <select v-model="run.datasetId">
                  <option v-for="dataset in datasets" :key="dataset.datasetId" :value="dataset.datasetId">{{ dataset.roadSource + ' - ' + dataset.roadNetworkTimestamp }}</option>
                </select>
                <div class="errortext" v-if="!run.datasetId && dirty">Cannot be blank.</div>
              </td>
            </tr>
            <tr>
              <td><label for="forwardRouteInd">Forward Route:</label></td>
              <td><input type="checkbox" v-model="run.forwardRouteInd"></td>
            </tr>

            <tr v-if="run.groupName !== 'Custom'">
                <td><label for="testCaseUrl">Use Parameters <br>(paste a Router URL here w/params ):</label></td>
                <td><input type="text" v-model="testCaseUrl" size=50 required>
                    <div class="errortext" v-if="!isValidRouterUrl(testCaseUrl) && dirty">
                    Invalid URL. Must contain 'http:// or 'https://'' as well as 'routeParams=' .</div>
                </td>
            </tr>
            <tr>
              <td colspan="2" ><button type="submit" :disabled="!isFormValid">Create New Run for Router Testing</button></td>
            </tr>
          </tbody>
        </table>
        
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import Shared from "../shared.js";

  export default {
    extends: Shared,
    data() {
      return {
        run: {
          description: '',
          groupName: 'Custom',
          environmentId: null,
          codeId: null,
          datasetId: null,
          forwardRouteInd: true,
          runTimestamp: '', //not set here
          queuedTimestamp: '', // This will be set on form submission
          status: 'queued', //we always set it to queued when creating it, the testing engine searched for this status to find new things to run.
          parameters: '',
        },
        testCaseUrl: 'https://office.refractions.net/~chodgson/gc/ols-demo/?rt=local&routeParams={"points":"-123,48,-123,48","criteria":"fastest","enable":"tr,xc,tc,"}',
        selectedFields: [],
        dirty: false,
        datasets: [],
        environments: [],
        groupNameOptions: [],
        codeVersions: []

      };
    },
    watch: {
        testCaseUrl(newValue, oldValue){
            this.dirty = true;
        },
        'run.groupName'(newValue, oldValue){
            if (newValue === 'Custom'){
                this.testCaseUrl= 'https://office.refractions.net/~chodgson/gc/ols-demo/?rt=local&routeParams={"points":"-123,48,-123,48","criteria":"fastest","enable":"tr,xc,tc,"}'
            }
        },
        'run.datasetId'(newValue, oldValue){
            this.dirty = true;
        },
    },
    computed: {
      isFormValid(){
        return (
          this.run.codeId &&
          this.run.environmentId &&
          this.run.groupName &&
          this.run.datasetId &&
          this.isValidRouterUrl(this.testCaseUrl)
        );
      }
    },
    mounted() {
      // Fetch options for environmentId, codeId, and datasetId
      this.fetchEnvironments();
      this.fetchCodes();
      this.fetchDatasets();
      this.fetchGroupNames();
    },
    methods: {
      handleGroupNameChange() {
        // Reset values when groupName changes
        if (this.run.groupName === 'Custom') {
          this.run.environmentId = null;
          this.run.codeId = null;
          this.run.datasetId = null;
        }
      },
      isValidRouterUrl(url){
        // Define a regular expression to check for a valid URL
        const urlRegex = /^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$/;
        const routeParamsRegex = /[?&]routeParams=/;

        //text must meet both requirements to be valid
        return urlRegex.test(url) && routeParamsRegex.test(url);
      },
      submitForm() {
        // Set queuedTimestamp to the current time on form submission
        this.run.queuedTimestamp = new Date().toISOString();

        const url = new URL(this.testCaseUrl);
        const routeParamsString = url.searchParams.get('routeParams');

        // Decode and parse the routeParams JSON
        const decodedRouteParams = decodeURIComponent(routeParamsString);
        const parsedRouteParams = JSON.parse(decodedRouteParams);

        // Extract 'points' and the rest of the routeParams
        this.run.parameters = { ...parsedRouteParams };
        delete this.run.parameters.points;

  
        console.log('Creating Dataset:', this.run);
        axios.post(this.ApiUrl + '/createRun', this.run)
            .then(response => {
            console.log('New Run created successfully:', response.data);
            window.alert('New Run successfully queued');
            })
            .catch(error => {
            console.error('Error creating Run:', error);
            window.alert('Error creating new Run');
            });
        
      },
      fetchEnvironments() {
          axios
          .get(this.ApiUrl + '/environments')
          .then(response => {
            this.environments = response.data
            });
        },
        fetchDatasets() {
          axios
          .get(this.ApiUrl + '/datasets')
          .then(response => {
            this.datasets = response.data
            });
        },
        fetchCodes() {
          axios
          .get(this.ApiUrl + '/codeVersions')
          .then(response => {
            this.codeVersions = response.data
            });
        },
        fetchGroupNames(){
          axios
          .get(this.ApiUrl + '/groupNameOptions')
          .then(response => {
            this.results = response.data
            response.data.forEach(item => {
              var option = { value: item.groupName, label: item.groupName }
              this.groupNameOptions.push(option)
            });
  
            var allOption = { value: 'ALL', label: 'ALL' }
            this.groupNameOptions.push(allOption)
          })
        },        
    },
  };
  </script>
  