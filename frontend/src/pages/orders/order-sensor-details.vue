<script setup>
import { ref, onMounted, inject } from "vue"
import Chart from 'primevue/chart'
import { useRouter } from "vue-router"
import moment from "moment"

const axios = inject('axios')
const router = useRouter()
const sensor = ref(null)
const isLoading = ref(false)
const observations = ref([])

const loadSensorDetails = async () => {
  isLoading.value = true
  try {
    const response = await axios.get('sensors/' + router.currentRoute.value.params.code)

    sensor.value = response.data
    console.log(response.data)
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

const goBack = () => {
  router.back()
}

const formatDate = value => moment(String(value)).format('DD/MM/YYYY HH:mm:ss')

onMounted(async () => {
  await loadSensorDetails()
  await loadObservations()
  if (sensor.value.type != 'Gps') {
    chartData.value = setChartData()
    chartOptions.value = setChartOptions()
  }
})

const chartData = ref()
const chartOptions = ref()

const setChartData = () => {
  if (!sensor.value || observations.value.length === 0) {
    return { labels: [], datasets: [] }
  }

  return {
    labels: Object.values(observations.value.map(observation => formatDate(observation.createdAt))),
    datasets: [
      {
        label: sensor.value.type,
        data: Object.values(observations.value.map(observation => observation.value)),
        fill: false,
        borderColor: getBorderColorByDataType(sensor.value.type),
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
          <VIcon size="35" icon="mdi-arrow-left-bold-circle" @click="goBack" />
          <h2>Observações</h2>
        </div>
        <div
          v-if="sensor && sensor.type != 'Gps'"
          class="card"
        >
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
        <div v-else>
          <VTable fixed-header v-if="observations">
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
                v-for="item in observations"
                :key="item.value"
              >
                <td style="width: 20%;">
                  {{ item.value }}
                </td>
                <td style="width: 100%; text-align: center;">
                  {{ formatDate(item.createdAt) }}
                </td>
              </tr>
            </tbody>
          </VTable>
        </div>
        <div
          v-else
          class="no-data"
        >
          Não tem observações de localização
        </div>
      </VCard>
    </VCol>
  </VRow>
</template>

<style scoped>
.product-catalog-details-header {
  display: flex;
  justify-content: start;
  align-items: center;
  padding: 24px;
  gap: 12px;
}

.product-catalog-details .catalog-item label {
  opacity: 0.7;
  font-size: 14px;
}

.no-data {
  padding: 0 24px 24px 24px;
}
</style>
