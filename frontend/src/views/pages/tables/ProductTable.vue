<script setup>
import { useRouter } from 'vue-router';

const router = useRouter()
const props = defineProps({

  products: {
    type: Object,
    required: true
  }
})

const products = ref(Object.assign({}, props.products))

const navigateTo = (path) => {
  router.push({ path: path})
}

watch(
  () => props,
  (newProps) => {
    products.value = Object.assign({}, newProps.products)
    console.log(products.value)
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
          Código de Catálogo
        </th>
        <th>
          Ações
        </th>
      </tr>
    </thead>

    <tbody>
      <tr v-for="item in products" :key="item.code">
        <td style="width: 30%;">
          {{ item.code }}
        </td>
        <td style="width: 100%; text-align: center;">
          {{ item.productCatalogCode }}
        </td>
        <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
          <VBtn rel="noopener noreferrer" color="primary" @click="navigateTo('product-/' + item.code)">
            <VIcon
            size="20"
            icon="bx-show"
          />
          </VBtn>
          <VBtn rel="noopener noreferrer" color="primary">
            <VIcon
            size="20"
            icon="bx-plus"
          />
          </VBtn>
          <VBtn rel="noopener noreferrer" color="primary">
            <VIcon
            size="20"
            icon="bx-pencil"
          />
          </VBtn>
          <VBtn rel="noopener noreferrer" color="primary">
            <VIcon
            size="20"
            icon="bx-trash"
          />
          </VBtn>

        </td>

      </tr>
    </tbody>
  </VTable>

</template>
<style scoped>

</style>