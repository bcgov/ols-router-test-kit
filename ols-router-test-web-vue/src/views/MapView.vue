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

  props: ['platform','resultId'],
  data() {
    return {
      selectedRt: null,
      iframeSrc: `https://office.refractions.net/~chodgson/gc/ols-demo/?rt=${this.platform}`,
      iframeParamString: '', // parameters sent from internal map frame via postMessage()
    };
  },
  async mounted(){
    window.addEventListener('message', this.handleMessage);
    await this.fetchEnvironments(true)
    const isValidPlatform = this.environments.some(env => env.platform === this.platform);
    this.selectedRt = isValidPlatform ? this.platform : 'Prod';
  },
  destroyed() {
    window.removeEventListener('message', this.handleMessage);
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

    updateIframeSrc() {
      // Get the current iframe src, and apply the saved router params recieved from the map frame and the rt value from the selector
      let url = new URL(this.iframeSrc);
      let params = new URLSearchParams(this.iframeParamString);
      params.set('rt', this.selectedRt);
      url.search = params.toString();

      // Set the updated src and reload the iframe
      this.iframeSrc = url.toString();
      this.load();
    },

    handleMessage(event) {
      if(event.data.params) {
        // the internal map frame sends us a message whenever it updates its URL with routing params
        this.iframeParamString = event.data.params;
      }
    }
  },
};
</script>