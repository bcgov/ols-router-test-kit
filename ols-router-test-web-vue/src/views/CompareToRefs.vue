<template>
  <main class="container">

    <div class="p-1 fw-bold border-bottom mb-2">Compare Results Against Reference Tests for Run Id: #{{ runId }} </div>
    <div>Table Description: A list of all results that are associated to this Run Id compared with the Reference Route. The "Diff" columns is the difference between the two results. Click the "See on Map" button to review the two routes.</div>
    <div> &nbsp</div>
    <div> Displaying Rows {{ ((pageNum-1) * perPage) }} to {{ ((pageNum-1) * perPage) + curPageCount }} out of {{rowCount}} rows:</div>
    <table class="table table-striped table-sm">
      <tbody>
        <tr>
          <th class="rightborder"></th>
          <th colspan="3" class="thLink rightborder" @click="setSortBy('distance_diff')"> Distance(m)
            <template v-if="(this.sortBy === 'distance_diff' && this.descending )">
                &gt;
            </template>
            <template v-if="(this.sortBy === 'distance_diff' && !this.descending )">
                &lt;
            </template>
          </th>

          <th colspan="3" class="thLink rightborder" @click="setSortBy('duration_diff')"> Duration(s)
            <template v-if="(this.sortBy === 'duration_diff' && this.descending )">
                &gt;
            </template>
            <template v-if="(this.sortBy === 'duration_diff' && !this.descending )">
                &lt;
            </template>
          </th>
          
          <th colspan="3" class="thLink rightborder" @click="setSortBy('calc_time_diff')"> Calc Time (ms)
            <template v-if="(this.sortBy === 'calc_time_diff' && this.descending )">
                &gt;
            </template>
            <template v-if="(this.sortBy === 'calc_time_diff' && !this.descending )">
                &lt;
            </template>
          </th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
        <tr>
          <th class="thLink rightborder" @click="setSortBy('test_id')"> Test ID  
            <template v-if="(this.sortBy === 'test_id' && this.descending )">
                &gt;
            </template>
            <template v-if="(this.sortBy === 'test_id' && !this.descending )">
                &lt;
            </template>
          </th>

          <th> Ref</th>
          <th> #{{ this.runId }}</th>
          
          <th class="thLink  rightborder" @click="setSortBy('distance_diff')"> Diff
            <template v-if="(this.sortBy === 'distance_diff' && this.descending )">
                &gt;
            </template>
            <template v-if="(this.sortBy === 'distance_diff' && !this.descending )">
                &lt;
            </template>
          </th>

          <th> Ref</th>
          <th> #{{ this.runId }}</th>
          <th class="thLink rightborder" @click="setSortBy('distance_diff')"> Diff
            <template v-if="(this.sortBy === 'duration_diff' && this.descending )">
                &gt;
            </template>
            <template v-if="(this.sortBy === 'duration_diff' && !this.descending )">
                &lt;
            </template>
          </th>

          <th> Ref</th>
          <th> #{{ this.runId }}</th>
          <th class="thLink  rightborder" @click="setSortBy('calc_time_diff')"> Diff
            <template v-if="(this.sortBy === 'calc_time_diff' && this.descending )">
                &gt;
            </template>
            <template v-if="(this.sortBy === 'calc_time_diff' && !this.descending )">
                &lt;
            </template>
          </th>

          <th class="thLink" @click="setSortBy('description')"> Description
            <template v-if="(this.sortBy === 'description' && this.descending )">
                &gt;
            </template>
            <template v-if="(this.sortBy === 'description' && !this.descending )">
                &lt;
            </template>
          </th>
          <th class="thLink" @click="setSortBy('notes')"> Notes
            <template v-if="(this.sortBy === 'notes' && this.descending )">
                &gt;
            </template>
            <template v-if="(this.sortBy === 'notes' && !this.descending )">
                &lt;
            </template>
          </th>

          <th> Map</th>
        </tr>

        <tr v-for="result in results" >  
            <td class="rightborder"> <router-link :to="{name:'test',params:{testId:result.test_id}}">{{ result.test_id }} </router-link> </td>

            <td> <template v-if="result.a_distance"> {{  result.a_distance.toFixed(0)}}</template>
            </td>
            <td> <template v-if="result.b_distance"> {{  result.b_distance.toFixed(0)}}</template></td>
            <td class="rightborder"> <template v-if="result.distance_diff"> {{  result.distance_diff.toFixed(0)}}</template></td>

            <td><template v-if="result.a_duration"> {{  result.a_duration.toFixed(0)}}</template></td>
            <td> <template v-if="result.b_duration"> {{  result.b_duration.toFixed(0)}}</template></td>
            <td class="rightborder"> <template v-if="result.duration_diff"> {{  result.duration_diff.toFixed(0)}}</template></td>


            <td><template v-if="result.a_calc_time"> {{  result.a_calc_time.toFixed(0)}}</template></td>
            <td> <template v-if="result.b_calc_time"> {{  result.b_calc_time.toFixed(0)}}</template></td>
            <td class="rightborder"> <template v-if="result.calc_time_diff"> {{  result.calc_time_diff.toFixed(0)}}</template></td>


            <td> {{ result.description}} </td>
            <td> {{ result.notes}}</td>
            <td> <button @click="show2OnMap(result.a_result_id, result.b_result_id )">View on Map</button></td>
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
      results:{}
    }

  },
  computed: {},
  methods: {
    updateTable(){
      var zeroBasePageNum = this.pageNum - 1
      axios
        .get('http://localhost:8080/compareRunVsRef?runId=' + this.runId + '&pageNumber=' + zeroBasePageNum + '&perPage=' + this.perPage + '&sortBy=' + this.sortBy + '&descending=' + this.descending)
        .then(response =>{
          this.results = response.data
          this.curPageCount = this.results.length 
        })

      axios
        .get('http://localhost:8080/compareRunVsRefCount?runId=' + this.runId)
        .then(response =>{
          this.rowCount = response.data
          this.maxPages = Math.ceil(this.rowCount / this.perPage)
        })
        
    }
  },
  mounted(){
    this.sortBy = "distance_diff"
    this.updateTable()
  }
}


</script>

<style scoped>

</style>
