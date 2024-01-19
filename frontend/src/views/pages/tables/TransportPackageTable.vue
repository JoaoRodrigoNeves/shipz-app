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
    required: true,
  },
  canDelete: {
    type: Boolean,
    required: false,
  },
})

const emit = defineEmits(['loadTransportPackages'])
const router = useRouter()
const confirm = useConfirm()
const isLoading = ref(false)
const axios = inject('axios')
const toast = useToast()
const selectedSensor = ref(null)
const addSensors = ref([])
const removeSensors = ref([])
const userRole = JSON.parse(sessionStorage.getItem('user_info')).role

const transportPackages = ref(Object.assign({}, props.transportPackages))

const formatDate = value => {
  return moment(String(value)).format('DD/MM/YYYY HH:mm:ss')
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
        <th v-if="userRole == 'LogisticOperator' && (order.status == 'Estado Inicial' || order.status == 'Em Processamento') || canDelete">
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
          {{ item.code }}
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
          v-if="userRole == 'LogisticOperator' && (order.status == 'Estado Inicial' || order.status == 'Em Processamento')"
          class="d-flex align-center justify-end gap-x-2"
          style="width: fit-content"
        >
          <VDialog width="500">
            <template #activator="{ props }">
              <VBtn
                v-bind="props"
                text="Adicionar Sensores"
                @click="loadSensors(null)"
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
                  <VAutocomplete
                    v-model="selectedSensor"
                    label="Tipo de sensor"
                    placeholder="Selecionar Sensor"
                    :items="addSensors"
                    item-title="type"
                    item-value="code"
                  />
                </VCardText>

                <VCardActions>
                  <VSpacer />

                  <VBtn
                    text="Adicionar"
                    @click="addOrRemoveSensorToPackage(selectedSensor, item.code, 'addSensor'); isActive.value = false;"
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
                @click="loadSensors(item.code)"
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
                    v-model="selectedSensor"
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
                    @click="addOrRemoveSensorToPackage(selectedSensor, item.code, 'removeSensor'); isActive.value = false;"
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
