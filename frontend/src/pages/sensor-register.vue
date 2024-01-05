
<script setup>
import { ref, onMounted, inject } from 'vue';
import { useToast } from "primevue/usetoast";

const axios = inject('axios')
const toast = useToast();
const isLoading = ref(false);
const sensors = ref([]);
const sensorForm = ref({
    sensorCode: null,
    value: null
})

const loadSensors = async () => {
    isLoading.value = true
    try {
        await axios.get('sensors').then(response => {
            sensors.value = response.data
            isLoading.value = false
        })
    } catch (error) {
        isLoading.value = false
        console.log(error)
    }
}

const save = async () => {
    isLoading.value = true
    let payload = {
        sensorCode: sensorForm.value.sensorCode,
        value: sensorForm.value.value - 0
    }
    await axios.post('observations', payload)
        .then(response => {
            toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Observação criada com sucesso no sensor #'+ sensorForm.value.sensorCode + ' (' + getSensorInfo().sensorTypeName + ')', life: 3000 });
            isLoading.value = false
            reset()
        }).catch(error => {
            isLoading.value = false
            console.error(error)
        })
}

const getSensorInfo = () => {
    return sensors.value.find(e => e.code == sensorForm.value.sensorCode)
}
const reset = () => {
    sensorForm.value = {
        sensorCode: null,
        value: null
    }
}


onMounted(async () => {
    await loadSensors();
})
</script>
<template>
    <VCard>
        <div class="sensors-container">
            <div class="sensor-container-header">
                <div class="title">
                    Leitura do Sensor
                </div>
                <div class="sensor-submit">
                    <VBtn color="secondary" variant="tonal" type="reset" @click="reset">
                        Repor
                    </VBtn>
                    <VBtn rel="noopener noreferrer" color="primary" @click="save">
                        Submeter
                    </VBtn>
                </div>
            </div>
            <div>
                <VForm class="input-form">
                    <VAutocomplete v-model="sensorForm.sensorCode" :items="sensors" label="Sensor"
                        placeholder="Selecionar Sensor" item-title="sensorTypeName" item-value="code">
                        <template v-slot:item="{ props, item }">
                            <VListItem v-bind="props" :title="item.raw.sensorTypeName" :subtitle="item.raw.code">
                            </VListItem>
                        </template>
                    </VAutocomplete>
                    <div style="width: 40%;">
                        <VTextField type="number" v-model="sensorForm.value" label="Valor"
                            placeholder="Inserir o Valor do Sensor" />
                    </div>


                    <span v-if="sensorForm.sensorCode">{{ getSensorInfo().type === "TEMPERATURE" ? 'ºC' :
                        getSensorInfo().type
                            === "PRESSURE" ? 'bar' : '%' }}</span>
                </VForm>
            </div>

        </div>
    </VCard>
</template>
  
<style>
.sensors-container {
    padding: 20px;
}

.sensors-container .input-form {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 16px
}

.sensors-container .sensor-container-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.sensors-container .sensor-container-header .title {
    font-size: 18px;
    width: 200px;
}

.sensors-container .sensor-container-header .sensor-submit {
    display: flex;
    justify-content: end;
    width: 100%;
    gap: 16px;
}

.sensors-container .sensors-list {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 16px;
    height: 100%;
}


.sensors-container .sensors-list .sensor-item {
    background-color: #fff;
    box-shadow: 0 0 0 2px #ffffff, 0 0 0 4px rgb(172, 169, 169), 0 1px 2px 0 black;
    padding: 10px;
    border-radius: 8px;
    width: 22%;
    position: relative;
    cursor: pointer;
}

.sensors-container .sensors-list .sensor-item .icon-check {
    position: absolute;
    top: -10px;
    right: -10px;
    background-color: #fff;
    border-radius: 100%;
}

.sensors-container .sensors-list .sensor-item .sensor-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.sensors-container .sensors-list .sensor-item .sensor-content {
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: 700;
    color: #00000080;
    height: 100px;
    font-size: 18px;
}

.sensors-container .sensors-list .sensor-item.checked {
    background-color: rgba(0, 128, 11, 0.15);
}

.sensors-container .sensors-list .sensor-item .sensor-footer {
    text-align: center;
}
</style>
  