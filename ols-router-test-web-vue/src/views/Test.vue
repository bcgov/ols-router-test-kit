<template>
  <main class="container">
    
    <div class="p-1">RouterNG Test Results for Test # {{ test.testId }}</div>
    <div>Description: A single test and it's attributes. Below are all the associated results that used this specific test definition.</div>
    <div> &nbsp</div>

    <table class="table table-striped">
      <tr>
        <td>Description:</td>
        <td>{{ test.description }}</td>
      </tr>

      <tr>
        <td>Group Name:</td>
        <td>{{ test.groupname }}</td>
      </tr>

      <tr>
        <td>Test Created Date:</td>
        <td>{{ test.createdDate }}</td>
      </tr>

      <tr>
        <td>Forward Reference:</td>
        <td>{{ test.forwardResultId }}</td>
      </tr>

      <tr>
        <td>Reverse Reference:</td>
        <td>{{ test.reverseResultId }}</td>
      </tr>

      <template v-if="(this.test.parameters)">
        <tr v-for="(p, name, i) in test.parameters"  >
          <td>{{ name }}</td>
          <td class="">{{ p }}</td>
        </tr>
      </template>
    </table>


    <div class="p-1 fw-bold border-bottom mb-2">Results for Test Id: {{ testId }} </div>
    <div>Table Description: A list of all results that are associated to this Run Id. Click the "See on Map" button to review the calculated route. <p>The Partition Signature is encoded data describing how the route uses truck routes(1's) and non-truck routes(0's).  e.g. "010" means the route started as non-truck, went onto a truck route, then finished on a non-truck route.</p></div>
    <div> &nbsp</div>
    <div> Displaying Rows {{ ((pageNum-1) * perPage) }} to {{ ((pageNum-1) * perPage) + curPageCount }} out of {{rowCount}} rows:</div> 
    <table class="table table-striped table-sm">
      <tbody>
        <tr>
        <th class="thLink" @click="setSortBy('runId')"> RunID 
          <template v-if="(this.sortBy === 'runId' && this.descending )">
              &gt;
          </template>
          <template v-if="(this.sortBy === 'runId' && !this.descending )">
              &lt;
          </template>
        </th>
         <th class="thLink" @click="setSortBy('distance')"> Distance 
          <template v-if="(this.sortBy === 'distance' && this.descending )">
              &gt;
          </template>
          <template v-if="(this.sortBy === 'distance' && !this.descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('duration')"> Duration 
          <template v-if="(this.sortBy === 'duration' && this.descending )">
              &gt;
          </template>
          <template v-if="(this.sortBy === 'duration' && !this.descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('calcTime')"> Calc Time
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
        <tr v-for="result in results" >  
          <td> <router-link :to="{name:'run',params:{runId:result.runId}}">{{ result.runId }} </router-link> </td>
          <td> {{ result.distance}} </td>
          <td> {{ result.duration }}</td>
          <td> {{ result.calcTime  }}</td>
          <td> {{ result.partitionSignature }} </td>
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
    testId:Number
  },
  data(){
    return{
        test:{},
        results:{}
    }

  },
  computed: {
  },
  methods: {
    initPage(){
      this.sortBy = "RunId"
      axios
        .get('http://localhost:8080/test?testId=' + this.testId)
        .then(response =>{this.test = response.data})
    },

    updateTable(){
      var zeroBasePageNum = this.pageNum - 1
      this.curPageCount = 0 
      axios
        .get('http://localhost:8080/results?filterColumn=testId&filterValue=' + this.testId + '&pageNumber=' + zeroBasePageNum + '&perPage=' + this.perPage + '&sortBy=' + this.sortBy + '&descending=' + this.descending)
        .then(response => {
          this.results = response.data 
          this.curPageCount = this.results.length 
        })


      axios
        .get('http://localhost:8080/resultsCount?filterColumn=testId&filterValue=' + this.testId)
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


../shared.js