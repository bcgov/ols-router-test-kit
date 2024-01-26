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
        defaultRt:"rri"
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
          location.href = this.baseUrl + "?rt=" + this.defaultRt + "&test_results=" + resultId
        },
        show2OnMap(resultId, resultId2){
          location.href = this.baseUrl + "?rt=" + this.defaultRt + "&test_results=" + resultId + "," + resultId2
        }
    }
  };