<template>
  <main class="container">
  &nbsp
    <form @submit.prevent="createTest">
      <table class="table table-striped ">
        <!-- Create Test Form -->
        <tr>
          <td colspan="2" class="headerColour"><h2>Create a New Test</h2></td>
        </tr>
        <tr>
          <td><label for="description">Description:</label></td>
          <td><input v-model="testData.description" type="text" size="50"/>
            <div class="errortext" v-if="!testData.description && testDirty">
            Cannot be blank.</div>
          </td>
        </tr>
        <tr>
          <td><label for="groupName">Group Name:</label></td>
          <td><select v-model="testData.groupName" id="selectOption">
            <option v-for="option in groupNameOptions" :key="option.value" :value="option.value" >
              {{ option.label }}
            </option>
            </select>
          </td>

        </tr>
        <tr>
          <td><label for="notes">Notes:</label></td>
          <td><input v-model="testData.notes" type="text" size="50"/>
          </td>
        </tr>
        <tr>
          <td><label for="goodDemoCaseInd">Good Demo Case Indicator:</label></td>
          <td><input v-model="testData.goodDemoCaseInd" type="checkbox" /></td>
        </tr>
        <tr>
          <td><label for="testCaseUrl">Paste the Router Test URL here:</label></td>
          <td><input v-model="testCaseUrl" type="text" size="50"/>
            <div class="errortext" v-if="!isValidRouterUrl(testCaseUrl) && testDirty">
            Invalid URL. Must contain 'http:// or 'https://'' as well as 'routeParams=' .</div>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <button type="submit" :disabled="!isTestFormValid">Create New Test</button>
          </td>
        </tr>
      </table>
    </form>



    <div>&nbsp</div>
    <form @submit.prevent="createEnvironment">
    <table class="table table-striped">
      <!-- Create Environment Form -->
      <tr>
        <td colspan="2" class="headerColour"><h2>Create a New Environment</h2> 
          (The exact URL and API Key provided here will be used to send test requests)<br>
          <router-link :to="{name:'list',params:{objectName:'environments'}}"> (View existing Environments)</router-link></td>
      </tr>
      <tr>
        <td><label for="platform">Platform:</label></td>
        <td><input v-model="environmentData.platform" type="text" size="50" required />
          <div class="errortext" v-if="!environmentData.platform && envDirty">
          Cannot be blank.</div>
        </td>
      </tr>
      <tr>
        <td><label for="environment">Environment Name:</label></td>
        <td><input v-model="environmentData.environment" type="text" size="50" required /><div class="errortext" v-if="!environmentData.environment && envDirty">
          Cannot be blank.</div></td>
      </tr>
      <tr>
        <td><label for="baseApiUrl">Base API URL:</label> </td>
        <td><input v-model="environmentData.baseApiUrl" type="text" size="50" required />
        <div class="errortext" v-if="!isValidUrl(environmentData.baseApiUrl) && envDirty">
          Please enter a valid URL format for the Base API URL.
        </div>
        </td>
      </tr>
      <tr>
        <td><label for="apiKey">API Key:</label></td>
        <td><input v-model="environmentData.apiKey" size="50" type="text"/></td>
      </tr>
      <tr>
        <td colspan="2"><button type="submit" :disabled="!isEnvFormValid">Create Environment</button></td>
        
      </tr>

      </table>
    </form>
      &nbsp
    <form @submit.prevent="createDataset">
      <table class="table table-striped">
      <!-- Create Dataset Form -->
      <tr>
        <td colspan="2" class="headerColour"><h2>Create a New Dataset</h2>
          (These values are not used to select a router or dataset version to send requests to, they are only for reference)<br>
          <router-link :to="{name:'list',params:{objectName:'datasets'}}"> (View existing Datasets)</router-link></td>
      </tr>
      <tr>
        <td><label for="isBcSubsetInd">Is BC Subset:</label></td>
        <td><input v-model="datasetData.isBcSubsetInd" type="checkbox" /></td>
      </tr>
      <tr>
        <td><label for="roadSource">Road Source:</label></td>
        <td><input v-model="datasetData.roadSource" type="text" size="50" required maxlength="10"/>
          <div class="errortext" v-if="!datasetData.roadSource && datasetDirty">
          Cannot be blank.</div>
        </td>
      </tr>
      <tr>
        <td><label for="roadNetworkTimestamp">Road Network Timestamp:</label></td>
        <td>
            <flat-pickr v-model="selectedDate" class="form-control" placeholder="Select Date"></flat-pickr>
            &nbsp
            <label for="selectedTime">Time:</label>
            &nbsp
            <input v-model="selectedTime" type="time" required />

          <input v-model="datasetData.roadNetworkTimestamp" type="hidden" required /><div class="errortext" v-if="!datasetData.roadNetworkTimestamp && datasetDirty">
          &nbsp
          Pick a date and time</div>
        </td>
      </tr>
      <tr>
        <td><label for="description">Description:</label></td>
        <td><input v-model="datasetData.description" type="text" size="50" required /><div class="errortext" v-if="!datasetData.description && datasetDirty">
          Cannot be blank.</div></td>
      </tr>
      <tr>
        <td colspan="2" ><button type="submit" :disabled="!isDatasetFormValid">Create Dataset</button></td>
      </tr>
    </table>
  </form>

  &nbsp
  <form @submit.prevent="createCodeVersion">
  <table class="table table-striped">
      <tr>
        <td colspan="2" class="headerColour"><h2>Create a New Code Version</h2>
          (These values are not used to select a router or code version to send requests to, they are only for reference)<br>
          <router-link :to="{name:'list',params:{objectName:'codeVersions'}}"> (View existing Code Versions)</router-link></td>
      </tr>
      <tbody>
        <tr>
          <td><label for="githubCommitId">Github Commit ID</label></td>
          <td><input id="githubCommitId" v-model="codeForm.githubCommitId" type="text" />
            <div class="errortext" v-if="!codeForm.githubCommitId && codeVersionDirty">
            Cannot be blank.</div>
          </td>
        </tr>
        <tr>
          <td><label for="versionNum">Version Number</label></td>
          <td><input id="versionNum" v-model="codeForm.versionNum" type="text" />
            <div class="errortext" v-if="!codeForm.versionNum && codeVersionDirty">
            Cannot be blank.</div>
          </td>
        </tr>
        <tr>
          <td><label for="description">Description</label></td>
          <td><input id="description" v-model="codeForm.description" type="text" />
            <div class="errortext" v-if="!codeForm.description && codeVersionDirty">
            Cannot be blank.</div>
          </td>
        </tr>
        <tr>
        <td colspan="2"><button type="submit" :disabled="!isCodeFormValid">Create Code Version</button></td>
      </tr>
      </tbody>
    </table>
    </form>
    <br>
  </main>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';
