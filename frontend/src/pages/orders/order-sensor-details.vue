<script setup>
import { ref, onMounted, inject } from "vue"
import Chart from 'primevue/chart'
import { useRouter } from "vue-router"
import moment from "moment"

const sensors = ref([])
const axios = inject('axios')
const router = useRouter()
const sensor = ref([])
const isLoading = ref(false)
const observations = ref([])
const cities = ref([])

const loadSensors = async () => {
  isLoading.value = true
  try {
    await axios.get('sensors').then(response => {

      const allSensors = response.data

      const filteredSensors = allSensors.filter(sensor => !sensor.inUse)

      sensors.value = filteredSensors
      isLoading.value = false
    })

  } catch (error) {
    isLoading.value = false
    console.log(error)
  }
}

const loadSensorDetails = async () => {
  isLoading.value = true
  try {
    const response = await axios.get('sensors/' + router.currentRoute.value.params.code)

    sensor.value = response.data
  } catch (error) {
    console.log(error)
  } finally {
    isLoading.value = false
  }
}

const loadObservations = async () => {
  isLoading.value = true
  try {
    const response = await axios.get('sensors/' + router.currentRoute.value.params.code + '/observations')

    observations.value = response.data
  } catch (error) {
    console.log(error)
  } finally {
    isLoading.value = false
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

const formatDate = value => moment(String(value)).format('DD/MM/YYYY HH:mm:ss')

onMounted(async () => {
  await loadSensors()
  await loadSensorDetails()
  await loadObservations()
  await loadCities()
  chartData.value = setChartData()
  chartOptions.value = setChartOptions()
})

const chartData = ref()
const chartOptions = ref()

const setChartData = () => {
  if (!sensor.value || observations.value.length === 0) {
    return { labels: [], datasets: [] }
  }

  const dataType = sensor.value.type

  return {
    labels: Object.values(observations.value.map(observation => formatDate(observation.createdAt))),
    datasets: [
      {
        label: dataType,
        data: Object.values(observations.value.map(observation => observation.value)),
        fill: false,
        borderColor: getBorderColorByDataType(dataType),
        tension: 0.4,
      },
    ],
  }
}

const setChartOptions = () => {
  const documentStyle = getComputedStyle(document.documentElement)
  const textColor = documentStyle.getPropertyValue('--text-color')
  const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary')
  const surfaceBorder = documentStyle.getPropertyValue('--surface-border')

  return {
    maintainAspectRatio: false,
    aspectRatio: 0.6,
    plugins: {
      legend: {
        labels: {
          color: textColor,
        },
      },
    },
    scales: {
      x: {
        ticks: {
          color: textColorSecondary,
        },
        grid: {
          color: surfaceBorder,
        },
      },
      y: {
        ticks: {
          color: textColorSecondary,
        },
        grid: {
          color: surfaceBorder,
        },
      },
    },
  }
}

const getBorderColorByDataType = dataType => {
  const documentStyle = getComputedStyle(document.documentElement)
  switch (dataType) {
  case sensor.value.type = 'Temperatura':
    return documentStyle.getPropertyValue('--blue-500')
  case sensor.value.type = 'Humidade':
    return documentStyle.getPropertyValue('--green-500')
  case sensor.value.type = 'Pressão':
    return documentStyle.getPropertyValue('--red-500')
  default:
    return ''
  }
}
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard>
        <div class="product-catalog-details-header">
          <h2>Observações</h2>
        </div>
        <div class="card">
          <Chart
            v-if="observations && observations.length > 0"
            type="line"
            :data="chartData"
            :options="chartOptions"
            class="h-30rem"
          />
          <div
            v-else
            class="no-data"
          >
            Não tem observações associadas a esta encomenda
          </div>
        </div>
        <VTable fixed-header>
          <thead>
            <tr>
              <th class="text-uppercase">
                Localização
              </th>
              <th>
                Data
              </th>
            </tr>
          </thead>

          <tbody>
            <tr
              v-for="item in sensors"
              :key="item.code"
            >
              <td style="width: 20%;">
                {{ item.code }}
              </td>
              <td style="width: 100%; text-align: center;">
                {{ item.type }}
              </td>
              <td
                class="d-flex align-center justify-end gap-x-2"
                style="width: fit-content"
              >
                <VBtn
                  rel="noopener noreferrer"
                  color="primary"
                  @click="navigateTo('/order-sensor/' + item.code)"
                >
                  <VIcon
                    size="20"
                    icon="bx-show"
                  />
                  <VTooltip
                    activator="parent"
                    location="top"
                  >
                    <span>Ver Detalhes</span>
                  </VTooltip>
                </VBtn>
              </td>
            </tr>
          </tbody>
        </VTable>
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

.product-catalog-details .catalog-item label {
  opacity: 0.7;
  font-size: 14px;
}

.no-data {
  padding: 0 24px 24px 24px;
}
</style>
