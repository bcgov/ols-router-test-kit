import { createRouter, createWebHistory } from 'vue-router'

import TestRuns from './views/TestRuns.vue'
import BulkTests from './views/BulkTests.vue'
import CustomTests from './views/CustomTests.vue'
import Results from './views/Results.vue'
import Test from './views/Test.vue'
import Run from './views/Run.vue'
import CompareRuns from './views/CompareRuns.vue'
import CompareToRefs from './views/CompareToRefs.vue'
import CreateNew from './views/CreateNew.vue'
import CreateRun from './views/CreateRun.vue'
import List from './views/List.vue'
import MapView from './views/MapView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/runs',
      name: 'runs',
      component: TestRuns
    },
    {
      path: '/bulkTests',
      name: 'bulkTests',
      component: BulkTests
    },
    {
      path: '/customTests',
      name: 'customTests',
      component: CustomTests
    },
    {
      path: '/results',
      name: 'results',
      component: Results
    },
    {
      path: '/test/:testId',
      name: 'test',
      props: (route) => ({ testId: Number(route.params.testId) }),
      component: Test
    },
    {
      path: '/run/:runId',
      name: 'run',
      props: (route) => ({ runId: Number(route.params.runId) }),
      component: Run
    },
    {
      path: '/compareRuns/:runIdA/:runIdB',
      name: 'compareRuns',
      props: (route) => ({ runIdA: Number(route.params.runIdA) , runIdB: Number(route.params.runIdB)}),
      component: CompareRuns
    },
    {
      path: '/compareToRefs/:runId',
      name: 'compareToRefs',
      props: (route) => ({ runId: Number(route.params.runId) }),
      component: CompareToRefs
    },
    {
      path: '/createNew/',
      name: 'createNew',
      component: CreateNew
    },
    {
      path: '/createRun/',
      name: 'createRun',
      component: CreateRun
    },
    {
      path: '/list/:objectName',
      name: 'list',
      props: (route) => ({ objectName: String(route.params.objectName) }),
      component: List
    },
    {
      path: '/map/:resultId*',
      name: 'map',
      props: (route) => ({ resultId:route.params.resultId }),
      component: MapView
    },
  ]
})

export default router;
