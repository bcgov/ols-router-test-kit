<template>
  <div style="border: none; width: 100%; height: 99%;">
    
    <!-- Dropdown for selecting rt parameter -->
    <label>
      Select Router
      <span class="info-icon" title="Choose which router URL to be used for dynamic route requests. This won't affect viewing a route from a test results.">(ℹ)</span>
      :&nbsp &nbsp
      <select v-model="selectedRt" @change="updateIframeSrc">
      <option v-for="env in environments" :key="env.platform" :value="env.platform">
        {{ env.platform }}
      </option>
    </select>

    </label>
  
    <!-- Iframe -->
    <iframe
      @load="load"
      id="mapFrame"
      style="border: none; width: 100%; height: 97%;"
      :src="iframeSrc"
    ></iframe>
  </div>
</template>

<script>
import axios from 'axios';
import Shared from "../shared.js";

export default {
  extends: Shared,

  props: ['platform','resultId']
  ,
  data() {
    return {
      selectedRt: null,
      iframeSrc: 'https://office.refractions.net/~chodgson/gc/ols-demo/?rt=rri', // Initial iframe URL
    };
  },
  mounted(){
    this.selectedRt = this.platform;
    this.fetchEnvironments(true)
    
  },
  methods: {
    load() {
      if (this.resultId && this.resultId.length) {
        axios
          .get(this.ApiUrl + "/resultsGeoJson?ids=" + this.resultId.join(","))
          .then(response => {
            document
              .getElementById('mapFrame')
              .contentWindow.postMessage(response.data, '*');
          })
          .catch(error => {
            console.error('Error fetching data:', error);
          });
      }
    },

    updateQueryStringParameter(url, key, value) {
      const baseUrl = url.split('?')[0];
      const queryString = url.split('?')[1] || '';
      const params = new URLSearchParams(queryString);

      // Update or set the parameter
      if (value === null) {
        params.delete(key); // Remove the parameter if null
      } else {
        params.set(key, value); // Set or update the parameter
      }

      return `${baseUrl}?${params.toString()}`;
    },

    updateIframeSrc() {
      // Get the current iframe src
      //This doesn't work with Crossed Origins, but maybe it will run on the same server eventually so this would try to keep paramaters on a change of server
      let currentSrc = this.iframeSrc || document.getElementById('mapFrame').contentWindow.location.href;
      const updatedSrc = this.updateQueryStringParameter(currentSrc, 'rt', this.selectedRt);

      // Set the updated src and reload the iframe
      this.iframeSrc = updatedSrc;
      this.load();
    },

  },
};
</script>