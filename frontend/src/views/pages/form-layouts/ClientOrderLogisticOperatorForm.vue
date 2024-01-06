<script setup>
import { ref, onMounted, inject } from 'vue'
import { useToast } from 'primevue/usetoast';

const axios = inject('axios')

const toast = useToast()

const emit = defineEmits(['addLogisticOperator'])
const props = defineProps({
  clientOrder: {
    type: Object,
    required: true
  }
})

const logisticOperators = ref([])
const logisticOperatorSelected = ref(null)
const isLoading = ref(false)

const loadLogisticOperators = async () => {
  isLoading.value = true

  await axios.get('logisticOperators').then(response => {
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

  await axios.patch('/clientOrders/' + props.clientOrder.code + '/changeLogistic/' + logisticOperatorSelected.value).then(response => {
    isLoading.value = false;
    if (response.status == 200) {
      toast.add({ severity: 'success', summary: 'Sucesso', detail: (props.clientOrder.logisticOperator ? 'Operador logistico alterado com sucesso na encomenda #' : 'Operador logistico adicionado com sucesso à encomenda #') + props.clientOrder.code, life: 3000 });
      emit('addLogisticOperator')
    }
  }).catch(
    error => {
      isLoading.value = false;
      console.error(error)
    }
  )
})

onMounted(async () => {
  await loadLogisticOperators()
  logisticOperatorSelected.value = props.clientOrder.logisticOperator
})

</script>

<template>
  <VForm @submit.prevent="save">
    <VRow>
      <VCol cols="12">
        <VAutocomplete v-model="logisticOperatorSelected" label="Operadores Logisticos"
          placeholder="Selecionar Operador Logistico" :items="logisticOperators" item-title="name"
          item-value="username" />
      </VCol>
      <VCol cols="12" class="d-flex gap-4">
        <VBtn type="submit">
          Adicionar Operador Logistico
        </VBtn>

        <VBtn type="reset" color="secondary" variant="tonal">
          Reset
        </VBtn>
      </VCol>
    </VRow>
  </VForm>
</template>