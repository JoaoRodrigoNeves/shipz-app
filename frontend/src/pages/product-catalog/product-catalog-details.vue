<script setup>
import { ref, onMounted, inject } from 'vue'
import ProductTable from '@/views/pages/tables/ProductTable.vue'
import { useRouter } from 'vue-router'
import ProductCatalogForm from '@/views/pages/form-layouts/ProductCatalogForm.vue'
import { useToast } from "primevue/usetoast";
import { useConfirm } from "primevue/useconfirm";

const axios = inject('axios')
const isLoading = ref(false)
const router = useRouter()
const confirm = useConfirm()
const toast = useToast()

const products = ref([])
const productCatalog = ref(null)
const isCreatingOrUpdatingCatalog = ref(false)
const catalogToUpdate = ref(null)
const createQuantity = ref(1)

const loadProductCatalogDetails = async () => {
  isLoading.value = true
  await axios.get('product-catalogs/' + router.currentRoute.value.params.code).then(response => {
    isLoading.value = false
    productCatalog.value = response.data
  }).catch(
    error => {
      isLoading.value = false
      console.error(error)
    },
  )
}

const loadProductCatalogProducts = async () => {
  isLoading.value = true

  await axios.get('product-catalogs/' + router.currentRoute.value.params.code + '/products').then(response => {
    isLoading.value = false
    products.value = response.data
  }).catch(
    error => {
      isLoading.value = false
      console.error(error)
    },
  )
}

const createProduct = (async () => {
  if (createQuantity.value <= 0 || createQuantity.value > 100) {
    toast.add({ severity: 'error', summary: 'Erro', detail: 'A quantidade de produtos a serem criados tem de ser entre 1 e 100 unidades', life: 3000 });
    return;
  }
  isLoading.value = true;
  var payload = {
    productCatalogCode: productCatalog.value.code,
    quantity: createQuantity.value

  }

  await axios.post('products', payload
  ).then(response => {
    if (response.status == 201) {
      toast.add({ severity: 'success', summary: 'Sucesso', detail: createQuantity.value == 1 ? 'Produto criado com sucesso' : 'Produtos criados com sucesso', life: 3000 });
      loadProductCatalogProducts();
      createQuantity.value = 1;
    }
  }).catch(
    error => {
      isLoading.value = false;
      console.error(error)
    }
  )
});

