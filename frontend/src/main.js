/* eslint-disable import/order */
import "primevue/resources/themes/lara-light-teal/theme.css";
import "primevue/resources/primevue.min.css";
import "primeicons/primeicons.css";
import 'primevue/resources/themes/lara-light-green/theme.css'
import '@/@iconify/icons-bundle'
import App from '@/App.vue'
import vuetify from '@/plugins/vuetify'
import { loadFonts } from '@/plugins/webfontloader'
import router from '@/router'

import '@core/scss/template/index.scss'
import '@layouts/styles/index.scss'
import '@styles/styles.scss'

import { createPinia } from 'pinia'
import { createApp } from 'vue'
import axios from 'axios'
import PrimeVue from 'primevue/config';
import ToastService from 'primevue/toastservice';
import Toast from 'primevue/toast';
import ConfirmationService from 'primevue/confirmationservice';
import ConfirmDialog from 'primevue/confirmdialog';
loadFonts()
const apiDomain = import.meta.env.VITE_API_DOMAIN

// Create vue app
const app = createApp(App)
app.provide('serverBaseUrl', apiDomain)
app.provide(
    'axios',
    axios.create({
        baseURL: apiDomain + '/projeto/api',
        headers: {
            'Content-type': 'application/json'
        }
    })
)

// Use plugins
app.use(vuetify)
app.use(createPinia())
app.use(router)
app.use(PrimeVue);
app.use(ToastService);

app.component('Toast', Toast);
app.component('ConfirmDialog', ConfirmDialog);
app.use(ConfirmationService);

// Mount vue app
app.mount('#app')
