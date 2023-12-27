<script setup>
  import {ref, inject, onMounted} from 'vue'

import { useTheme } from 'vuetify'
import { hexToRgb } from '@layouts/utils'

const { global } = useTheme()
const axios = inject('axios')

onMounted(async () => {
    try {
      const token = JSON.parse(sessionStorage.getItem("token"))
      if (!token) {
        return
      }
      axios.defaults.headers.common.Authorization = 'Bearer ' + token
    } catch (error) {
      console.log(error)
    }
  })
</script>

<template>
  <Toast/>
  <ConfirmDialog></ConfirmDialog>
  <VApp :style="`--v-global-theme-primary: ${hexToRgb(global.current.value.colors.primary)}`">
    <RouterView />
  </VApp>
</template>
