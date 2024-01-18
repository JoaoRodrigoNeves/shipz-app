<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import moment from 'moment'

const props = defineProps({
  transportPackages: {
    type: Object,
    required: true,
  },
})

const router = useRouter()

const transportPackages = ref(Object.assign({}, props.transportPackages))

const formatDate = value => {
  return moment(String(value)).format('DD/MM/YYYY hh:mm:ss')
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
      </tr>
    </thead>

    <tbody>
      <tr
        v-for="item in transportPackages"
        :key="item.code"
      >
        <td style="width: 20%;">
          {{ item.code }}
        </td>
        <td style="width: 20%; text-align: center;">
          {{ item.material }}
        </td>
        <td style="width: 30%; text-align: center;">
          {{ item.volume + " cm³" }}
        </td>
        <td style="width: 30%; text-align: center;">
          {{ formatDate(item.createdAt) }}
        </td>
      </tr>
    </tbody>
  </VTable>
</template>

<style scoped></style>
