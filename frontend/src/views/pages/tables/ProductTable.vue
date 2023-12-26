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

console.log(products.value)
const navigateTo = (path) => {
  router.push({ path: path})
}

watch(
  () => props,
  (newProps) => {
    products.value = Object.assign({}, newProps.products)

  },
  { immediate: true }
)
</script>

<template>
  <VTable fixed-header v-if="products">
    <thead>
      <tr>
        <th class="text-uppercase">
          Código
        </th>
        <th>
          Ações
        </th>
      </tr>
    </thead>

    <tbody>
      <tr v-for="item in products" :key="item.code">
        <td style="width: 100%;">
          {{ item.code }}
        </td>
        <td class="d-flex align-center justify-center gap-x-2" style="width: fit-content;">
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
  <div v-else class="no-products">
    Não tem produtos associados a este catálogo
  </div>
</template>
<style scoped>

.no-products{
  padding: 0 24px 24px 24px;
}
</style>
