<script setup>
  import {ref, inject, onMounted} from 'vue'

import { useTheme } from 'vuetify'
import { hexToRgb } from '@layouts/utils'

const { global } = useTheme()
const axios = inject('axios')

onMounted(async () => {
    try {
      const user_info = JSON.parse(sessionStorage.getItem("user_info"))
      if (!user_info) {
        return
      }
      axios.defaults.headers.common.Authorization = 'Bearer ' + user_info.token
    } catch (error) {
      console.log(error)
    }
  })
</script>

<template>
  <Toast/>
  <VApp :style="`--v-global-theme-primary: ${hexToRgb(global.current.value.colors.primary)}`">
    <RouterView />
  </VApp>
</template>
