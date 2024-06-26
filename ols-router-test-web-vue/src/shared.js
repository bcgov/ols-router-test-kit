import axios from 'axios';

export default {
    data(){
      return{perPage:10,
        pageNum:1, // 1-based page number for display , we always -1 when making the call to the API which is 0-based-->
        descending:true,
        maxPages:9999999,
        sortBy:"",
        rowCount:9999999,
        curPageCount:10,
        baseUrl:"http://office.refractions.net/~chodgson/gc/ols-demo/index.html",
        defaultRt:"rri",
        datasets: [],
        environments: [],
        groupNameOptions: [],
        codeVersions: [],
        ApiUrl: "http://localhost:8080",
      }
    },
    methods: {
        setSortBy(col){
            if (this.sortBy == col){
              this.descending = !this.descending
            }else{
              this.sortBy = col
              this.descending = false
            }
            this.updateTable()
        },
        previousPage(){
            if(this.pageNum > 1){
              this.pageNum = this.pageNum -1
            }
          this.updateTable()
        },
        nextPage(){
            if(this.pageNum < this.maxPages){
              this.pageNum = this.pageNum +1
            }
            this.updateTable()
        },
        perPageChanged(){
            this.updateTable()
        },
        pageNumChanged(){
            if (isNaN(this.pageNum) ) this.pageNum = 1;
            if (this.pageNum < 1) this.pageNum = 1;
            if (this.pageNum > this.maxPages) this.pageNum = this.maxPages;
            this.updateTable()
          },
        showOnMap(resultId){
          this.$router.push({name: 'map', params: {resultId:[resultId]} })
          //location.href = this.baseUrl + "?rt=" + this.defaultRt + "&test_results=" 
          //    + encodeURIComponent(this.ApiUrl + "/resultsGeoJson?ids=" + resultId)
        },
        show2OnMap(resultId, resultId2){
          this.$router.push({name: 'map', params: {resultId:[resultId, resultId2]} })
          //location.href = this.baseUrl + "?rt=" + this.defaultRt + "&test_results=" 
          //    + encodeURIComponent(this.ApiUrl + "/resultsGeoJson?ids=" + resultId + "," + resultId2)
        },
        formatDate(date){
          return new Date(date);
        },      
        fetchGroupNames(){
          axios
          .get(this.ApiUrl + '/groupNameOptions')
          .then(response => {
            this.results = response.data
            response.data.forEach(item => {
              var option = { value: item.groupName, label: item.groupName }
              this.groupNameOptions.push(option)
            });
  
            var allOption = { value: 'All', label: 'All' }
            this.groupNameOptions.push(allOption)
          })
        },        
        fetchEnvironments() {
          axios
          .get(this.ApiUrl + '/environments')
          .then(response => {
            this.environments = response.data
            });
        },
        fetchDatasets() {
          axios
          .get(this.ApiUrl + '/datasets')
          .then(response => {
            this.datasets = response.data
            });
        },
        fetchCodes() {
          axios
          .get(this.ApiUrl + '/codeVersions')
          .then(response => {
            this.codeVersions = response.data
            });
        },
    }
  };