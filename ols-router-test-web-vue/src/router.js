import { createRouter, createWebHistory } from 'vue-router'

import TestRuns from './views/TestRuns.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/runs',
      name: 'runs',
      component: TestRuns
    },
    // {
    //   path: '/tables/:tablekey',
    //   name: 'TableDetails',
    //   props: (route) => ({ tablekey: route.params.tablekey }),
    //   component: TableVue
    // },
  ]
})

export default router;
