<template>
  <div id="pageWrap">
    <Navigation/>
    <div id="contentScroll">
      <div id="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import {RouterView} from 'vue-router'
import Navigation from './components/Navigation.vue'

</script>

<style scoped>
#pageWrap {
	display: flex;
	flex-direction: column;
	width: 100%;
	height: 100%;
}
#contentScroll {
	flex: 1 1 auto;
	overflow: auto;
	margin: 0px;
	background-color: white;
	padding: 0 20px 0 20px;
}
</style>