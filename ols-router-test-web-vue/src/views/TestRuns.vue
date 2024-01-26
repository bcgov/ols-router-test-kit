<template>
  <main class="container">
    
    <div class="p-1 fw-bold border-bottom mb-2">Test Runs </div>
    <div>Table Description: A list of completed test runs. Each run shows the date, number of tests, parameters used etc. To compare 2 runs against each other, select the radio button in column "Comp A" on one row, then the radio button in"Comp B" for the 2nd row you wish to compare. Then press the "Compare" button just below this text.</div>
    <div><button  @click="compareHandler"> Compare</button></div>
    <div> &nbsp</div>
    <div> Displaying Rows {{ ((pageNum-1) * perPage) }} to {{ ((pageNum-1) * perPage) + curPageCount }} out of {{rowCount}} rows:</div>
    <table class="table table-striped table-sm">
      <tbody>
        <tr>
        <th>Comp&nbspA</th>
        <th>Comp&nbspB</th>
        <th class="thLink" @click="setSortBy('runId')"> Run ID 
          <template v-if="(sortBy === 'runId' && descending )">
              &gt;
          </template>
          <template v-if="(sortBy === 'runId' && !descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('runDate')"> Date 
          <template v-if="(sortBy === 'runDate' && descending )">
              &gt;
          </template>
          <template v-if="(sortBy === 'runDate' && !descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('groupName')"> Group Name 
          <template v-if="(sortBy === 'groupName' && descending )">
              &gt;
          </template>
          <template v-if="(sortBy === 'groupName' && !descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('testCount')"># Tests
          <template v-if="(sortBy === 'testCount' && descending )">
              &gt;
          </template>
          <template v-if="(sortBy === 'testCount' && !descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('description')">Run Description
          <template v-if="(sortBy === 'description' && descending )">
              &gt;
          </template>
          <template v-if="(sortBy === 'description' && !descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('environment')">Environment
          <template v-if="(sortBy === 'environment' && descending )">
              &gt;
          </template>
          <template v-if="(sortBy === 'environment' && !descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('dataset')">Dataset
          <template v-if="(sortBy === 'dataset' && descending )">
              &gt;
          </template>
          <template v-if="(sortBy === 'dataset' && !descending )">
              &lt;
          </template>
        </th>
        <th class="thLink" @click="setSortBy('forwardRoute')">Dir
          <template v-if="(sortBy === 'forwardRoute' && descending )">
              &gt;
          </template>
          <template v-if="(sortBy === 'forwardRoute' && !descending )">
              &lt;
          </template>
        </th>
        <th>Criteria</th>
        <th>Enable</th>
        <th class="hideoverflowtd">Other Paramaters</th>
        <th>Vs Reference Route</th>
      </tr>
      <tr v-for="run in runs">
        <td class="centered"><input name='compareA' v-model="compareA" type='radio' :value="run.runId"></td>
        <td class="centered"><input name='compareB' v-model="compareB" type='radio' :value="run.runId"></td>
        <td class="thLink centered"> 
          <router-link :to="{name:'run',params:{runId:run.runId}}">{{ run.runId }}</router-link></td>
        <td>{{ run.runDate }}</td>
        <td>{{ run.groupName }}</td>
        <td class="centered">{{ run.testCount }}</td>
        <td>{{ run.description }}</td>
        <td>{{ run.environment   }}</td>
        <td> {{ run.dataset }} </td>
        <td class="centered" v-if="run.forwardRoute"> F </td>
        <td class="centered" v-else> T </td>
        <td>{{  run.parameters.criteria }}</td>
        <td>{{  run.parameters.enable }}</td>
        <td  class="hideoverflowtd">
          <div class="hideoverflowtext" v-for="(param, name, i) in run.parameters">
            <template v-if="(name != 'criteria' && name !='enable')">
              {{  name +":"+  param }}
            </template>
           </div>
        </td>
        <td class="centered"><button @click="referenceHandler(run.runId)">Vs Reference</button></td>
      </tr>
    </tbody>
    </table>
    <table class="table-noborder">
      <tr>
        <td class="textLink table-noborder" @click="pageNum=1;updateTable()">&lt&lt</td>&nbsp
        <td class="textLink table-noborder" @click="previousPage"> Prev </td>
        <td class="table-noborder">Rows Per Page: <input type="text" size=2 @input="perPageChanged" v-model="perPage" /></td>
        <td class="textLink table-noborder" @click="nextPage"> Next </td>&nbsp
        <td class="textLink table-noborder" @click="pageNum=maxPages;updateTable();">&gt&gt </td>
      </tr>
      <tr>
        <td class="table-noborder"></td>
        <td class="table-noborder">Jump To Page: <input type="text" size=2 @input="pageNumChanged" v-model="pageNum" /></td>
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
    return{perPage:10,
        pageNum:1, // 1-based page number for display , we always -1 when making the call to the API which is 0-based-->
        sortBy:"runId",
        descending:true,
        maxPages:9999999,
        runs:null,
        compareA:null,
        compareB:null,
    }

  },
  computed: {
  },
  methods: {
    updateTable(){
      var zeroBasePageNum = this.pageNum - 1
      axios
        .get('http://localhost:8080/runs?pageNumber=' + zeroBasePageNum + '&perPage=' + this.perPage + '&sortBy=' + this.sortBy + '&descending=' + this.descending)
        .then(response => {
          this.runs = response.data
          this.curPageCount = this.runs.length 
        })

        axios
        .get('http://localhost:8080/runsCount')
        .then(response => {
          this.rowCount = response.data
          this.maxPages = Math.ceil(this.rowCount / this.perPage)
        })


      
    },
    compareHandler(){
      if(this.compareA == null || this.compareB == null){
        alert('Select a row for each "Comp A" and "Comp B" columns and try again.')
        return
      }
      this.$router.push({ name:'compareRuns' , params:{runIdA:this.compareA, runIdB:this.compareB} })
    },
    referenceHandler(runId){
      this.$router.push({ name:'compareToRefs' , params:{runId:runId} })
    }
  },
  mounted(){
    this.updateTable()
  }
}


</script>

<style scoped>


</style>