const deleteProductCatalogConfirm = (productCatalogItem) => {
  confirm.require({
    message: 'Tem a certeza que pretende apagar o catálogo ' + productCatalogItem.name + ' ?',
    header: 'Apagar Catálogo',
    rejectLabel: 'Não',
    acceptLabel: 'Sim',
    accept: async () => {
      isLoading.value = true;

      await axios.delete('product-catalogs/' + productCatalogItem.code).then(response => {
        isLoading.value = false
        router.push({ path: '/product-catalogs' })
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

const closeFormAndUpdateCatalog = async () => {
  isCreatingOrUpdatingCatalog.value = false
  await loadProductCatalogDetails()
}

const updateProductCatalog = async productCatalog => {
  catalogToUpdate.value = productCatalog
  isCreatingOrUpdatingCatalog.value = true
}

const navigateTo = (path) => {
  router.push({ path: path })
}

onMounted(async () => {
  await loadProductCatalogDetails()
  await loadProductCatalogProducts()
})
</script>

<template>
  <VRow v-if="!isCreatingOrUpdatingCatalog">
    <VCol cols="12">
      <VCard v-if="productCatalog">
        <div class="product-catalog-details-header">
          <div style="display: flex; align-items: center; gap: 12px;">
            <VIcon size="35" icon="mdi-arrow-left-bold-circle" @click="navigateTo('/product-catalogs')" />
            <h2>{{ "Catálogo - PC" + productCatalog.code }}</h2>
          </div>

          <div class="product-catalog-details-actions">

            <VBtn rel="noopener noreferrer" color="primary" @click="updateProductCatalog(productCatalog)">
              <VIcon size="20" icon="bx-pencil" />
              <VTooltip activator="parent" location="top">
                <span>Editar Catálogo</span>
              </VTooltip>
            </VBtn>
            <VBtn rel="noopener noreferrer" color="primary" v-if="products && products.length == 0"
              @click="deleteProductCatalogConfirm(productCatalog)">
              <VIcon size="20" icon="bx-trash" />
              <VTooltip activator="parent" location="top">
                <span>Apagar Catálogo</span>
              </VTooltip>
            </VBtn>
          </div>
        </div>
        <div class="product-catalog-details">
          <div class="catalog-item">
            <label>
              Código
            </label>
            <span>
              {{ "PC" + productCatalog.code }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Nome
            </label>
            <span>
              {{ productCatalog.name }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Criado por
            </label>
            <span>
              {{ productCatalog.productManufacterName }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Área
            </label>
            <span>
              {{ productCatalog.catalogArea }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Categoria
            </label>
            <span>
              {{ productCatalog.category }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Descrição
            </label>
            <span>
              {{ productCatalog.description }}
            </span>
          </div>
          <div class="catalog-item">
            <label>
              Embalagem Primária
            </label>
            <div style="display: flex; align-items: center; gap: 12px;">
              <span>
                1
                <VTooltip activator="parent" location="top">
                  <span>Máximo de Produtos</span>
                </VTooltip>
              </span>
              -
              <span>
                {{ productCatalog.primaryPackageVolume }} cm³
                <VTooltip activator="parent" location="top">
                  <span>Volume</span>
                </VTooltip>
              </span>
              -
              <span>
                {{ productCatalog.primaryPackageMaterial }}
                <VTooltip activator="parent" location="top">
                  <span>Material</span>
                </VTooltip>
              </span>
            </div>
          </div>
          <div class="catalog-item" v-if="productCatalog.maxSecondaryPackage">
            <label>
              Embalagem Secundária
            </label>
            <div style="display: flex; align-items: center; gap: 12px;">
              <span>
                {{ productCatalog.maxSecondaryPackage }}
                <VTooltip activator="parent" location="top">
                  <span>Máximo de Produtos</span>
                </VTooltip>
              </span>
              -
              <span>
                {{ productCatalog.primaryPackageVolume * productCatalog.maxSecondaryPackage }} cm³
                <VTooltip activator="parent" location="top">
                  <span>Volume</span>
                </VTooltip>
              </span>
              -
              <span>
                {{ productCatalog.secondaryPackageMaterial }}
                <VTooltip activator="parent" location="top">
                  <span>Material</span>
                </VTooltip>
              </span>
            </div>
          </div>
          <div class="catalog-item" v-if="productCatalog.maxTertiaryPackage">
            <label>
              Embalagem Terciária
            </label>
            <div style="display: flex; align-items: center; gap: 12px;">
              <span>
                {{ productCatalog.maxTertiaryPackage }}
                <VTooltip activator="parent" location="top">
                  <span>Máximo de Produtos</span>
                </VTooltip>
              </span>
              -
              <span>
                {{ productCatalog.primaryPackageVolume * productCatalog.maxTertiaryPackage }} cm³
                <VTooltip activator="parent" location="top">
                  <span>Volume</span>
                </VTooltip>
              </span>
              -
              <span>
                {{ productCatalog.tertiaryPackageMaterial }}
                <VTooltip activator="parent" location="top">
                  <span>Material</span>
                </VTooltip>
              </span>
            </div>
          </div>
          <div class="catalog-item" v-if="productCatalog.sensors && productCatalog.sensors.length > 0">
            <label>
              Sensores
            </label>
            <div style="display: flex; align-items: center; gap: 8px;">
              <span v-for="(sensor, i) in productCatalog.sensors" :key="i">
                {{ sensor }} <span v-if="i < productCatalog.sensors.length - 1"> / </span>
              </span>
            </div>

          </div>
        </div>
        <div class="products-actions">
          <h2>Produtos</h2>
          <div class="product-quantity">
            <VTextField type="number" v-model="createQuantity" class="product-quantity" style="width:75px" />
            <VBtn rel="noopener noreferrer" color="primary" @click="createProduct">
              <VIcon size="20" icon="bx-plus" />
              <VTooltip activator="parent" location="top">
                <span>Adicionar Produto</span>
              </VTooltip>
            </VBtn>
          </div>
        </div>
        <div v-if="products && products.length > 0 && !isLoading">
          <ProductTable @loadProducts="loadProductCatalogProducts" :product-package-view="false" :products="products" />
        </div>
        <div v-else class="no-products">
          Não tem produtos associados a este catálogo
        </div>
      </VCard>
    </VCol>
  </VRow>
  <VCard v-if="isCreatingOrUpdatingCatalog">
    <VCard>
      <div class="product-catalogs-header">
        <VIcon size="35" icon="mdi-arrow-left-bold-circle" @click="isCreatingOrUpdatingCatalog = false" />
        <h2>Editar Catálogo</h2>
      </div>
      <VCardText>
        <ProductCatalogForm @closeFormAndUpdate="closeFormAndUpdateCatalog" :productCatalogToUpdate="catalogToUpdate"
          :isCreating="false"></ProductCatalogForm>
      </VCardText>
    </VCard>
  </VCard>
</template>

<style scoped>
.product-catalog-details-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
}

.product-catalog-details-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.product-catalog-details {
  display: flex;
  padding: 0 24px;
  gap: 16px 0px;
  flex-wrap: wrap;
}

.product-catalog-details .catalog-item {
  display: flex;
  flex-direction: column;
  width: 50%;
}

.product-catalog-details .catalog-item label {
  opacity: 0.7;
  font-size: 14px;
}

.product-catalog-details .catalog-item span {
  text-transform: capitalize;
}

.products-actions {
  display: flex;
  justify-content: space-between;
  padding: 24px;
}

.products-actions .product-quantity {
  display: flex;
  align-items: center;
  gap: 12px;
}

.no-products {
  padding: 0 24px 24px 24px;
}

.product-catalogs-header {
  display: flex;
  justify-content: start;
  align-self: center;
  gap: 12px;
  padding: 24px;
}
</style>
