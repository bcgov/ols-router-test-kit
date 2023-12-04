<template>
  <main class="container">
    
    <div class="p-1 fw-bold border-bottom mb-2">Test Runs</div>
    <table class="table table-striped table-sm">
      <tbody>
      <tr v-for="run in runs">
        <td v-for="item in run">
          {{ item }}
        </td>
      </tr>
    </tbody>
    </table>
    
  </main>
</template>

<script>
export default {
  computed: {
    runs: function() {
      return [["col1", "col2"],["row2col1","row2col2"]];
    }
  },
  methods: {
  },
}
</script>

<style scoped>

</style>

