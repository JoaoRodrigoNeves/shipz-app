<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import ProductTable from '@/views/pages/tables/ProductTableOrders.vue'
import { useToast } from "primevue/usetoast";

const toast = useToast()  
const axios = inject('axios')
const router = useRouter()
const isLoading = ref(false)
const order = ref([])
const products = ref([]);
const cities = ref([]);

const loadOrderDetails = async () => {
  isLoading.value = true
  try {
    await axios.get('clientOrders/' + router.currentRoute.value.params.code).then(response => {
      order.value = response.data
      products.value = response.data.productsDTO
      isLoading.value = false
    })
  } catch (error) {
    isLoading.value = false
    console.log(error)
  }
}

const loadCities = async () => {
  isLoading.value = true
  try {
    await axios.get('https://json.geoapi.pt/municipios').then(response => {
      cities.value = response.data
      isLoading.value = false
    })
    
  } catch (error) {
    isLoading.value = false
    console.log(error)
  }
}

const changeLocation = async () => {
  isLoading.value = true
  try {
    await axios.patch('clientOrders/' + router.currentRoute.value.params.code +'/changeLocation/' + order.value.location).then(response => {
      isLoading.value = false
      toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Localização alterada com sucesso', life: 3000, });
    })

  } catch (error) {
    isLoading.value = false
    console.log(error)
  }
}



onMounted(async () => {
  await loadOrderDetails()
  await loadCities()
})
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard style="padding: 20px;">
        <div class="product-catalog-details-header">
          <h2>Encomenda nº{{ order.code }}</h2>
        </div>
        <div class="w-50 my-5">
          <VAutocomplete v-model="order.location" label="Localização" placeholder="Selecionar Localização" :items="cities" @update:model-value="changeLocation()" />
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