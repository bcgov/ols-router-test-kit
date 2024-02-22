<template>
  <main class="container">

    <div class="p-1 fw-bold border-bottom mb-2">Compare Results for Run Ids: #{{ runIdA }} and #{{ runIdB }} </div>
    <div>Table Description: A list of all results that are associated to this Run Id. Click the "See on Map" button to review the calculated route. <p>The Partition Signature is encoded data describing how the route uses truck routes(1's) and non-truck routes(0's).  e.g. "010" means the route started as non-truck, went onto a truck route, then finished on a non-truck route.</p></div>
    <div> &nbsp</div>
    <div> Displaying Rows {{ ((pageNum-1) * perPage) }} to {{ ((pageNum-1) * perPage) + curPageCount }} out of {{rowCount}} rows:</div>
    <table class="table table-striped table-sm">
      <tbody>
        <tr>
          <th class="rightborder"></th>
          <th colspan="4" class="thLink rightborder" @click="setSortBy('distance_diff')"> Distance(m)
            <template v-if="(this.sortBy === 'distance_diff' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'distance_diff' && !this.descending )">
                ▲
            </template>
          </th>

          <th colspan="4" class="thLink rightborder" @click="setSortBy('duration_diff')"> Duration(s)
            <template v-if="(this.sortBy === 'duration_diff' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'duration_diff' && !this.descending )">
                ▲
            </template>
          </th>
          
          <th colspan="4" class="thLink rightborder" @click="setSortBy('calc_time_diff')"> Calc Time (ms)
            <template v-if="(this.sortBy === 'calc_time_diff' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'calc_time_diff' && !this.descending )">
                ▲
            </template>
          </th>

          <th colspan="3" class="thLink rightborder" @click="setSortBy('partition_diff')"> Partition Sig
            <template v-if="(this.sortBy === 'partition_diff' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'partition_diff' && !this.descending )">
                ▲
            </template>
          </th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
        <tr>
          <th class="thLink rightborder" @click="setSortBy('a_test_id')"> Test ID  
            <template v-if="(this.sortBy === 'a_test_id' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'a_test_id' && !this.descending )">
                ▲
            </template>
          </th>

          <th> {{ this.runIdA }}</th>
          <th> {{ this.runIdB }}</th>
          <th class="thLink" @click="setSortBy('distance_diff')"> Diff
            <template v-if="(this.sortBy === 'distance_diff' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'distance_diff' && !this.descending )">
                ▲
            </template>
          </th>
          <th class="thLink rightborder" @click="setSortBy('distance_perc')"> Diff %
            <template v-if="(this.sortBy === 'distance_perc' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'distance_perc' && !this.descending )">
                ▲
            </template>
          </th>

          <th> {{ this.runIdA }}</th>
          <th> {{ this.runIdB }}</th>
          <th class="thLink" @click="setSortBy('distance_diff')"> Diff
            <template v-if="(this.sortBy === 'duration_diff' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'duration_diff' && !this.descending )">
                ▲
            </template>
          </th>
          <th class="thLink rightborder" @click="setSortBy('duration_perc')"> Diff %
            <template v-if="(this.sortBy === 'duration_perc' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'duration_perc' && !this.descending )">
                ▲
            </template>
          </th>

          <th> {{ this.runIdA }}</th>
          <th> {{ this.runIdB }}</th>
          <th class="thLink" @click="setSortBy('calc_time_diff')"> Diff
            <template v-if="(this.sortBy === 'calc_time_diff' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'calc_time_diff' && !this.descending )">
                ▲
            </template>
          </th>
          <th class="thLink rightborder" @click="setSortBy('calc_time_perc')"> Diff %
            <template v-if="(this.sortBy === 'calc_time_perc' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'calc_time_perc' && !this.descending )">
                ▲
            </template>
          </th>

          <th> {{ this.runIdA }}</th>
          <th> {{ this.runIdB }}</th>
          <th class="rightborder"> Diff %</th>

          <th class="thLink" @click="setSortBy('description')"> Description
            <template v-if="(this.sortBy === 'description' && this.descending )">
                ▼
            </template>
            <template v-if="(this.sortBy === 'description' && !this.descending )">
                ▲
            </template>
          </th>
          <th> Notes</th>
          <th> Map</th>
        </tr>

        <tr v-for="result in results" >  
            <td class="rightborder centered"> <router-link :to="{name:'test',params:{testId:result.a_test_id}}">{{ result.a_test_id }} </router-link> </td>

            <td class="right"> {{ result.a_distance.toFixed(0)}} </td>
            <td class="right"> {{ result.b_distance.toFixed(0) }}</td>
            <td class="right"> {{ result.distance_diff.toFixed(0)  }}</td>
            <td class="rightborder right"> {{ result.distance_perc.toFixed(0) }} </td>

            <td class="right"> {{ result.a_duration.toFixed(0)}} </td>
            <td class="right"> {{ result.b_duration.toFixed(0) }}</td>
            <td class="right"> {{ result.duration_diff.toFixed(0)  }}</td>
            <td class="rightborder right"> {{ result.duration_perc.toFixed(0) }} </td>

            <td class="right"> {{ result.a_calc_time}} </td>
            <td class="right"> {{ result.b_calc_time }}</td>
            <td class="right"> {{ result.calc_time_diff.toFixed(0)  }}</td>
            <td class="rightborder right"> {{ result.calc_time_perc.toFixed(0) }} </td>

            <td class="right"> {{ result.a_partition_signature}} </td>
            <td class="right"> {{ result.b_partition_signature}}</td>
            <td class="rightborder right"> {{ result.partition_diff  }}</td>

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
    runIdA: Number,
    runIdB: Number
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
        .get(this.ApiUrl + '/compareRuns?runIdA=' + this.runIdA + '&runIdB=' + this.runIdB + '&pageNumber=' + zeroBasePageNum + '&perPage=' + this.perPage + '&sortBy=' + this.sortBy + '&descending=' + this.descending)
        .then(response =>{
          this.results = response.data
          this.curPageCount = this.results.length 
        })

      axios
        .get(this.ApiUrl + '/compareRunsCount?runIdA=' + this.runIdA + '&runIdB=' + this.runIdB )
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
