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

const logisticOperators = ref([])
const logisticOperatorSelected = ref(null)
const isLoading = ref(false)

const loadLogisticOperators = async () => {
  isLoading.value = true

  await axios.get('logistic-operators').then(response => {
    logisticOperators.value = response.data
    isLoading.value = false
  }).catch(
    error => {
      isLoading.value = false;
      console.error(error)
    }
  )

}

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
  } else {
    await axios.patch('orders/' + props.order.code + '/logistic-operator',
      {
        logisticOperator: logisticOperatorSelected.value
      }).then(response => {
        isLoading.value = false;
        if (response.status == 200) {
          toast.add({ severity: 'success', summary: 'Sucesso', detail: (props.order.logisticOperator ? 'Operador logistico alterado com sucesso na encomenda #' : 'Operador logistico adicionado com sucesso à encomenda #') + props.order.code, life: 3000 });
          emit('closeFormAndUpdate')
        }
      }).catch(
        error => {
          toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema!', life: 3000 });
          isLoading.value = false;
          console.error(error)
        }
      )
  }
})

onMounted(async () => {
  await loadLogisticOperators()
  logisticOperatorSelected.value = props.order.logisticOperator
})

</script>

<template>
  <VForm @submit.prevent="save">
    <VRow>
      <VCol v-if="!props.isUpdatingStatus" cols="12">
        <VAutocomplete v-model="logisticOperatorSelected" label="Operadores Logisticos"
          placeholder="Selecionar Operador Logistico" :items="logisticOperators" item-title="name"
          item-value="username" />
      </VCol>
      <VCol v-if="props.isUpdatingStatus" cols="12">
        <VSelect v-model="statusSelected" label="Estado" placeholder="Selecionar Estado"
          :items="['Estado Inicial', 'Em Processamento', 'Enviada', 'Entregue']" />
      </VCol>
      <VCol cols=" 12" class="d-flex gap-4">
        <VBtn type="submit">
          {{ props.isUpdatingStatus ? 'Atualizar Estado' : 'Atualizar Operador Logistico' }}
        </VBtn>

        <VBtn type="reset" color="secondary" variant="tonal">
          Reset
        </VBtn>
      </VCol>
    </VRow>
  </VForm>
</template>