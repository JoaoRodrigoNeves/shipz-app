<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import ProductTable from '@/views/pages/tables/ProductTableOrders.vue'
import { useToast } from "primevue/usetoast"
import moment from "moment/moment"
import TransportPackageTable from '@/views/pages/tables/TransportPackageTable.vue'
import SensorTable from '@/views/pages/tables/SensorsTable.vue'
import OrderForm from '@/views/pages/form-layouts/OrderForm.vue'

const toast = useToast()
const axios = inject('axios')
const router = useRouter()
const isLoading = ref(false)
const order = ref([])
const hasGpsSensor = ref(false)
const isUpdatingStatus = ref(false)
const sensors = ref([])
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
    loadOrderWithSensors()
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

  await axios.get('transport-package-catalogs',
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
    console.error(error)
  }
}

const loadOrderWithSensors = async () => {
  isLoading.value = true
  try {
    await axios.get('orders/' + router.currentRoute.value.params.code + '/sensors').then(response => {
      sensors.value = response.data
      hasGpsSensor.value = sensors.value.some(sensor => sensor.type == 'Gps')
      isLoading.value = false
    })

  } catch (error) {
    isLoading.value = false
    sensors.value = null
    console.error(error)
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
      if (response.status == 201) {
        toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Embalagem de Transporte adicionada com sucesso', life: 3000 })
      } else {
        toast.add({ severity: 'warn', summary: 'Atenção', detail: 'Embalagem de Transporte adicionada com sucesso, apesar de não ser necessária', life: 3000 })

      }
    })
    isLoading.value = false
    await loadTransportPackages()
  } catch (error) {
    isLoading.value = false
    console.error(error)
  }
}

const changeLocation = async () => {
  let gpsSensors = sensors.value.filter(s => s.type == "Gps");
  isLoading.value = true
  let success = false;
  gpsSensors.forEach(async (sensor) => {
    let payload = {
      sensorCode: sensor.code,
      value: order.value.location,
    }
    await axios.post('observations', payload)
      .then(response => {
        if (!success) {
          toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Localização adicionada com sucesso', life: 3000 });
          success = true
        }
      }).catch(error => {
        toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema ao alterar a localização', life: 3000 });
        console.error(error)
      })
  })

  isLoading.value = false
}

const goBack = () => {
  router.back()
}

const closeFormAndUpdate = async () => {
  isUpdatingStatus.value = false
  await loadOrderDetails()
}

onMounted(async () => {
  await loadOrderDetails()
  await loadTransportPackages()
  await loadProducts()
  await loadCities()
  await loadTransportPackagesCatalog()
})
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard v-if="!isUpdatingStatus">
        <div class="product-catalog-details-header">
          <div style="display: flex; align-items: center; gap:12px">
            <VIcon size="35" icon="mdi-arrow-left-bold-circle" @click="goBack" />
            <h2>Encomenda nº{{ order.code }}</h2>
          </div>
          <div style="display: flex; align-items: center; gap: 12px; ">
            <span style="color: rgb(255, 193, 7); font-size: 14px; text-decoration: underline;"
              v-if="!isLoading && (!transportPackages || transportPackages.length == 0)">
              Terá de adicionar pelo menos uma caixa de transporte.
            </span>
            <VBtn v-if="role == 'LogisticOperator' && order && order.status != 'Entregue'" rel="noopener noreferrer" color="primary"
              @click="isUpdatingStatus = true" :disabled="transportPackages && transportPackages.length == 0">
              <VIcon size="20" icon="mdi-package" />
              <VTooltip activator="parent" location="top">
                <span>Atualizar Estado</span>
              </VTooltip>
            </VBtn>
          </div>

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
          <div v-if="role == 'LogisticOperator' && order && (order.status == 'Em Processamento' || order.status == 'Enviada')" class="catalog-item" style="margin-top: 4px; width: 300px;">
            <span>
              <VAutocomplete v-if="hasGpsSensor" v-model="order.location" label="Localização" :items="cities"
                class="product-quantity" @update:model-value="changeLocation" />
            </span>
          </div>
        </div>
        <VExpansionPanels>
          <VExpansionPanel v-if="role != 'FinalCostumer'">
            <VExpansionPanelTitle>
              <div class="table-actions">
                <h3>Embalagens de Transporte</h3>

                <VDialog v-model="isDialogTransportPackageOpen" width="500">
                  <template #activator="{ props }">
                    <VBtn v-bind="props" v-if="order && order.status != 'Entregue'">
                      <VIcon size="20" icon="bx-plus" />
                      <VTooltip activator="parent" location="top">
                        <span>Adicionar Embalagem de Transporte</span>
                      </VTooltip>
                    </VBtn>
                  </template>
                  <VCard title="Embalagem de Transporte">
                    <VCardText>
                      <VAutocomplete v-model="selectedTransportPackages" label="Embalagem de Transporte"
                        :items="transportPackagesCatalog" item-title="name" item-value="code" />
                    </VCardText>

                    <VCardActions>
                      <VSpacer />

                      <VBtn text="Adicionar"
                        @click="addTransportPackageToOrder(); isDialogTransportPackageOpen = false;" />
                    </VCardActions>
                  </VCard>
                </VDialog>
              </div>
            </VExpansionPanelTitle>
            <VExpansionPanelText>
              <div v-if="transportPackages && transportPackages.length > 0 && !isLoading">
                <TransportPackageTable v-if="!isLoading" :transport-packages="transportPackages" :can-delete="true"
                  :order="order" @loadTransportPackages="loadTransportPackages" @load-sensors="loadOrderWithSensors" />
              </div>
              <div v-else class="no-data">
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
                <ProductTable v-if="!isLoading" :products="products" />
              </div>
              <div v-else class="no-data">
                Não tem produtos associados a esta encomenda
              </div>
            </VExpansionPanelText>
          </VExpansionPanel>
          <VExpansionPanel v-if="sensors && sensors.length > 0">
            <VExpansionPanelTitle>
              <div class="table-actions">
                <h3>Sensores</h3>
              </div>
            </VExpansionPanelTitle>
            <VExpansionPanelText>
              <div v-if="!isLoading">
                <SensorTable v-if="!isLoading" :sensors="sensors" />
              </div>
              <div v-else class="no-data">
                Não tem sensores associados a esta encomenda
              </div>
            </VExpansionPanelText>
          </VExpansionPanel>
        </VExpansionPanels>
      </VCard>
      <VCard v-else>
        <div class="orders-form">
          <div style="display: flex; align-items: center; gap:12px; margin-bottom: 24px;">
            <VIcon size="35" icon="mdi-arrow-left-bold-circle" @click="isUpdatingStatus = false" />
            <h2>Encomenda nº{{ order.code }}</h2>
          </div>
          <OrderForm @closeFormAndUpdate="closeFormAndUpdate" :order="order" :is-updating-status="true" />
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
  margin-bottom: 24px;
}

.orders-form {
  padding: 20px;

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

