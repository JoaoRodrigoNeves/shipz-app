<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import moment from "moment/moment"

const props = defineProps({
  orders: {
    type: Object,
    required: true,
  },

})

const emit = defineEmits(['loadOrders', 'updateOrder'])
const router = useRouter()

const orders = ref(Object.assign({}, props.orders))

const navigateTo = path => {
  router.push({ path: path })
}

const formatDate = value => {
  return moment(String(value)).format('DD/MM/YYYY HH:mm:ss')
}

watch(
  () => props,
  newProps => {
    orders.value = Object.assign({}, newProps.orders)
  },
  { immediate: true },
)
</script>

<template>
  <VTable fixed-header>
    <thead>
      <tr>
        <th class="text-uppercase">
          Código de encomenda
        </th>
        <th>
          Operador Logistico
        </th>
        <th>
          Estado
        </th>
        <th>
          Data
        </th>
        <th>
          Ações
        </th>
      </tr>
    </thead>

    <tbody>
      <tr v-for="item in orders" :key="item.code">
        <td style="width: 20%;">
          {{ item.code }}
        </td>
        <td style="width: 30%; text-align: center;">
          {{ item.logisticOperatorName }}
        </td>

        <td style="width: 30%; text-align: center;">
          {{ item.status }}
        </td>
        <td style="width: 40%; text-align: center;">
          {{ formatDate(item.createdAt) }}
        </td>
        <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
          <VBtn rel="noopener noreferrer" color="primary" @click="navigateTo('order/' + item.code)">
            <VIcon size="20" icon="bx-show" />
            <VTooltip activator="parent" location="top">
              <span>Ver Detalhes</span>
            </VTooltip>
          </VBtn>
        </td>
      </tr>
    </tbody>
  </VTable>
</template>

<style scoped></style>
