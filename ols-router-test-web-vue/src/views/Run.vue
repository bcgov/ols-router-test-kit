<template>
  <main class="container">
    <div class="p-1"> Details for Run #{{this.run.runId}}</div>
    <table class="table table-striped">
      <tr>
        <td>Description:</td>
        <td>{{ run.description }}</td>
      </tr>

      <tr>
        <td>Status:</td>
        <td>{{ run.status }}</td>
      </tr>

      <tr>
        <td>Group Name:</td>
        <td>{{ run.groupName }}</td>
      </tr>

      <tr>
        <td>Run Timestamp:</td>
        <td>{{ formatDate(run.runTimestamp) }}</td> 
      </tr>

      <tr>
        <td>Environment Name:</td>
        <td>{{ environment.platform + " ("+ environment.baseApiUrl + ") - " + environment.environment }}</td>
      </tr>

      <tr>
        <td>Data Set:</td>
        <td>{{ dataset.roadSource + " - " + formatDate(dataset.roadNetworkTimestamp) + " - " + dataset.description }}</td>
      </tr>
      
      <tr>
        <td>Code Version:</td>
        <td>{{ codeVersion.versionNum }}</td>
      </tr>
      <tr>
        <td>Direction:</td>
        <td><p v-if=run.forwardRouteInd > Forward</p>
            <p v-else> Backward</p>
        </td>
      </tr>

      <tr v-for="(p, name, i) in run.parameters"  >
        <td>{{ name }}</td>
        <td class="">{{ p }}</td>
      </tr>
    </table>


    <div class="p-1 fw-bold border-bottom mb-2">Results for Run Id: {{ runId }} </div>
    <div>Table Description: A list of all results that are associated to this Run Id. Click the "See on Map" button to review the calculated route. <p>The Partition Signature is encoded data describing how the route uses truck routes(1's) and non-truck routes(0's).  e.g. "010" means the route started as non-truck, went onto a truck route, then finished on a non-truck route.</p></div>
    <div> &nbsp</div>
    <div> Displaying Rows {{ ((pageNum-1) * perPage)+1 }} to {{ ((pageNum-1) * perPage) + curPageCount }} out of {{rowCount}} rows:</div>
    <table class="table table-striped table-sm">
      <tbody>
        <tr>
        <th class="thLink" @click="setSortBy('testId')"> Test ID 
          <template v-if="(this.sortBy === 'testId' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'testId' && !this.descending )">
              ▲
          </template>
        </th>
         <th class="thLink" @click="setSortBy('distance')"> Distance 
          <template v-if="(this.sortBy === 'distance' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'distance' && !this.descending )">
              ▲
          </template>
        </th>
        <th class="thLink" @click="setSortBy('duration')"> Duration 
          <template v-if="(this.sortBy === 'duration' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'duration' && !this.descending )">
              ▲
          </template>
        </th>
        <th class="thLink" @click="setSortBy('calcTime')"> Calc Time
          <template v-if="(this.sortBy === 'calcTime' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'calcTime' && !this.descending )">
              ▲
          </template>
        </th>
        <th class="thLink" @click="setSortBy('partitionSignature')"> Partition Signature
          <template v-if="(this.sortBy === 'partitionSignature' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'partitionSignature' && !this.descending )">
              ▲
          </template>
        </th>
        <th></th>

      </tr>
        <tr v-for="result in results" >  
          <td> <router-link :to="{name:'test',params:{testId:result.testId}}">{{ result.testId }} </router-link> </td>
          <td class="right"> {{ result.distance}} </td>
          <td class="right"> {{ Math.ceil(result.duration) }}</td>
          <td class="right"> {{ result.calcTime  }}</td>
          <td class="right"> {{ result.partitionSignature }} </td>
          <td><button @click="showOnMap(result.resultId, environment.platform)">Show on Map</button></td>
        </tr>
      </tbody>
    </table>

    <table class="table-noborder">
      <tr>
        <td class="textLink table-noborder" @click="pageNum=1;this.updateTable()">&lt&lt</td>&nbsp
        <td class="textLink table-noborder" @click="previousPage"> Prev </td>
        <td class="table-noborder">Rows Per Page: <input type="text" size=2 @input="perPageChanged" v-model="this.perPage" /></td>
        <td class="textLink table-noborder" @click="nextPage"> Next </td>&nbsp
        <td class="textLink table-noborder" @click="pageNum=maxPages;this.updateTable();">&gt&gt </td>
      </tr>
      <tr>
        <td class="table-noborder"></td>
        <td class="table-noborder">Jump To Page: <input type="text" size=2 @input="pageNumChanged" v-model="this.pageNum" /></td>
      </tr>
  </table>
  </main>
</template>

<script>
import axios from 'axios';
import Shared from "../shared.js";

export default {
  extends: Shared,
  props:{
    runId: Number
  },
  data(){
    return{
      run:{},
      environment:{},
      dataset:{},
      codeVersion:{},
      results:{},
      debounceTimeout: null,
    }

  },
  computed: {},
  methods: {
    initPage(){
      this.sortBy = "testId"
      axios
        .get(this.ApiUrl + '/run?runId=' + this.runId)
        .then(response =>{
          this.run = response.data
          if(this.run.environmentId){
            axios
              .get(this.ApiUrl + '/environment?envId=' + this.run.environmentId) 
              .then(response => (this.environment = response.data))
          }

          if(this.run.datasetId){
            axios
              .get(this.ApiUrl + '/dataset?datasetId=' + this.run.datasetId) 
              .then(response => (this.dataset = response.data))
          }

          if(this.run.codeId){
            axios
              .get(this.ApiUrl + '/codeVersion?codeId=' + this.run.codeId) 
              .then(response => (this.codeVersion = response.data))
          }
        })
    },
    updateTable() {
      // Clear the previous timeout if it exists
      if (this.debounceTimeout) {
        clearTimeout(this.debounceTimeout);
      }
      //console.log(shared.data().debouceTime); // Outputs: 700
      // Set a new timeout
      this.debounceTimeout = setTimeout(() => {
        // Call the function to update the table
        this.runUpdateTable();
      }, Shared.data().debouceTime); // Adjust the delay in shared.js
    },
    
    runUpdateTable(){
      
      var zeroBasePageNum = this.pageNum - 1
      axios
        .get(this.ApiUrl + '/results?filterColumn=runId&filterValue=' + this.runId + '&pageNumber=' + zeroBasePageNum + '&perPage=' + this.perPage + '&sortBy=' + this.sortBy + '&descending=' + this.descending)
        .then(response => {
          this.results = response.data 
          this.curPageCount = this.results.length 
        })


      axios
        .get(this.ApiUrl + '/resultsCount?filterColumn=runId&filterValue=' + this.runId)
        .then(response => {
          this.rowCount = response.data
          this.maxPages = Math.ceil(this.rowCount / this.perPage)
        })

    }
  },
  mounted(){
    this.initPage()
    this.updateTable()
  }
}


</script>

<style scoped>

</style>