import VueFlatpickr from 'vue-flatpickr-component';
import 'flatpickr/dist/flatpickr.css';
import Shared from "../shared.js";


export default {
  extends: Shared,
  components: {
    flatPickr: VueFlatpickr,
  },
  data() {
    return {
      environmentData: {
        platform: '',
        environment: '',
        baseApiUrl: '',
        apiKey: '',
      },
      datasetData: {
        isBcSubsetInd: false,
        roadSource: '',
        roadNetworkTimestamp: '',
        description: '',
      },
      testData: {
        description: '',
        groupName: 'Custom',
        resultIdFwdRef: null,
        resultIdRevRef: null,
        notes: '',
        goodDemoCaseInd: false,
        points: '',
        parameters: '',
      },
      codeForm: {
        githubCommitId: '',
        versionNum: '',
        description: '',
      },
      testCaseUrl: "",
      selectedDate: null,
      selectedTime: null,
      envDirty: false,
      datasetDirty: false,
      testDirty: false,
      codeVersionDirty:false,
      groupNameOptions: [],
    };
  },
  watch: {
    'environmentData.baseApiUrl'(newValue, oldValue){
      this.envDirty = true;
    },
    'datasetData.description'(newValue, oldValue){
      this.datasetDirty = true;
    },
    'codeForm.description'(newValue, oldValue){
      this.codeVersionDirty = true;
    },
    testCaseUrl(){
      this.testDirty = true;
    },
    selectedDate() {
      this.UpdateZonedDateTime();
    },
    selectedTime() {
      this.UpdateZonedDateTime();
    },
    
  },
  computed: {
    isEnvFormValid() {
      return (
        this.environmentData.platform &&
        this.environmentData.environment &&
        this.isValidUrl(this.environmentData.baseApiUrl)
      );
    },
    isDatasetFormValid() {
      return (
        this.datasetData.roadSource &&
        this.datasetData.roadNetworkTimestamp && this.isValidIsoStringTimestamp(this.datasetData.roadNetworkTimestamp)&&
        this.datasetData.description
      );
    },
    isTestFormValid(){
      return (
        this.testData.description &&
        this.testData.groupName &&
        this.isValidRouterUrl(this.testCaseUrl)
      );
    },
    isCodeFormValid(){
      //check for any blank values
      return !Object.values(this.codeForm).some(value => !value);
    },
    
  },
  methods: {
    createEnvironment() {
      console.log('Creating Environment:', this.environmentData);
      axios.post(this.ApiUrl + '/createEnvironment', this.environmentData)
        .then(response => {
          console.log('Environment created successfully:', response.data);
          window.alert('Environment created successfully');
        })
        .catch(error => {
          console.error('Error creating environment:', error);
          window.alert('Error creating new environment');
        });
    },
    createDataset() {
      console.log('Creating Dataset:', this.datasetData);
      axios.post(this.ApiUrl + '/createDataset', this.datasetData)
        .then(response => {
          console.log('Dataset created successfully:', response.data);
          window.alert('Dataset created successfully');
        })
        .catch(error => {
          console.error('Error creating dataset:', error);
          window.alert('Error creating new dataset');
        });
    },
    createTest(){
      console.log('Creating Test:', this.datasetData);

      try{
        const url = new URL(this.testCaseUrl);
        const routeParamsString = url.searchParams.get('routeParams');

        // Decode and parse the routeParams JSON
        const decodedRouteParams = decodeURIComponent(routeParamsString);
        const parsedRouteParams = JSON.parse(decodedRouteParams);

        // Extract 'points' and the rest of the routeParams
        this.testData.points = parsedRouteParams.points;
        this.testData.parameters = { ...parsedRouteParams };
        delete this.testData.parameters.points;
      }catch(error){
        window.alert('Error parsing your URL:' + error);
      }
      axios.post(this.ApiUrl + '/createTest', this.testData)
        .then(response => {
          console.log('Test created successfully:', response.data);
          window.alert('Test created successfully');
        })
        .catch(error => {
          console.error('Error creating Test:', error);
          window.alert('Error creating new Test');
        });
    },
    createCodeVersion(){
      console.log('Creating Dataset:', this.codeForm);
      axios.post(this.ApiUrl + '/createCodeVersion', this.codeForm)
        .then(response => {
          console.log('Code Version created successfully:', response.data);
          window.alert('Code Version created successfully');
        })
        .catch(error => {
          console.error('Error creating Code Version:', error);
          window.alert('Error creating new Code Version');
        });
    },
    isValidUrl(url) {
      // Regular expression to validate a URL
      const urlRegex = /^(ftp|http|https):\/\/[^ "]+$/;
      return urlRegex.test(url);
    },
    isValidRouterUrl(url){
      // Define a regular expression to check for a valid URL
      const urlRegex = /^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$/;
      const routeParamsRegex = /[?&]routeParams=/;

      //text must meet both requirements to be valid
      return urlRegex.test(url) && routeParamsRegex.test(url);
    },
    clearEnvForm() {
      this.environmentData.platform = '';
      this.environmentData.environment = '';
      this.environmentData.baseApiUrl = '';
      this.environmentData.apiKey = '';
    },
    clearDatasetForm() {
      this.datasetData.roadSource = '';
      this.datasetData.roadNetworkTimestamp = '';
      this.datasetData.description = '';
      this.datasetData.isBcSubsetInd = false;
    },
    UpdateZonedDateTime() {
      if (this.selectedDate && this.selectedTime) {
        const dateTimeString = `${this.selectedDate}T${this.selectedTime}:00`;
        this.datasetData.roadNetworkTimestamp = new Date(dateTimeString).toISOString();
      }
    },
    isValidIsoStringTimestamp(timestamp) {
      const parsedDate = new Date(timestamp);
      return !isNaN(parsedDate.getTime());
    }
  },
  mounted(){
    axios
        .get(this.ApiUrl + '/groupNameOptions')
        .then(response => {
          this.results = response.data
          response.data.forEach(item => {
            var option = { value: item.groupName, label: item.groupName }
            this.groupNameOptions.push(option)
          });

          var allOption = { value: 'All', label: 'All' }
          this.groupNameOptions.push(allOption)
        })
    
  }
};
</script>