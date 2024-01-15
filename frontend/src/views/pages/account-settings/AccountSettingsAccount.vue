<script setup>
import { ref, inject } from 'vue'
import { useToast } from "primevue/usetoast";
const toast = useToast();
const axios = inject('axios')
const isLoading = ref(false)
const user = ref(JSON.parse(sessionStorage.getItem('user_info')))

const accountDetails = ref({
  username: user.value.username,
  name: user.value.name,
  password: null,
  email: user.value.email
})

const accountDetailsLocal = ref(Object.assign({}, accountDetails.value))

const save = (async () => {
  isLoading.value = true

  await axios.put('product-manufacters', accountDetailsLocal.value).then(response => {
    if (response.status == 201) {
      let user_info = {
        username: response.data.username,
        email: response.data.email,
        role: user.value.role,
        name: response.data.name,
      }

      accountDetails.value = {
        username: response.data.username,
        email: response.data.email,
        password: null,
        name: response.data.name,
      }
      sessionStorage.setItem('user_info', JSON.stringify(user_info))
      isLoading.value = false

      toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Utilizador atualizado com sucesso', life: 3000 });
    }

  }).catch(
    error => {
      isLoading.value = false;
      toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um erro ao atualizar o utilizador', life: 3000 });
    }
  )
});

const resetForm = () => {
  accountDetailsLocal.value = Object.assign({}, accountDetails.value)
}

const isAccountDeactivated = ref(null)
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard title="Detalhes da Conta">

        <VDivider />

        <VCardText>
          <VForm class="mt-6">
            <VRow>
              <VCol md="6" cols="12">
                <VTextField v-model="accountDetailsLocal.name" placeholder="John" label="Nome" />
              </VCol>
              <VCol md="6" cols="12">
                <VTextField v-model="accountDetailsLocal.email" label="E-mail" placeholder="johndoe@gmail.com"
                  type="email" />
              </VCol>
              <VCol cols="12" class="d-flex flex-wrap gap-4">
                <VBtn @click="save">Guardar Alterações</VBtn>

                <VBtn color="secondary" variant="tonal" type="reset" @click.prevent="resetForm">
                  Reset
                </VBtn>
              </VCol>
            </VRow>
          </VForm>
        </VCardText>
      </VCard>
    </VCol>

    <VCol cols="12">
      <VCard title="Apagar conta">
        <VCardText>
          <div>
            <VCheckbox v-model="isAccountDeactivated" label="Confirmo que pretendo apagar a conta" />
          </div>

          <VBtn :disabled="!isAccountDeactivated" color="error" class="mt-3">
            Apagar Conta
          </VBtn>
        </VCardText>
      </VCard>
    </VCol>
  </VRow>
</template>
