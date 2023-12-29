<script setup>
import { ref, inject } from 'vue'
import { useConfirm } from "primevue/useconfirm";

const emit = defineEmits(['loadProducts', 'updateProduct', ])
const axios = inject('axios')

const confirm = useConfirm();
const props = defineProps({

  products: {
    type: Object,
    required: true
  }
})

const deleteProductConfirm = (product) => {
  confirm.require({
    message: 'Tem a certeza que pretende apagar o produto ' + product.code + ' ?',
    header: 'Apagar Produto',
    rejectLabel: 'Não',
    acceptLabel: 'Sim',
    accept: async () => {
      try {
        await axios.delete('products/' + product.code).then(response => {
          emit('loadProducts')
        })
      } catch (error) {
        console.log(error)
      }
    }
  });
}
const products = ref(Object.assign({}, props.products))

const updateProduct = (product) => {
  emit('updateProduct', product)
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
        <td style="width: 100%; text-align: center;">
          {{ item.productCatalogName }}
        </td>
        <td style="width: 100%; text-align: center;">
          {{ item.clientOrderCode ? item.clientOrderCode : "Sem encomenda" }}
        </td>
        <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
          <VBtn rel="noopener noreferrer" color="primary" @click="updateProduct(item)">
            <VIcon size="20" icon="bx-pencil" />
            <VTooltip activator="parent" location="top">
              <span>Editar Produto</span>
            </VTooltip>
          </VBtn>
          <VBtn rel="noopener noreferrer" color="primary" @click="deleteProductConfirm(item)">
            <VIcon size="20" icon="bx-trash" />
            <VTooltip activator="parent" location="top">
              <span>Apagar Produto</span>
            </VTooltip>
          </VBtn>

        </td>

      </tr>
    </tbody>
  </VTable>
</template>
<style scoped></style>