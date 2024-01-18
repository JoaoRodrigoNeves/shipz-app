<script setup>
import { ref, inject } from 'vue'
import logo from '@images/logo.svg?raw'
import { useRouter } from 'vue-router'
import { useToast } from "primevue/usetoast"

const axios = inject('axios')
const router = useRouter()
const toast = useToast()
const isLoading = ref(false)

const form = ref({
  username: '',
  password: '',
})

const isPasswordVisible = ref(false)

const submit = (async () => {
  isLoading.value = true
  await await axios.post('auth/login', form.value).then(response => {
    axios.defaults.headers.common.Authorization = "Bearer " + response.data.token
    sessionStorage.setItem("token", JSON.stringify(response.data.token))
    sessionStorage.setItem("user_info", JSON.stringify(response.data.user_info))
    isLoading.value = false

    router.push({ path: 'dashboard' })
  },
  ).catch(
    error => {
      isLoading.value = false
      toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema ao entrar na aplicação!', life: 3000 })
    },
  )

})

const navigateToSensorObservation = () => {
  router.push({ path: '/sensor-observation' })
}
</script>

<template>
  <div class="auth-wrapper d-flex align-center justify-center pa-4">
    <VCard
      class="auth-card pa-4 pt-7"
      max-width="448"
    >
      <VCardItem class="justify-center">
        <template #prepend>
          <div class="d-flex">
            <div
              class="d-flex text-primary"
              v-html="logo"
            />
          </div>
        </template>

        <VCardTitle class="text-2xl font-weight-bold">
          sneat
        </VCardTitle>
      </VCardItem>

      <VCardText class="pt-2">
        <h5 class="text-h5 mb-1">
          Welcome to sneat! 👋🏻
        </h5>
        <p class="mb-0">
          Please sign-in to your account and start the adventure
        </p>
      </VCardText>

      <VCardText>
        <VForm @submit.prevent="submit">
          <VRow>
            <VCol cols="12">
              <VTextField v-model="form.username" autofocus label="Nome de Utilizador" type="text" />
            </VCol>
            <VCol cols="12">
              <VTextField v-model="form.password" label="Palavra Passe"
                :type="isPasswordVisible ? 'text' : 'password'"
                :append-inner-icon="isPasswordVisible ? 'bx-hide' : 'bx-show'"
                @click:append-inner="isPasswordVisible = !isPasswordVisible"
              />

              <VBtn
                block
                type="submit"
                class="mt-4 mb-4"
              >
                Login
              </VBtn>
            </VCol>
          </VRow>
        </VForm>
      </VCardText>
    </VCard>
  </div>
  <div style="position: absolute; bottom: 0; display: flex; justify-content: center; align-items: center; width: 100%;">
    <div style="width: fit-content;">
      <VBtn
        block
        type="submit"
        class="mt-4 mb-4"
        @click="navigateToSensorObservation"
      >
        Registar evento nos sensores
      </VBtn>
    </div>
  </div>
</template>

<style lang="scss">
@use "@core/scss/template/pages/page-auth.scss";
</style>
