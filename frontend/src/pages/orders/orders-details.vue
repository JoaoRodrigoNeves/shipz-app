<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import ProductTable from '@/views/pages/tables/ProductTableOrders.vue'
import { useToast } from "primevue/usetoast"
import moment from "moment/moment"

const toast = useToast()
const axios = inject('axios')
const router = useRouter()
const isLoading = ref(false)
const order = ref([])
const products = ref([])
const cities = ref([])
const dropdown = (JSON.parse(sessionStorage.getItem('user_info')).role == 'LogisticOperator')

const formatDate = value => {
  return moment(String(value)).format('DD/MM/YYYY hh:mm:ss')
}

const loadOrderDetails = async () => {
  isLoading.value = true

  await axios.get('orders/' + router.currentRoute.value.params.code).then(response => {
    order.value = response.data
    products.value = response.data.productsDTO
    isLoading.value = false
  }).catch(
    error => {
      isLoading.value = false
      console.error(error)
    },
  )
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
  let payload = {
    location: order.value.location,
  }
  try {
    await axios.patch('orders/' + router.currentRoute.value.params.code + '/location', payload).then(response => {
      isLoading.value = false
      toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Localização alterada com sucesso', life: 3000 })
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
        <div class="product-catalog-details">
          <div class="catalog-item">
            <label>
              Cliente
            </label>
            <span>
              {{ order.finalCostumer }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Operador Logístico
            </label>
            <span>
              {{ order.logisticOperator }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Estado
            </label>
            <span>
              {{ order.status }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Número de Produtos
            </label>
            <span v-if="!isLoading">
              {{ products.length }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Localização
            </label>
            <span>
              {{ order.location }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Data
            </label>
            <span>
              {{ formatDate(order.createdAt) }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Entregue
            </label>
            <span>
              {{ order.deliveredAt ? formatDate(order.deliveredAt) : 'Não entregue' }}
            </span>
          </div>
        </div>
        <div
          v-if="dropdown"
          class="w-50 my-5"
        >
          <VAutocomplete
            v-model="order.location"
            label="Localização"
            placeholder="Selecionar Localização"
            :items="cities"
            @update:model-value="changeLocation"
          />
        </div>
        <div class="products-actions">
          <h3>Produtos</h3>
        </div>
        <div v-if="products && products.length > 0 && !isLoading">
          <ProductTable
            v-if="!isLoading"
            :products="products"
          />
        </div>
        <div
          v-else
          class="no-products"
        >
          Não tem produtos associados a esta encomenda
        </div>
      </VCard>
    </VCol>
  </VRow>
</template>

<style scoped>
.product-catalog-details-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
}

.product-catalog-details {
  display: flex;
  padding: 0 24px;
  gap: 16px 0px;
  flex-wrap: wrap;
}

.product-catalog-details .catalog-item {
  display: flex;
  flex-direction: column;
  width: 50%;
}

.product-catalog-details .catalog-item label {
  opacity: 0.7;
  font-size: 14px;
}

.products-actions {
  display: flex;
  justify-content: space-between;
  padding: 24px;
}

.products-actions .product-quantity {
  display: flex;
  align-items: center;
  gap: 12px;
}

.no-products {
  padding: 0 24px 24px 24px;
}
</style>

