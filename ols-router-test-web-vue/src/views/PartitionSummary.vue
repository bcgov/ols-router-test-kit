<template>
  <div>
    <h2>Results Summary</h2>

    <!-- Inputs for run IDs in a table layout -->
    <table class="table">
      <tr>
        <td><label for="runIdA" class="fst-normal">Run ID A:</label></td>
        <td><input type="number" id="runIdA" v-model="IdA" @keyup.enter="update"/></td>
      </tr>
      <tr>
        <td><label for="runIdB" class="fst-normal">Run ID B:</label></td>
        <td><input type="number" id="runIdB" v-model="IdB" @keyup.enter="update"/></td>
      </tr>
      <tr>
        <td colspan="2" style="text-align: center;">
          <button @click="update">Run</button>
        </td>
      </tr>
    </table>

    <div>
      <h3>Run Details</h3>
      <table class="table table-striped2 table-sm">
        <thead>
          <tr>
            <th>Run ID</th>
            <th>Description</th>
            <th>Dataset</th>
            <th>Environment - Platform</th>
            <th>Code Ver - Github ID</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>
              <a :href="`/run/${runA.runId}`">
                  {{ runA.runId }}
              </a>
            </td>
            <td>{{ runA.description }}</td>
            <td>{{ runA.datasetId }}</td>  <!-- Now contains dataset description -->
            <td>{{ runA.environmentId }}</td>  <!-- Now contains environment name -->
            <td>{{ runA.codeId }}</td>  <!-- Now contains "githubCommitId (versionNum)" -->
          </tr>
          <tr>
            <td>
              <a :href="`/run/${runB.runId}`">
                  {{ runB.runId }}
              </a> 
            </td>
            <td>{{ runB.description }}</td>
            <td>{{ runB.datasetId }}</td>
            <td>{{ runB.environmentId }}</td>
            <td>{{ runB.codeId }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <br>

    <!-- Tables side by side -->
    <div class="table-container">
      <div class="table-wrapper">
        <h3>Count per Signature</h3>
        <table class="table table-striped2 table-sm">
          <thead>
            <tr>
              <th>Signature</th>
              <th>Run ID A Count</th>
              <th>Run ID B Count</th>
            </tr>
          </thead>
          <tbody>
            <!-- Loop through sorted data -->
            <tr v-for="(result, index) in groupedAndSortedResults" :key="index">
              <td>{{ result.partition_signature }}</td>
              <td>{{ result.runA }}</td>
              <td>{{ result.runB }}</td>
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
              <td>
                <a :href="`/test/${mismatchResult.test_id}`">
                  {{ mismatchResult.test_id }}
                </a>
              </td>
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
      runA: {},
      runB: {},
    };
  },
  methods: {
    update(){
      this.fetchSummary();
      this.fetchRunDetails();
    },
    async fetchSummary() {
      try {
        this.results = [];
        const response = await axios.get(
          `${this.ApiUrl}/partitionSummary?runIdA=${this.IdA}&runIdB=${this.IdB}`
        );
        this.results = [...response.data];
      } catch (error) {
        console.error("Error fetching summary:", error);
      }

      try {
        this.mismatchResults = [];
        const response = await axios.get(
          `${this.ApiUrl}/getMismatchedPartitionSignatures?runIdA=${this.IdA}&runIdB=${this.IdB}`
        );
        this.mismatchResults = [...response.data];
      } catch (error) {
        console.error("Error fetching summary:", error);
      }
      
    },

    async fetchRunDetails() {
      try {
        // Fetch the two run details in parallel
        const [runAData, runBData] = await Promise.all([
          axios.get(`${this.ApiUrl}/run?runId=${this.IdA}`),
          axios.get(`${this.ApiUrl}/run?runId=${this.IdB}`)
        ]);

        this.runA = runAData.data;
        this.runB = runBData.data;

        // Fetch additional details in parallel for both runs
        const [datasetA, datasetB, envA, envB, codeA, codeB] = await Promise.all([
          axios.get(`${this.ApiUrl}/dataset?datasetId=${this.runA.datasetId}`),
          axios.get(`${this.ApiUrl}/dataset?datasetId=${this.runB.datasetId}`),
          axios.get(`${this.ApiUrl}/environment?envId=${this.runA.environmentId}`),
          axios.get(`${this.ApiUrl}/environment?envId=${this.runB.environmentId}`),
          axios.get(`${this.ApiUrl}/codeVersion?codeId=${this.runA.codeId}`),
          axios.get(`${this.ApiUrl}/codeVersion?codeId=${this.runB.codeId}`)
        ]);

        // Replace IDs with human-readable values
        this.runA.datasetId = datasetA.data.description;
        this.runB.datasetId = datasetB.data.description;
        
        this.runA.environmentId = `${envA.data.environment} - ${envA.data.platform}`;
        this.runB.environmentId = `${envB.data.environment} - ${envB.data.platform}`;

        this.runA.codeId = `${codeA.data.versionNum} - ${codeA.data.githubCommitId}`;
        this.runB.codeId = `${codeB.data.versionNum} - ${codeB.data.githubCommitId}`;

      } catch (error) {
        console.error("Error fetching run details:", error);
      }
    }
  },
  computed: {
    groupedAndSortedResults() {
      // Step 1: Group the results by partition_signature and assign counts for Run A and Run B
      const grouped = {};
      this.results.forEach(({ partition_signature, run_id, count }) => {
        if (!grouped[partition_signature]) {
          grouped[partition_signature] = { runA: null, runB: null };
        }
        if (run_id === this.IdA) {
          grouped[partition_signature].runA = count;
        } else if (run_id === this.IdB) {
          grouped[partition_signature].runB = count;
        }
      });

      // Step 2: Convert grouped object to an array of entries and sort them by signature length
      const groupedArray = Object.entries(grouped).map(([partition_signature, counts]) => ({
        partition_signature,
        ...counts,
      }));

      // Step 3: Sort the grouped array by signature length so they are ordered nicely
      return groupedArray.sort((a, b) => {
        const lengthComparison = a.partition_signature.length - b.partition_signature.length;
        if (lengthComparison === 0) {
          return a.partition_signature.localeCompare(b.partition_signature);
        }
        return lengthComparison;
      });
    },

    // Computed property for formatting the values
    formatRunData() {
      return (value) => {
        if (typeof value === "object") {
          return JSON.stringify(value);  // For nested objects, convert to string
        }
        return value;
      };
    }
  },
  mounted(){
    this.fetchSummary();
    this.fetchRunDetails();
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
