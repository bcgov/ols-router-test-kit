<template>
  <div>
    <h2>Results Summary</h2>

    <!-- Inputs for run IDs in a table layout -->
    <table class="table">
      <tr>
        <td><label for="runIdA" class="fst-normal">Run ID A:</label></td>
        <td><input type="number" id="runIdA" v-model="IdA" @keyup.enter="fetchSummary"/></td>
      </tr>
      <tr>
        <td><label for="runIdB" class="fst-normal">Run ID B:</label></td>
        <td><input type="number" id="runIdB" v-model="IdB" @keyup.enter="fetchSummary"/></td>
      </tr>
      <tr>
        <td colspan="2" style="text-align: center;">
          <button @click="fetchSummary">Run</button>
        </td>
      </tr>
    </table>

    <br>

    <!-- Tables side by side -->
    <div class="table-container">
      <div class="table-wrapper">
        <h3>Count per Signature</h3>
        <table class="table table-striped2 table-sm">
          <thead>
            <tr>
              <th>Signature</th>
              <th>Run ID</th>
              <th>Count</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="result in results" :key="result.run_id">
              <td>{{ result.partition_signature }}</td>
              <td>{{ result.run_id }}</td>
              <td>{{ result.count }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="table-wrapper">
        <h3>Test IDs with non-matching signatures</h3>
        <table class="table table-striped2 table-sm">
          <thead>
            <tr>
              <th>Test ID</th>
              <th>Run ID A Sig</th>
              <th>Run ID B Sig</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="mismatchResult in mismatchResults" :key="mismatchResult.test_id">
              <td>{{ mismatchResult.test_id }}</td>
              <td>{{ mismatchResult.signatureA }}</td>
              <td>{{ mismatchResult.signatureB }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>



<script>
import axios from 'axios';
import Shared from "../shared.js";

export default {
  extends: Shared,
  props: {
    runIdA: {
      type: Number,
      required: true
    },
    runIdB: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      // Local variables initialized to the props values
      IdA: this.runIdA,
      IdB: this.runIdB,
      results: [],
      mismatchResults: [],
    };
  },
  methods: {
    async fetchSummary() {
      try {
        this.results = [];
        const response = await axios.get(
          `${this.ApiUrl}/partitionSummary?runIdA=${this.IdA}&runIdB=${this.IdB}`
        );
        this.results = response.data;
      } catch (error) {
        console.error("Error fetching summary:", error);
      }

      try {
        this.mismatchResults = [];
        const response = await axios.get(
          `${this.ApiUrl}/getMismatchedPartitionSignatures?runIdA=${this.IdA}&runIdB=${this.IdB}`
        );
        this.mismatchResults = response.data;
      } catch (error) {
        console.error("Error fetching summary:", error);
      }
      
    }
  },
  mounted(){
    this.fetchSummary();
  }
};
</script>

<style scoped>
.table-container {
  display: flex;
  gap: 20px; /* Space between tables */
  align-items: flex-start; /* Prevents shorter table from stretching */
}
.table {
  min-width: 300px; /* Ensures tables don’t shrink too much */
}
</style>
