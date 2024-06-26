<template>
  <div id="menuBar">
    <div><RouterLink :to="{name:'runs'}" class="homelink">RouterNG Testing Framework</RouterLink></div>
    <div id="tabs">
      <div><RouterLink :to="{name:'runs'}">Runs</RouterLink></div>
      <div><RouterLink :to="{name:'bulkTests'}">Bulk Tests</RouterLink></div>
      <div><RouterLink :to="{name:'customTests'}">Custom Tests</RouterLink></div>
      <div><RouterLink :to="{name:'results'}">Results</RouterLink></div>
      <div><RouterLink :to="{name:'createNew'}">Create New Tests/Env/...</RouterLink></div>
      <div><RouterLink :to="{name:'createRun'}">Queue New Router Run</RouterLink></div>
      <div><RouterLink :to="{name:'map'}">Map</RouterLink></div>
    </div>
  </div>
</template>

<script>
import { RouterLink} from 'vue-router'

export default {}


</script>

<style scoped>

#menuBar {
    flex: 0 0 auto;
    font-size: 18px;
    font-family: verdana,helvetica;
    color: white;
    display: flex;
    margin: 0px;
    align-items: baseline;
    justify-content: flex-start;
    padding-top: 4px;
    padding-left: 20px;
    border-bottom: 1px solid DimGrey;
    background-color: SteelBlue;
}
#tabs {
  flex: 0 0 auto;
	font-size: 16px;
	font-family: verdana,helvetica;
	color: white;
	display: flex;
	margin: 0px 0px 0px 40px;
	justify-content: center;
	padding-top: 4px;
  background-color: SteelBlue;
}
#tabs div {
	border: 1px solid DimGrey;
	border-radius: 4px 4px 0 0;
	padding: 4px 8px;
	background-color: LightGrey;
	color: Navy;
	margin-bottom: -1px;
}
#tabs div a,
#tabs div a:active,
#tabs div a:visited {
	color: Navy;
  text-decoration: none;
}
#tabs div a.router-link-active {
  color: var(--color-text-link);
}

.homelink {
  text-decoration: none;
}



</style>
