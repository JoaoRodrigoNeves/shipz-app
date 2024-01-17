<script setup>
import { ref, onMounted, inject } from 'vue'
import ProductTable from '@/views/pages/tables/ProductTable.vue'
import { useRouter } from 'vue-router'
import ProductCatalogForm from '@/views/pages/form-layouts/ProductCatalogForm.vue'
import ProductForm from '@/views/pages/form-layouts/ProductForm.vue'
import { useToast } from "primevue/usetoast"
import { useConfirm } from "primevue/useconfirm"

const axios = inject('axios')
const isLoading = ref(false)
const router = useRouter()
const confirm = useConfirm()
const toast = useToast()

const products = ref([])
const productCatalog = ref(null)
const isCreatingOrUpdatingCatalog = ref(false)
const isCreatingOrUpdatingProduct = ref(false)
const catalogToUpdate = ref(null)
const productToUpdate = ref(null)
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
    if(createQuantity.value <= 0 || createQuantity.value > 100){
        toast.add({ severity: 'error', summary: 'Erro', detail: 'A quantidade de produtos a serem criados tem de ser entre 1 e 100 unidades', life: 3000 });
        return;
    }
    isLoading.value = true;
    var payload = {
        productCatalogCode: productCatalog.value.code,
        quantity: createQuantity.value

  }
  await axios.post('products', payload,
  ).then(response => {
    if (response.status == 201) {
      toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto criado com sucesso', life: 3000 })
      loadProductCatalogProducts()
    }
  }).catch(
    error => {
      isLoading.value = false
      console.error(error)
    },
  )
})

const deleteProductCatalogConfirm = productCatalogItem => {
  console.log(productCatalogItem)
  confirm.require({
    message: 'Tem a certeza que pretende apagar o catálogo ' + productCatalogItem.name + ' ?',
    header: 'Apagar Catálogo',
    rejectLabel: 'Não',
    acceptLabel: 'Sim',
    accept: async () => {
      isLoading.value = true

      await axios.delete('product-catalogs/' + productCatalogItem.code).then(response => {
        isLoading.value = false
        router.push({ path: '/product-catalogs' })
      }).catch(
        error => {
          isLoading.value = false
          console.error(error)
        },
      )
    },
  })
}

const closeFormAndUpdateCatalog = async () => {
  isCreatingOrUpdatingCatalog.value = false
  await loadProductCatalogDetails()
}

const closeFormAndUpdateProduct = async () => {
  isCreatingOrUpdatingProduct.value = false
  await loadProductCatalogProducts()
}


const updateProductCatalog = async productCatalog => {
  catalogToUpdate.value = productCatalog
  isCreatingOrUpdatingCatalog.value = true
}

const updateProduct = async product => {
  productToUpdate.value = product
  isCreatingOrUpdatingProduct.value = true
}

onMounted(async () => {
  await loadProductCatalogDetails()
  await loadProductCatalogProducts()
})
</script>

<template>
  <VRow v-if="!isCreatingOrUpdatingCatalog && !isCreatingOrUpdatingProduct">
    <VCol cols="12">
      <VCard v-if="productCatalog">
        <div class="product-catalog-details-header">
          <h2>{{ productCatalog.name }}</h2>
          <div class="product-catalog-details-actions">
            <VBtn
              rel="noopener noreferrer"
              color="primary"
              @click="updateProductCatalog(productCatalog)"
            >
              <VIcon
                size="20"
                icon="bx-pencil"
              />
              <VTooltip
                activator="parent"
                location="top"
              >
                <span>Editar Catálogo</span>
              </VTooltip>
            </VBtn>
            <VBtn
              v-if="products && products.length == 0"
              rel="noopener noreferrer"
              color="primary"
              @click="deleteProductCatalogConfirm(productCatalog)"
            >
              <VIcon
                size="20"
                icon="bx-trash"
              />
              <VTooltip
                activator="parent"
                location="top"
              >
                <span>Apagar Catálogo</span>
              </VTooltip>
            </VBtn>
          </div>
        </div>
        <div class="product-catalog-details">
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
              {{ productCatalog.productManufacterUsername }}
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
        </div>
        <div class="products-actions">
          <h2>Produtos</h2>
          <div class="product-quantity">
            <VTextField
              v-model="createQuantity"
              type="number"
              class="product-quantity"
              style="width:75px"
            />
            <VBtn
              rel="noopener noreferrer"
              color="primary"
              @click="createProduct"
            >
              <VIcon
                size="20"
                icon="bx-plus"
              />
              <VTooltip
                activator="parent"
                location="top"
              >
                <span>Adicionar Produto</span>
              </VTooltip>
            </VBtn>
          </div>
        </div>
        <div v-if="products && products.length > 0 && !isLoading">
          <ProductTable
            :product-package-view="false"
            :products="products"
            @updateProduct="updateProduct"
            @loadProducts="loadProductCatalogProducts"
          />
        </div>
        <div
          v-else
          class="no-products"
        >
          Não tem produtos associados a este catálogo
        </div>
      </VCard>
    </VCol>
  </VRow>
  <VCard v-if="isCreatingOrUpdatingCatalog">
    <VCard>
      <div class="product-catalogs-header">
        <h2>Editar Catálogo</h2>
      </div>
      <VCardText>
        <ProductCatalogForm
          :product-catalog-to-update="catalogToUpdate"
          :is-creating="false"
          @closeFormAndUpdate="closeFormAndUpdateCatalog"
        />
      </VCardText>
    </VCard>
  </VCard>
  <VCard v-if="isCreatingOrUpdatingProduct">
    <VCard>
      <div class="product-catalogs-header">
        <h2>Editar Produto</h2>
      </div>
      <VCardText>
        <ProductForm
          :product-to-update="productToUpdate"
          :is-creating="false"
          @closeFormAndUpdate="closeFormAndUpdateProduct"
        />
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
    justify-content: space-between;
    align-self: center;
    padding: 24px;
}
</style>
