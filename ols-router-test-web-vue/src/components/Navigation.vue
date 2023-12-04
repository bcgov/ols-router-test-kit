<template>
    <div id="menuBar">
      <div>RouterNG Testing Framework</div>
      <div id="tabs">
        <div><RouterLink :to="{name:'runs'}">Runs</RouterLink></div>
        <div>Bulk Tests</div>
        <div>Custom Tests</div>
        <div>Results</div>
      </div>
      <div></div>
    </div>
</template>

<script>
import { RouterLink} from 'vue-router'

export default {
}

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
#tabs div.active {
	border: 1px solid Black;
	background-color: White;
	font-weight: bold;
	border-bottom-color: transparent;
}
#tabs div a,
#tabs div a:active,
#tabs div a:visited {
	color: Navy;
  text-decoration: none;
}
</style>
