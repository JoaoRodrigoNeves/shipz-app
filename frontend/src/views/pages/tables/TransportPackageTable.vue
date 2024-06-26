<script setup>
import { ref, watch, onMounted } from 'vue'
import { useToast } from 'primevue/usetoast'
import moment from 'moment'
import { useConfirm } from "primevue/useconfirm"

const props = defineProps({
  transportPackages: {
    type: Object,
    required: true,
  },
  order: {
    type: Object,
    required: false,
  },
  canDelete: {
    type: Boolean,
    required: false,
  },
})

const emit = defineEmits(['loadTransportPackages', 'loadSensors'])

const confirm = useConfirm()
const isLoading = ref(false)
const axios = inject('axios')
const toast = useToast()
const selectedSensors = ref(null)
const addSensors = ["Temperatura", "Humidade", "Pressão", "Gps", "Dano"]
const removeSensors = ref([])
const userRole = JSON.parse(sessionStorage.getItem('user_info')).role

const transportPackages = ref(Object.assign({}, props.transportPackages))

const formatDate = value => {
  return moment(String(value)).format('DD/MM/YYYY HH:mm:ss')
}

const removeTransportPackage = transportPackage => {
  confirm.require({
    message: 'Tem a certeza que pretende apagar a embalagem de transporte ' + transportPackage.code + ' ?',
    header: 'Apagar Embalagem de Transporte',
    rejectLabel: 'Não',
    acceptLabel: 'Sim',
    accept: async () => {
      isLoading.value = true

      await axios.delete('transport-packages/' + transportPackage.code).then(response => {
        isLoading.value = false
        toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Embalagem de Transporte apagada com sucesso', life: 3000 })
        emit('loadTransportPackages')
      }).catch(
        error => {
          isLoading.value = false
          console.error(error)
        },
      )
    },
  })
}

const loadSensors = async packageCode => {
  try {
    await axios.get('transport-packages/' + packageCode + '/sensors').then(response => {
      removeSensors.value = response.data
    })
  } catch (error) {
    console.error(error)
  }
}

const addOrRemoveSensorToPackage = async (packageCode, addOrRemove) => {
  try {
    if (addOrRemove == 'addSensor') {
      let payload = {
        sensors: selectedSensors.value,
      }
      await axios.post('transport-packages/' + packageCode + '/add-sensors', payload).then(response => {
        toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Sensor adicionado com sucesso', life: 3000 })
        emit('loadSensors')
      })
    } else {
      await axios.delete('sensors/' + selectedSensors.value + '/packages/' + packageCode).then(response => {
        toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Sensor removido com sucesso', life: 3000 })
        emit('loadSensors')
      })
    }
    selectedSensors.value = null
  } catch (error) {
    console.error(error)
  }
}

const resetSelectedSensor = () => {
  selectedSensors.value = null
}

watch(
  () => props,
  newProps => {
    transportPackages.value = Object.assign({}, newProps.transportPackages)
  },
  { immediate: true },
)
</script>

<template>
  <VTable fixed-header>
    <thead>
      <tr>
        <th class="text-uppercase">
          Código
        </th>
        <th>
          Material
        </th>
        <th>
          Volume
        </th>
        <th>
          Data de Criação
        </th>
        <th v-if="userRole == 'LogisticOperator' && canDelete && order && (order.status == 'Estado Inicial' || order.status == 'Em Processamento')">
          Ações
        </th>
      </tr>
    </thead>

    <tbody>
      <tr
        v-for="item in transportPackages"
        :key="item.code"
      >
        <td style="width: 10%;">
          {{ "TP" + item.code }}
        </td>
        <td style="width: 20%; text-align: center;">
          {{ item.material }}
        </td>
        <td style="width: 30%; text-align: center;">
          {{ item.volume + " cm³" }}
        </td>
        <td style="width: 40%; text-align: center;">
          {{ formatDate(item.createdAt) }}
        </td>
        <td
          v-if="userRole == 'LogisticOperator' && order && (order.status == 'Estado Inicial' || order.status == 'Em Processamento')"
          class="d-flex align-center justify-end gap-x-2"
          style="width: fit-content"
        >
          <VDialog width="500">
            <template #activator="{ props }">
              <VBtn
                v-bind="props"
                text="Adicionar Sensores"
                @click="resetSelectedSensor"
              >
                <VIcon
                  size="20"
                  icon="bx-plus"
                />
                <VTooltip
                  activator="parent"
                  location="top"
                >
                  <span>Adicionar sensores</span>
                </VTooltip>
              </VBtn>
            </template>

            <template #default="{ isActive }">
              <VCard title="Adicionar Sensores">
                <VCardText>
                  <VSelect
                    v-model="selectedSensors"
                    multiple
                    label="Tipo de sensor"
                    placeholder="Selecionar Sensor"
                    :items="addSensors"
                  />
                </VCardText>

                <VCardActions>
                  <VSpacer />

                  <VBtn
                    text="Adicionar"
                    @click="addOrRemoveSensorToPackage(item.code, 'addSensor'); isActive.value = false;"
                  />
                </VCardActions>
              </VCard>
            </template>
          </VDialog>

          <VDialog width="500">
            <template #activator="{ props }">
              <VBtn
                v-bind="props"
                text="Remover Sensores"
                @click="loadSensors(item.code); resetSelectedSensor()"
              >
                <VIcon
                  size="20"
                  icon="bx-minus"
                />
                <VTooltip
                  activator="parent"
                  location="top"
                >
                  <span>Remover sensores</span>
                </VTooltip>
              </VBtn>
            </template>

            <template #default="{ isActive }">
              <VCard title="Remover Sensores">
                <VCardText>
                  <VAutocomplete
                    v-model="selectedSensors"
                    label="Tipo de sensor"
                    placeholder="Selecionar Sensor"
                    :items="removeSensors"
                    item-title="type"
                    item-value="code"
                  />
                </VCardText>

                <VCardActions>
                  <VSpacer />

                  <VBtn
                    text="Remover"
                    @click="addOrRemoveSensorToPackage(item.code, 'removeSensor'); isActive.value = false;"
                  />
                </VCardActions>
              </VCard>
            </template>
          </VDialog>

          <VBtn
            rel="noopener noreferrer"
            color="primary"
            @click="removeTransportPackage(item)"
          >
            <VIcon
              size="20"
              icon="bx-trash"
            />
            <VTooltip
              activator="parent"
              location="top"
            >
              <span>Remover Embalagem de Transporte</span>
            </VTooltip>
          </VBtn>
        </td>
      </tr>
    </tbody>
  </VTable>
</template>

<style scoped></style>
