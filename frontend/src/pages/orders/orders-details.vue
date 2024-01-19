<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import ProductTable from '@/views/pages/tables/ProductTableOrders.vue'
import { useToast } from "primevue/usetoast"
import moment from "moment/moment"
import TransportPackageTable from '@/views/pages/tables/TransportPackageTable.vue'
import SensorTable from '@/views/pages/tables/SensorsTable.vue'

const toast = useToast()
const axios = inject('axios')
const router = useRouter()
const isLoading = ref(false)
const order = ref([])

const sensors = ref([])
const selectedSensor = ref(null)
const selectedTransportPackages = ref(null)
const products = ref([])
const transportPackages = ref([])
const transportPackagesCatalog = ref([])
const cities = ref([])
const role = JSON.parse(sessionStorage.getItem('user_info')).role
const isDialogTransportPackageOpen = ref(false)
const formatDate = value => {
  return moment(String(value)).format('DD/MM/YYYY HH:mm:ss')
}

const loadOrderDetails = async () => {
  isLoading.value = true

  await axios.get('orders/' + router.currentRoute.value.params.code).then(response => {
    order.value = response.data
    isLoading.value = false
  }).catch(
    error => {
      isLoading.value = false
      console.error(error)
    },
  )
}

const loadTransportPackages = async () => {
  isLoading.value = true

  await axios.get('orders/' + router.currentRoute.value.params.code + '/transport-packages',
  ).then(response => {
    transportPackages.value = response.data
    isLoading.value = false
  }).catch(
    error => {
      isLoading.value = false
      console.error(error)
    },
  )
}

const loadTransportPackagesCatalog = async () => {
  isLoading.value = true

  await axios.get('transport-package-catalogs'
  ).then(response => {
    transportPackagesCatalog.value = response.data
    isLoading.value = false
  }).catch(
    error => {
      isLoading.value = false
      console.error(error)
    },
  )
}

const loadProducts = async () => {
  isLoading.value = true
  await axios.get('orders/' + router.currentRoute.value.params.code + '/products',
  ).then(response => {
    isLoading.value = false
    products.value = response.data
  }).catch(error => {
    isLoading.value = false
    console.error(error)
  })
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

const loadSensors = async () => {
  isLoading.value = true
  try {
    await axios.get('sensors').then(response => {
      const allSensors = response.data;
      const filteredSensors = allSensors.filter(sensor => !sensor.inUse);
      sensors.value = filteredSensors;
      isLoading.value = false;
    })

  } catch (error) {
    isLoading.value = false
    console.log(error)
  }
}

const addTransportPackageToOrder = async () => {
  isLoading.value = true
  let payload = {
    orderCode: order.value.code,
    transportPackageCatalogCode: selectedTransportPackages.value,
  }
  try {
    await axios.post('transport-packages', payload).then(response => {
      if(response.status == 201){
        toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Embalagem de Transporte adicionada com sucesso', life: 3000 })
      }else{
      toast.add({ severity: 'warn', summary: 'Atenção', detail: 'Embalagem de Transporte adicionada com sucesso, apesar de não ser necessária', life: 3000 })

      }
    })
    isLoading.value = false
    await loadTransportPackages()
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

const goBack = () => {
  router.back()
}

onMounted(async () => {
  await loadOrderDetails();
  await loadTransportPackages();
  await loadProducts();
  await loadCities();
  await loadSensors();
  await loadTransportPackagesCatalog();
})
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard>
        <div class="product-catalog-details-header">
          <h2>Encomenda nº{{ order.code }}</h2>
        </div>

        <div class="product-catalog-details">
          <div class="catalog-item">
            <label>
              Cliente
            </label>
            <span>
              {{ order.finalCostumerName }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Operador Logístico
            </label>
            <span>
              {{ order.logisticOperatorName }}
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
              {{ order.location ? order.location : 'Sem localização definida' }}
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
              Data de Entrega
            </label>
            <span>
              {{ order.deliveredAt ? formatDate(order.deliveredAt) : 'Não entregue' }}
            </span>
          </div>
          <div class="catalog-item" style="margin-top: 4px; width: 300px;" v-if="role == 'LogisticOperator'">
            <span>
              <VAutocomplete v-model="order.location" label="Localização" :items="cities" class="product-quantity"
                @update:model-value="changeLocation" />
            </span>
          </div>
        </div>
        <VExpansionPanels>
          <VExpansionPanel v-if="role != 'FinalCostumer'">
            <VExpansionPanelTitle>
              <div class="table-actions">
                <h3>Embalagens de Transporte</h3>

                <VDialog width="500" v-model="isDialogTransportPackageOpen">
                  <template v-slot:activator="{ props }">
                    <VBtn v-bind="props" text="Adicionar Sensores">
                      <VIcon size="20" icon="bx-plus" />
                      <VTooltip activator="parent" location="top">
                        <span>Adicionar Embalagem de Transporte</span>
                      </VTooltip>
                    </VBtn>
                  </template>
                  <v-card title="Embalagem de Transporte">
                    <v-card-text>
                      <VAutocomplete v-model="selectedTransportPackages" label="Embalagem de Transporte"
                        :items="transportPackagesCatalog" item-title="name" item-value="code" />
                    </v-card-text>

                    <v-card-actions>
                      <v-spacer></v-spacer>

                      <v-btn text="Adicionar"
                        @click="addTransportPackageToOrder(); isDialogTransportPackageOpen = false;"></v-btn>
                    </v-card-actions>
                  </v-card>
                </VDialog>
              </div>
            </VExpansionPanelTitle>
            <VExpansionPanelText>
              <div v-if="transportPackages && transportPackages.length > 0 && !isLoading">
                <TransportPackageTable v-if="!isLoading" :transport-packages="transportPackages" :canDelete="true"
                  @loadTransportPackages="loadTransportPackages" :order="order" />
              </div>
              <div
                v-else
                class="no-data"
              >
                Não tem embalagens de transporte associados a esta encomenda
              </div>
            </VExpansionPanelText>
          </VExpansionPanel>
          <VExpansionPanel>
            <VExpansionPanelTitle>
              <div class="table-actions">
                <h3>Produtos</h3>
              </div>
            </VExpansionPanelTitle>
            <VExpansionPanelText>
              <div v-if="products && products.length > 0 && !isLoading">
                <ProductTable
                  v-if="!isLoading"
                  :products="products"
                />
              </div>
              <div
                v-else
                class="no-data"
              >
                Não tem produtos associados a esta encomenda
              </div>
            </VExpansionPanelText>
          </VExpansionPanel>
          <VExpansionPanel>
            <VExpansionPanelTitle>
              <div class="table-actions">
                <h3>Sensores</h3>
              </div>
            </VExpansionPanelTitle>
            <VExpansionPanelText>
              <div v-if="sensors && sensors.length > 0 && !isLoading">
                <SensorTable
                  v-if="!isLoading"
                  :sensors="sensors"
                />
              </div>
              <div
                v-else
                class="no-data"
              >
                Não tem sensores associados a esta encomenda
              </div>
            </VExpansionPanelText>
          </VExpansionPanel>
        </VExpansionPanels>
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
  margin-bottom: 24px;
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

.table-actions {
  display: flex;
  justify-content: space-between;
  padding: 24px;
  width: 100%;
}

.no-data {
  padding: 0 24px 24px 24px;
}
</style>

