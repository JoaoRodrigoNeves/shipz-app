<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from "primevue/usetoast";
import { useConfirm } from "primevue/useconfirm";
import ObservationsTable from '@/views/pages/tables/ObservationsTable.vue'

const axios = inject('axios')
const isLoading = ref(false)
const router = useRouter()
const confirm = useConfirm();
const toast = useToast();

const sensor = ref(null)
const observations = ref([])

const loadSensorDetails = async () => {
    isLoading.value = true;
    await axios.get('sensors/' + router.currentRoute.value.params.code).then(response => {
        isLoading.value = false;
        sensor.value = response.data
    }).catch(
        error => {
            isLoading.value = false;
            console.error(error)
        }
    )
}

const loadSensorObservations = async () => {
    isLoading.value = true;

    await axios.get('sensors/' + router.currentRoute.value.params.code + '/observations').then(response => {
        isLoading.value = false;
        observations.value = response.data
    }).catch(
        error => {
            isLoading.value = false;
            console.error(error)
        }
    )
}

onMounted(async () => {
    await loadSensorDetails();
    await loadSensorObservations();
})

</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard v-if="sensor">
                <div class="sensor-details-header">
                    <h2>{{ "Sensor - S" + sensor.code }}</h2>
                </div>
                <div class="sensor-details">
                    <div class="sensor-item">
                        <label>
                            Tipo de sensor:

                            <span class="bold">
                                {{ sensor.type }}
                            </span>
                        </label>
                    </div>
                </div>
                <br>
                <div v-if="observations && observations.length > 0 && !isLoading">
                    <ObservationsTable :observations="observations" />
                </div>
                <div v-else class="no-obervations">
                    Não existem observações para este sensor.
                </div>
            </VCard>
        </VCol>
    </VRow>
</template>
<style scoped>
.sensor-details-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24px 24px 10px;
}

.no-obervations {
    padding: 0 24px 24px 24px;
}

.sensor-details {
    display: flex;
    padding: 0 24px;
    gap: 16px 0px;
    flex-wrap: wrap;
}

.sensor-details .sensor-item {
    display: flex;
    flex-direction: column;
    width: 50%;
}
</style>