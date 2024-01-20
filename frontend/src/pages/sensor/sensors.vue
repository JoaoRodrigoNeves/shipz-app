<script setup>
import SensorsTable from '@/views/pages/tables/SensorsTable.vue';
import SensorForm from '@/views/pages/form-layouts/SensorForm.vue'
import { onMounted } from 'vue';
import { ref, inject } from 'vue'

const axios = inject('axios')
const isLoading = ref(false)
const isCreating = ref(false)
const sensors = ref([])

const loadSensors = async () => {
    isLoading.value = true;
    await axios.get('sensors').then(response => {
        isLoading.value = false;
        sensors.value = response.data
    }).catch(
        error => {
            isLoading.value = false;
            console.error(error)
        }
    )
}

const closeForm = async () => {
    isCreating.value = false
    await loadSensors()
}

onMounted(async () => {
    await loadSensors();
})

</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard v-if="!isCreating">
                <div class="sensors-header">
                    <h2>Sensores</h2>
                    <VBtn rel="noopener noreferrer" color="primary" @click="isCreating = true">
                        <VIcon size="20" icon="bx-plus" />
                        <VTooltip activator="parent" location="top">
                            <span>Criar Sensor</span>
                        </VTooltip>
                    </VBtn>
                </div>
                <SensorsTable v-if="sensors && sensors.length > 0 && !isLoading" @loadSensors="loadSensors" :sensors="sensors" />
              <div
                v-else
                class="no-data"
              >
                Não tem sensores registados
              </div>
            </VCard>
            <VCard v-else>
                <VCard>
                    <div class="sensors-header">
                        <h2>Criar Sensor</h2>
                    </div>
                    <VCardText>
                        <SensorForm @closeForm="closeForm" :isCreating="isCreating"></SensorForm>
                    </VCardText>
                </VCard>
            </VCard>
        </VCol>
    </VRow>
</template>

<style scoped>
.sensors-header {
    display: flex;
    justify-content: space-between;
    align-self: center;
    padding: 24px;
}

.no-data {
  padding: 0 24px 24px 24px;
}
</style>
