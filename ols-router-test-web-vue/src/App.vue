<template>
  <Navigation/>
  <div id="contentScroll"><router-view /></div>
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
	flex: 1;
	overflow: auto;
	margin: 0px;
	background: var(--color-background);
	padding: 0 20px 0 20px;
	height: 100%;
}
</style>