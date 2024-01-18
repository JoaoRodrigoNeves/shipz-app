<script setup>
import { ref, onMounted, inject } from "vue"
import Chart from 'primevue/chart'
import { useRouter } from "vue-router"
import moment from "moment"

const sensors = ref([])
const axios = inject('axios')
const router = useRouter()
const isLoading = ref(false)
const observations = ref([])

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

const loadObservations = async () => {
  isLoading.value = true
  try {
    await axios.get('sensors/' + router.currentRoute.value.params.code + '/observations').then(response => {
      observations.value = response.data
      isLoading.value = false
    })

  } catch (error) {
    isLoading.value = false
    console.log(error)
  }
}

const formatDate = value => {
  return moment(String(value)).format('DD/MM/YYYY HH:mm:ss')
}

onMounted(async () => {
  await loadSensors()
  await loadObservations()
  chartData.value = setChartData()
  chartOptions.value = setChartOptions()
})

const chartData = ref()
const chartOptions = ref()

const setChartData = () => {
  const documentStyle = getComputedStyle(document.documentElement)
  
  return {
    labels: Object.values(observations.value.map(observation => formatDate(observation.createdAt))),
    datasets: [
      {
        label: 'Temperatura (ºC)',
        data: Object.values(observations.value.map(observation => observation.value)),
        fill: false,
        borderColor: documentStyle.getPropertyValue('--blue-500'),
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
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard style="padding: 20px;">
        <div class="product-catalog-details-header">
          <h2>Observações</h2>
        </div>
        <div class="card">
          <Chart
            type="line"
            :data="chartData"
            :options="chartOptions"
            class="h-30rem"
          />
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

.product-catalog-details .catalog-item label {
  opacity: 0.7;
  font-size: 14px;
}
</style>
