<script setup>
import { ref, onMounted, inject } from 'vue'
import { useToast } from 'primevue/usetoast';
import { VSelect } from 'vuetify/lib/components/index.mjs';

const axios = inject('axios')

const toast = useToast()

const emit = defineEmits(['closeFormAndUpdate'])
const props = defineProps({
  order: {
    type: Object,
    required: true
  },
  isUpdatingStatus: {
    type: Boolean,
    required: true
  }
})

const statusSelected = ref(props.order.status)
const isLoading = ref(false)

const save = (async () => {
  isLoading.value = true;
  if (props.isUpdatingStatus) {
    await axios.patch('orders/' + props.order.code + '/status',
      {
        status: statusSelected.value
      }).then(() => {
        toast.add({ severity: 'info', summary: 'Estado alterado', life: 3000 });
        isLoading.value = false
        emit('closeFormAndUpdate')
      }).catch(error => {
        toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema!', life: 3000 });
        isLoading.value = false;
        console.error(error)
      })
  }
})

onMounted(async () => {
})

</script>

<template>
  <VForm @submit.prevent="save">
    <VRow>
      <VCol v-if="props.isUpdatingStatus" cols="6">
        <VSelect v-model="statusSelected" label="Estado" placeholder="Selecionar Estado"
          :items="['Estado Inicial', 'Em Processamento', 'Enviada', 'Entregue']" />
      </VCol>
      <VCol cols="12" class="d-flex gap-4">
        <VBtn type="submit" :disabled="isLoading">
          Atualizar Estado
        </VBtn>
      </VCol>
    </VRow>
  </VForm>
</template>