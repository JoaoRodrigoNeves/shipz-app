<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import ProductTable from '@/views/pages/tables/ProductTableOrders.vue'

const axios = inject('axios')
const router = useRouter()
const isLoading = ref(false)
const order = ref([])
const products = ref([])

const loadOrderDetails = async () => {
  isLoading.value = true

  await axios.get('clientOrders/' + router.currentRoute.value.params.code).then(response => {
    order.value = response.data
    products.value = response.data.productsDTO
    isLoading.value = false
  }).catch(
    error => {
      isLoading.value = false;
      console.error(error)
    }
  )
}

onMounted(async () => {
  await loadOrderDetails()
})
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard style="padding: 20px;">
        <div class="product-catalog-details-header">
          <h2>Encomenda nº{{ order.code }}</h2>
        </div>
        <div class="products-actions">
          <h3>Produtos</h3>
        </div>
        <div v-if="products && products.length > 0 && !isLoading">
          <ProductTable v-if="!isLoading" :products="products" />
        </div>
        <div v-else class="no-products">
          Não tem produtos associados a esta encomenda
        </div>
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