<script setup>
import { ref, onMounted, inject } from 'vue'
import OrdersTable from '@/views/pages/tables/OrdersTable.vue'

const axios = inject('axios')
const isLoading = ref(false)
const orders = ref([])


const loadOrders = async () => {
  isLoading.value = true
  try {
    await axios.get('logisticOperators/' + JSON.parse(sessionStorage.getItem('user_info')).username).then(response => {
      orders.value = response.data.clientOrdersDTO
      console.log(orders.value)
      isLoading.value = false
    })
  } catch (error) {
    isLoading.value = false
    console.log(error)
  }
}

onMounted(async () => {
  await loadOrders()
})
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard>
        <div class="product-catalogs-header">
          <h2>Encomendas</h2>
        </div>
        <OrdersTable v-if="!isLoading" @loadOrders="loadOrders" :orders="orders" />
      </VCard>
    </VCol>
  </VRow>
</template>

<style scoped>
.product-catalogs-header {
  display: flex;
  justify-content: space-between;
  align-self: center;
  padding: 24px;
}
</style>