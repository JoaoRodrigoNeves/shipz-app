<script setup>
import { ref } from 'vue'

const emit = defineEmits(['loadProducts'])
const props = defineProps({

  products: {
    type: Object,
    required: true
  }
})

const products = ref(Object.assign({}, props.products))

watch(
  () => props,
  (newProps) => {
    products.value = Object.assign({}, newProps.products)
  },
  { immediate: true }
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
          Código do Catálogo
        </th>
        <th>
          Nome do Catálogo
        </th>
        <th>
          Código da Encomenda
        </th>
      </tr>
    </thead>

    <tbody>
      <tr v-for="item in products" :key="item.code">
        <td style="width: 30%;">
          {{ item.code }}
        </td>
        <td style="width: 20%; text-align: center;">
          {{ item.productCatalogCode }}
        </td>
        <td style="width: 30%; text-align: center;">
          {{ item.productCatalogName }}
        </td>
        <td style="width: 100%; text-align: center;">
          {{ item.clientOrderCode ? item.clientOrderCode : "Sem encomenda" }}
        </td>

      </tr>
    </tbody>
  </VTable>
</template>
<style scoped></style>