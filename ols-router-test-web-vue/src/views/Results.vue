<template>
  <main class="container">
    
    <div class="p-1 fw-bold border-bottom mb-2">Results</div>
    <div>Table Description: A list of test results and some associated values. The Partition Signature is encoded data describing how the route uses truck routes(1's) and non-truck routes(0's).  e.g. "010" means the route started as non-truck, went onto a truck route, then finished on a non-truck route. 0101010 means it hopped on and off truck routes multiple times and might be a problem. For non-truck routes it is blank and not used.</div>
    <div> &nbsp</div>
    <div> Displaying Rows {{ ((pageNum-1) * perPage) +1}} to {{ ((pageNum-1) * perPage) + curPageCount }} out of {{rowCount}} rows:</div>
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
        <th class="thLink" @click="setSortBy('runId')"> Run ID 
          <template v-if="(this.sortBy === 'runId' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'runId' && !this.descending )">
              ▲
          </template>
        </th>
        <th class="thLink" @click="setSortBy('distance')"> Distance (m)
          <template v-if="(this.sortBy === 'distance' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'distance' && !this.descending )">
              ▲
          </template>
        </th>
        <th class="thLink" @click="setSortBy('duration')"> Duration (s)
          <template v-if="(this.sortBy === 'duration' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'duration' && !this.descending )">
              ▲
          </template>
        </th>
        <th class="thLink" @click="setSortBy('calcTime')"> Calc Time (ms)
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
        <th class="thLink" @click="setSortBy('resultId')"> Result Id
          <template v-if="(this.sortBy === 'resultId' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'resultId' && !this.descending )">
              ▲
          </template>
        </th>

      </tr>
      <tr v-for="result in results">
        <td class="centered">
          <router-link :to="{name:'test',params:{testId:result.testId}}">{{ result.testId }} </router-link>
        </td>
        <td class="centered">
          <router-link :to="{name:'run',params:{runId:result.runId}}">{{ result.runId }} </router-link>
        </td>
        <td class="right"> {{ result.distance}} </td>
        <td class="right"> {{ Math.round(result.duration) }}</td>
        <td class="right"> {{ result.calcTime  }}</td>
        <td class="right"> {{ result.partitionSignature }} </td>
        <td><button @click="aboutToshowOnMap(result.resultId, result.runId)">Show on Map (#{{ result.resultId}}) </button></td>
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
  data(){
    return{
      results:{}
    }

  },
  computed: {
  },
  methods: {
    runUpdateTable(){
      
      var zeroBasePageNum = this.pageNum - 1
      axios
        .get(this.ApiUrl + '/results?pageNumber=' + zeroBasePageNum + '&perPage=' + this.perPage + '&sortBy=' + this.sortBy + '&descending=' + this.descending)
        .then(response => {
          this.results = response.data
          this.curPageCount = this.results.length 
        })

        axios
        .get(this.ApiUrl + '/resultsCount')
        .then(response => {
          this.rowCount = response.data
          this.maxPages = Math.ceil(this.rowCount / this.perPage)
        })
    },
    aboutToshowOnMap(resultId, runId){
      axios
        .get(this.ApiUrl + '/run?runId=' + runId)
        .then(response => {
          var run = response.data;
          if (run.environmentId) {
            axios
              .get(this.ApiUrl + '/environment?envId=' + run.environmentId)
              .then(response => {
                var environment = response.data;
                if (environment && resultId) {
                  this.showOnMap(resultId, environment.platform);
                }
              });
          }
        });
    }
  },
  mounted(){
    this.sortBy = "runId"
    this.updateTable()
    
  }
}


</script>

<style scoped>

</style>

../shared.js