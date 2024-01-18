<script setup>
import { ref, inject } from 'vue'
import { useConfirm } from "primevue/useconfirm";
import { useRouter } from 'vue-router';

const emit = defineEmits(['loadProducts'])
const axios = inject('axios')
const router = useRouter()

const confirm = useConfirm();
const props = defineProps({
  productPackageView: {
    type: Boolean,
    required: true
  },
  products: {
    type: Object,
    required: true
  }
})
const isLoading = ref(false)

const deleteProductConfirm = (product) => {
  confirm.require({
    message: 'Tem a certeza que pretende apagar o produto ' + product.code + ' ?',
    header: 'Apagar Produto',
    rejectLabel: 'Não',
    acceptLabel: 'Sim',
    accept: async () => {
      isLoading.value = true;

      await axios.delete('products/' + product.code).then(response => {
        isLoading.value = false
        emit('loadProducts')
      }).catch(
        error => {
          toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possivel apagar o produto com o código P' + product.code, life: 3000 });
          isLoading.value = false;
          console.error(error)
        }
      )
    }
  });
}
const products = ref(Object.assign({}, props.products))

const removeProduct = (product) => {
  emit('removeProduct', product)
}

const navigateTo = (path) => {
    router.push({ path: path })
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
          Código do Produto
        </th>
        <th>
          Código do Catálogo
        </th>
        <th>
          Nome do Catálogo
        </th>
        <th>
          Fabricante
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
        <td style="width: 15%;">
          {{ "P" + item.code }}
        </td>
        <td style="width: 20%; text-align: center;">
          {{ "PC" + item.productCatalogCode }}
        </td>
        <td style="width: 30%; text-align: center;">
          {{ item.productCatalogName }}
        </td>
        <td style="width: 20%; text-align: center;">
          {{ item.productManufacterName }}
        </td>
        <td style="width: 15%; text-align: center;">
          {{ item.clientOrderCode ? item.clientOrderCode : "Sem encomenda" }}
        </td>
        <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
          <VBtn rel="noopener noreferrer" color="primary" @click="navigateTo('/product/' + item.code)">
            <VIcon size="20" icon="bx-show" />
            <VTooltip activator="parent" location="top">
              <span>Ver Detalhes</span>
            </VTooltip>
          </VBtn>
          <VBtn rel="noopener noreferrer" color="primary" v-if="!props.productPackageView"
            @click="deleteProductConfirm(item)">
            <VIcon size="20" icon="bx-trash" />
            <VTooltip activator="parent" location="top">
              <span>Apagar Produto</span>
            </VTooltip>
          </VBtn>
          <VBtn rel="noopener noreferrer" color="primary" v-if="props.productPackageView" @click="removeProduct(item)">
            <VIcon size="20" icon="bx-trash" />
            <VTooltip activator="parent" location="top">
              <span>Remover Produto</span>
            </VTooltip>
          </VBtn>
        </td>
      </tr>
    </tbody>
  </VTable>
</template>
<style scoped></style>