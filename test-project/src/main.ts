import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { setupVeeValidate } from '@/utils/validation'
import VueApexCharts from 'vue3-apexcharts'

import App from './App.vue'
import router from './router'

import './assets/css/tailwind.css'

const app = createApp(App)

setupVeeValidate();

app.use(createPinia())
app.use(router)
app.use(VueApexCharts)

app.mount('#app')
