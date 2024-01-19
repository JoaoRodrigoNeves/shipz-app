<script setup>
import { ref, inject } from 'vue'
import { useRouter } from 'vue-router';
import { useConfirm } from "primevue/useconfirm";

const emit = defineEmits(['updateProductCatalog', 'loadProductCatalogs'])
const axios = inject('axios')
const isLoading = ref(false)
const router = useRouter()
const confirm = useConfirm();
const props = defineProps({

  productCatalogs: {
    type: Object,
    required: true
  }
})

const productCatalogs = ref(Object.assign({}, props.productCatalogs))


const navigateTo = (path) => {
  router.push({ path: path })
}

const updateProductCatalog = (productCatalog) => {
  emit('updateProductCatalog', productCatalog)
}

const deleteProductCatalogConfirm = (productCatalog) => {
  confirm.require({
    message: 'Tem a certeza que pretende apagar o catálogo ' + productCatalog.name + ' ?',
    header: 'Apagar Catálogo',
    rejectLabel: 'Não',
    acceptLabel: 'Sim',
    accept: async () => {
      isLoading.value = true
      await axios.delete('product-catalogs/' + productCatalog.code).then(response => {
        isLoading.value = false
        emit('loadProductCatalogs')
      }).catch(
        error => {
          toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possivel apagar o catalogo com o código PC' + productCatalogItem.value.code, life: 3000 });
          isLoading.value = false;
          console.error(error)
        }
      )
    }
  });
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
          {{ "PC" + item.code }}
        </td>
        <td class="text-center" style="width: 100%;">
          {{ item.name }}
        </td>
        <td class="d-flex align-center justify-center gap-x-2" style="width: fit-content;">
          <VBtn rel="noopener noreferrer" color="primary" @click="navigateTo('product-catalog/' + item.code)">
            <VIcon size="20" icon="bx-show" />
            <VTooltip activator="parent" location="top">
              <span>Ver Detalhes</span>
            </VTooltip>
          </VBtn>
          <VBtn rel="noopener noreferrer" color="primary" @click="updateProductCatalog(item)">
            <VIcon size="20" icon="bx-pencil" />
            <VTooltip activator="parent" location="top">
              <span>Editar Catálogo</span>
            </VTooltip>
          </VBtn>
          <VBtn rel="noopener noreferrer" color="primary" @click="deleteProductCatalogConfirm(item)">
            <VIcon size="20" icon="bx-trash" />
            <VTooltip activator="parent" location="top">
              <span>Apagar Catálogo</span>
            </VTooltip>
          </VBtn>

        </td>

      </tr>
    </tbody>
  </VTable>
</template>
