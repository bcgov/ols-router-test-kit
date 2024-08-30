<template>
  <main class="container">
    
    <div class="p-1 fw-bold border-bottom mb-2">Custom Tests</div>
    <div>Table Description: A list of custom test definitions. Each test has specific routing parameters associated with it. Typically used for known edge-cases and other feature specific test cases.</div>
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
        <th class="thLink" @click="setSortBy('description')"> Description
          <template v-if="(this.sortBy === 'description' && this.descending )">
              ▼
          </template>
          <template v-if="(this.sortBy === 'description' && !this.descending )">
              ▲
          </template>
        </th>
        <th> Criteria </th>
        <th> Enable </th>
        <th class="hideoverflowbigtd">Other Paramaters</th>
        <th> Forward Ref? </th>
        <th> Reverse Ref? </th>
      </tr>
      <tr v-for="test in tests">
        <td>
          <router-link :to="{name:'test',params:{testId:test.testId}}">{{ test.testId }} </router-link>
        </td>
        <td> {{ test.description}} </td>
        <td><p v-if="test.parameters?.criteria">{{ test.parameters.criteria }}</p></td>
        <td><p v-if="test.parameters?.criteria">{{ test.parameters.enable }}</p></td>
        <td class="hideoverflowbigtd">
           
           <div class="hideoverflowtext" v-for="(param, name, i) in test.parameters">
              {{  name +":"+  param }}
           </div>
        </td>
        <td><p v-if="test.forwardResultId">Yes</p><p v-else>No</p></td>
        <td><p v-if="test.reverseResultId">Yes</p><p v-else>No</p></td>
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
        tests:null,
        
    }

  },
  computed: {
  },
  methods: {
    runUpdateTable(){
      var zeroBasePageNum = this.pageNum - 1
      axios
        .get(this.ApiUrl + '/customTests?pageNumber=' + zeroBasePageNum + '&perPage=' + this.perPage + '&sortBy=' + this.sortBy + '&descending=' + this.descending)
        .then(response => {
          this.tests = response.data
          this.curPageCount = this.tests.length 
        })

        axios
        .get(this.ApiUrl + '/customTestsCount')
        .then(response => {
          this.rowCount = response.data
          this.maxPages = Math.ceil(this.rowCount / this.perPage)
        })

    }
  },
  mounted(){
    this.sortBy = "testId"
    this.updateTable()
  }
}


</script>

<style scoped>

</style>

../shared.js