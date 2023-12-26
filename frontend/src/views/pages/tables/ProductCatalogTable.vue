<script setup>
import { useRouter } from 'vue-router';

const router = useRouter()
const props = defineProps({

  productCatalogs: {
    type: Object,
    required: true
  }
})

const productCatalogs = ref(Object.assign({}, props.productCatalogs))


const navigateTo = (path) => {
  router.push({ path: path})
}

watch(
  () => props,
  (newProps) => {
    productCatalogs.value = Object.assign({}, newProps.productCatalogs)
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
          Nome
        </th>
        <th>
          Ações
        </th>
      </tr>
    </thead>

    <tbody>
      <tr v-for="item in productCatalogs" :key="item.code">
        <td>
          {{ item.code }}
        </td>
        <td class="text-center">
          {{ item.name }}
        </td>
        <td class="d-flex align-center justify-center gap-x-2">
          <VBtn rel="noopener noreferrer" color="primary" @click="navigateTo('product-catalog/' + item.code)">
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
