<script setup>
import { ref, onMounted, inject } from 'vue'
import OrdersTable from '@/views/pages/tables/OrdersTable.vue'
import ClientOrderLogisticOperatorForm from '@/views/pages/form-layouts/ClientOrderLogisticOperatorForm.vue'

const axios = inject('axios')
const isLoading = ref(false)
const orders = ref([])
const isUpdating = ref(false)
const clientOrderToUpdate = ref(null)
const loadOrders = async () => {
  isLoading.value = true
  if (JSON.parse(sessionStorage.getItem('user_info')).role == "ProductManufacter") {
    try {
      await axios.get('clientOrders').then(response => {
        orders.value = response.data
        isLoading.value = false
      })
    } catch (error) {
      isLoading.value = false
      console.log(error)
    }
  } else {
    try {
      await axios.get('logisticOperators/' + JSON.parse(sessionStorage.getItem('user_info')).username).then(response => {
        orders.value = response.data.clientOrdersDTO
        isLoading.value = false
      })
    } catch (error) {
      isLoading.value = false
      console.log(error)
    }
  }
}


const updateLogisticOperator = (product) => {
  clientOrderToUpdate.value = product
  isUpdating.value = true
}
const addLogisticOperator = async () => {
  isUpdating.value = false
  await loadOrders()

}

onMounted(async () => {
  await loadOrders()
})
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard>
        <div class="client-orders-header">
          <h2>Encomendas</h2>
        </div>
        <div v-if="!isUpdating">
          <OrdersTable v-if="!isLoading" @updateLogisticOperator="updateLogisticOperator" @loadOrders="loadOrders"
            :orders="orders" />
        </div>
        <div v-else class="client-orders-form">
          <ClientOrderLogisticOperatorForm :clientOrder="clientOrderToUpdate" @addLogisticOperator="addLogisticOperator"></ClientOrderLogisticOperatorForm>
        </div>
      </VCard>
    </VCol>
  </VRow>
</template>

<style scoped>
.client-orders-header {
  display: flex;
  justify-content: space-between;
  align-self: center;
  padding: 24px;
}

.client-orders-form{
  padding: 20px;
}
</style>