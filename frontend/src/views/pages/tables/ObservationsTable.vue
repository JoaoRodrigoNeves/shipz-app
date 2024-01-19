<script setup>
import { ref } from 'vue'
import moment from 'moment'

const emit = defineEmits(['loadObservations',])

const props = defineProps({
  observations: {
    type: Object,
    required: true
  }
})

const observations = ref(Object.assign({}, props.observations))

const formatDate = (value) => {
    return moment(String(value)).format('DD/MM/YYYY HH:mm:ss')
}

watch(
  () => props,
  (newProps) => {
    observations.value = Object.assign({}, newProps.observations)
  },
  { immediate: true }
)
</script>

<template>
  <VTable fixed-header >
    <thead>
      <tr>
        <th class="text-uppercase">
          
        </th>
        <th>
          Criado
        </th>
      </tr>
    </thead>

    <tbody>
      <tr v-for="item in observations" :key="item.code">
        <td style="width: 15%;">
          Observação: {{ item.value }}
        </td>
        <td style="width: 20%; text-align: center;">
          {{ formatDate(item.createdAt) }}
        </td>
      </tr>
    </tbody>
  </VTable>
</template>
<style scoped></style>