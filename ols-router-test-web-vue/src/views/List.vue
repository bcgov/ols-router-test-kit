<template>
    <div>
      <h2>List of {{ objectName }}</h2>
      <table class="table table-striped">
          <tr>
            <th class="headerColour" v-for="(value, key) in dynamicRow1()" :key="key">{{ key }}</th>
            <!-- Loop through the keys of the first item to generate table headers -->
          </tr>
          <tr v-for="(item, index) in dynamicArray()" :key="index">
            <td v-for="(value, key) in item" :key="key">{{ value }}</td>
            <!-- Loop through the keys of each item to generate table cells -->
          </tr>
      </table>
      
    </div>
  </template>


<script>
import axios from 'axios';
import Shared from "../shared.js";

export default {
  extends: Shared,

  props:{
    objectName:String,
  },
  data(){
    return{
      objectList: []
    }

  },
  computed: {
  },
  methods: {
    dynamicRow1() {
      // Access the value using bracket notation
        return this[this.objectName][0];
    },
    dynamicArray(){
      return this[this.objectName];
    }

  },
  mounted(){
    if(this.objectName === "environments"){
      this.fetchEnvironments()
    }else if(this.objectName === "datasets"){
      this.fetchDatasets()
    }else if(this.objectName === "codeVersions"){
      this.fetchCodes()
    }
  }
}


</script>
