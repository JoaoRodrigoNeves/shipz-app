<script setup>
import { ref, onMounted, inject } from 'vue'
import OrdersTable from '@/views/pages/tables/OrdersTable.vue'
import OrderForm from '@/views/pages/form-layouts/OrderForm.vue'

const axios = inject('axios')
const isLoading = ref(false)
const orders = ref([])
const isUpdating = ref(false)
const isUpdatingStatus = ref(null)
const orderToUpdate = ref(null)

const loadOrders = async () => {
  isLoading.value = true
  if (JSON.parse(sessionStorage.getItem('user_info')).role == "LogisticOperator") {
    await axios.get('logistic-operators/' + JSON.parse(sessionStorage.getItem('user_info')).username + '/orders').then(response => {
      orders.value = response.data
      isLoading.value = false
    }).catch(
      error => {
        isLoading.value = false
        console.error(error)
      },
    )
  } else {
    await axios.get('final-costumers/' + JSON.parse(sessionStorage.getItem('user_info')).username + '/orders').then(response => {
      orders.value = response.data
      isLoading.value = false
    }).catch(
      error => {
        isLoading.value = false
        console.error(error)
      },
    )
  }
}

const updateOrder = async (order, flagStatus) => {
  orderToUpdate.value = order
  isUpdating.value = true
  isUpdatingStatus.value = flagStatus
}

const closeFormAndUpdate = async () => {
  isUpdating.value = false
  isUpdatingStatus.value = null
  orderToUpdate.value = null
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
        <div class="orders-header">
          <h2>Encomendas</h2>
        </div>
        <div v-if="!isUpdating">
          <OrdersTable v-if="orders && orders.length > 0 &&!isLoading" :orders="orders" @updateOrder="updateOrder" @loadOrders="loadOrders" />
            <div v-else class="no-orders">
              Não há encomendas registadas
            </div>
        </div>
        <div v-else class="orders-form">
          <OrderForm @closeFormAndUpdate="closeFormAndUpdate" :order="orderToUpdate"
            :is-updating-status="isUpdatingStatus" />
        </div>
      </VCard>
    </VCol>
  </VRow>
</template>

<style scoped>
.orders-header {
  display: flex;
  justify-content: space-between;
  align-self: center;
  padding: 24px;
}

.orders-form {
  padding: 20px;
}

.no-orders {
    padding: 0 24px 24px 24px;
}
</style>
