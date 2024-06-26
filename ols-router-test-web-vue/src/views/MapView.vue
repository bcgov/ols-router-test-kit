<template>
  <iframe @load="load" id="mapFrame" style="border: none; width:100%; height:99%;" src="https://office.refractions.net/~chodgson/gc/ols-demo/"></iframe>
</template>


<script>
import axios from 'axios';
import Shared from "../shared.js";

export default {
  extends: Shared,

  props: ['resultId'],
  data() {
    return{
    }
  },
  methods: {
    load() {
      console.log(this.resultId)
      if(this.resultId && this.resultId.length) {
        axios
          .get(this.ApiUrl + "/resultsGeoJson?ids=" + this.resultId.join(","))
          .then(response => {
            document.getElementById('mapFrame').contentWindow.postMessage(response.data, "*");
          });
      }
    },
  }
}


</script>
