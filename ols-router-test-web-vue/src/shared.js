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
        baseUrl:"https://bcgov.github.io/ols-devkit/ols-demo/index.html",
        defaultRt:"rri",
        datasets: [],
        environments: [],
        groupNameOptions: [],
        codeVersions: [],
        ApiUrl: "https://ols-route-test-dev.apps.gov.bc.ca/api",
        debouceTime: 700,
        debounceTimeout: null,
      }
    },
    methods: {
        updateTable() {
          // Clear the previous timeout if it exists
          if (this.debounceTimeout) {
            clearTimeout(this.debounceTimeout);
          }
    
          // Set a new timeout
          this.debounceTimeout = setTimeout(() => {
            // Call the function to update the table
            this.runUpdateTable();
          }, this.debouceTime); // Adjust the delay in shared.js
        },
        setSortBy(col){
            if (this.sortBy == col){
              this.descending = !this.descending
            }else{
              this.sortBy = col
              this.descending = true
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
        showOnMap(resultId, platform) {
          //this.$router.push({ name: 'map', params: { resultId: resultId.toString() } });
          // Open the route in a new window instead
          window.open(
            this.$router.resolve({
              name: 'map',
              params: { resultId: resultId.toString(),
                        platform: platform.toString() 
               }
            }).href,
            '_blank'
          );
        },       
        show2OnMap(resultId, resultId2, platform) {
          const combinedResultId = `${resultId},${resultId2}`;
          // Open the route in a new window
          window.open(
            this.$router.resolve({
              name: 'map',
              params: { resultId: combinedResultId,
                        platform: platform.toString() 
               }
            }).href,
            '_blank'
          );
        },
        formatDate(date){
          if (date == null) {
            return '';
          } else {
            return new Date(date);
          }
          
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
        fetchEnvironments(usableOnly = false) {
          return axios
          .get(this.ApiUrl + '/environments')
          .then(response => {
            this.environments = response.data;
            if(usableOnly){
              this.environments = this.environments.filter(env => env.usableAsMapPlatformInd === true);
            }
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