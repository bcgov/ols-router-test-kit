////https://stackoverflow.com/questions/65547199/using-bootstrap-5-with-vue-3
// import 'bootstrap'
// import "bootstrap/dist/css/bootstrap.min.css"
import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router.js'
// import vSelect from 'vue-select'
// import 'vue-select/dist/vue-select.css'
// import VueUniversalModal from 'vue-universal-modal'
// import 'vue-universal-modal/dist/index.css'

const app = createApp(App)
    // .component('v-select', vSelect)
    // .use(VueUniversalModal, {
    //     teleportTarget: '#modals'
    // })
    .use(router)
    .mount('#app');