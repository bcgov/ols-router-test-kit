<template>
  <main class="container">
    
    <div class="p-1 fw-bold border-bottom mb-2">Bulk Tests </div>
    <div>Table Description: A large set of simple test without specific routing parameters. Each test is a simple a start and end point, meant to provide a large coverage of routing areas and possible use cases.</div>
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
        <th class="thLink" @click="setSortBy('description')"> Description 
          <template v-if="(this.sortBy === 'description' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'description' && !this.descending )">
              ▲
          </template>
        </th>
        <th class="thLink" @click="setSortBy('groupName')"> Group Name 
          <template v-if="(this.sortBy === 'groupName' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'groupName' && !this.descending )">
              ▲
          </template>
        </th>
        <th class="thLink" @click="setSortBy('points')">Coordinates
          <template v-if="(this.sortBy === 'points' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'points' && !this.descending )">
              ▲
          </template>
        </th>
      </tr>
      <tr v-for="test in bulkTests">
        <td>
          <router-link :to="{name:'test',params:{testId:test.testId}}">{{ test.testId }} </router-link>
        </td>
        <td> {{ test.description}} </td>
        <td> {{ test.groupName}}</td>
        <td class="hideoverflowbigtd"> {{ test.points }} </td>
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
    return{perPage:10,
        pageNum:1, // 1-based page number for display , we always -1 when making the call to the API which is 0-based-->
        sortBy:"testId",
        descending:false,
        maxPages:9999999,
        bulkTests:null
    }

  },
  computed: {
  },
  methods: {
    updateTable(){
      var zeroBasePageNum = this.pageNum - 1
      axios
        .get(this.ApiUrl + '/bulkTests?pageNumber=' + zeroBasePageNum + '&perPage=' + this.perPage + '&sortBy=' + this.sortBy + '&descending=' + this.descending)
        .then(response => {
          this.bulkTests = response.data
          this.curPageCount = this.bulkTests.length 
        })

        axios
        .get(this.ApiUrl + '/bulkTestsCount')
        .then(response => {
          this.rowCount = response.data
          this.maxPages = Math.ceil(this.rowCount / this.perPage)
        })

      
    }
  },
  mounted(){
    this.updateTable()
    
  }
}


</script>

<style scoped>

</style>