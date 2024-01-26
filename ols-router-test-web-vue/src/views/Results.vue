<template>
  <main class="container">
    
    <div class="p-1 fw-bold border-bottom mb-2">Results</div>
    <div>Table Description: A list of test results and some associated values. The Partition Signature is encoded data describing how the route uses truck routes(1's) and non-truck routes(0's).  e.g. "010" means the route started as non-truck, went onto a truck route, then finished on a non-truck route. 0101010 means it hopped on and off truck routes multiple times and might be a problem. For non-truck routes it is blank and not used.</div>
    <div> &nbsp</div>
    <div> Displaying Rows {{ ((pageNum-1) * perPage) }} to {{ ((pageNum-1) * perPage) + curPageCount }} out of {{rowCount}} rows:</div>
    <table class="table table-striped table-sm">
      <tbody>
        <tr>
        <th class="thLink" @click="setSortBy('testId')"> Test ID 
          <template v-if="(this.sortBy === 'testId' && this.descending )">
              &gt;
          </template>
          <template v-if="(this.sortBy === 'testId' && !this.descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('runId')"> Run ID 
          <template v-if="(this.sortBy === 'runId' && this.descending )">
              &gt;
          </template>
          <template v-if="(this.sortBy === 'runId' && !this.descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('distance')"> Distance (m)
          <template v-if="(this.sortBy === 'distance' && this.descending )">
              &gt;
          </template>
          <template v-if="(this.sortBy === 'distance' && !this.descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('duration')"> Duration (s)
          <template v-if="(this.sortBy === 'duration' && this.descending )">
              &gt;
          </template>
          <template v-if="(this.sortBy === 'duration' && !this.descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('calcTime')"> Calc Time (ms)
          <template v-if="(this.sortBy === 'calcTime' && this.descending )">
              &gt;
          </template>
          <template v-if="(this.sortBy === 'calcTime' && !this.descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('partitionSignature')"> Partition Signature
          <template v-if="(this.sortBy === 'partitionSignature' && this.descending )">
              &gt;
          </template>
          <template v-if="(this.sortBy === 'partitionSignature' && !this.descending )">
              &lt;
          </template>
        </th>


      </tr>
      <tr v-for="result in results">
        <td>
          <router-link :to="{name:'test',params:{testId:result.testId}}">{{ result.testId }} </router-link>
        </td>
        <td>
          <router-link :to="{name:'run',params:{runId:result.runId}}">{{ result.runId }} </router-link>
        </td>
        <td> {{ result.distance}} </td>
        <td> {{ result.duration }}</td>
        <td> {{ result.calcTime  }}</td>
        <td> {{ result.partitionSignature }} </td>
        <td><button @click="showOnMap(result.resultId)">Show on Map</button></td>
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
    updateTable(){
      
      var zeroBasePageNum = this.pageNum - 1
      axios
        .get('http://localhost:8080/results?pageNumber=' + zeroBasePageNum + '&perPage=' + this.perPage + '&sortBy=' + this.sortBy + '&descending=' + this.descending)
        .then(response => {
          this.results = response.data
          this.curPageCount = this.results.length 
        })

        axios
        .get('http://localhost:8080/resultsCount')
        .then(response => {
          this.rowCount = response.data
          this.maxPages = Math.ceil(this.rowCount / this.perPage)
        })


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