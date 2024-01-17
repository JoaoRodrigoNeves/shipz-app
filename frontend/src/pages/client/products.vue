<script setup>
import { ref, onMounted } from 'vue'
import { useToast } from "primevue/usetoast"

const axios = inject('axios')
const toast = useToast();
const isLoading = ref(false);
const productCatalogs = ref([]);
const productCatalogsSelected = ref([]);
const logisticOperatorSelected = ref(null);
const logisticOperators = ref([]);

const loadProductCatalogs = async () => {
  isLoading.value = true

  await axios.get('product-catalogs/available').then(response => {
    isLoading.value = false
    productCatalogs.value = response.data
  }).catch(
    error => {
      isLoading.value = false
      console.error(error)
    },
  )
}

const loadLogisticOperators = async () => {
  isLoading.value = true

  await axios.get('logistic-operators').then(response => {
    logisticOperators.value = response.data
    isLoading.value = false
  }).catch(
    error => {
      isLoading.value = false;
      console.error(error)
    }
  )

}

const createOrder = async () => {
  isLoading.value = true

  var payload = {
    products: productCatalogsSelected.value,
    logisticOperator: logisticOperatorSelected.value,
    finalCostumer: JSON.parse(sessionStorage.getItem('user_info')).username
  }
  await axios.post('orders', payload).then(response => {
    if (response.status == 201) {
      toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Encomenda criado com sucesso', life: 3000 })
      resetProducts()
      loadProductCatalogs()
    }
    isLoading.value = false
  }).catch(
    error => {
      console.log(error)
      toast.add({ severity: 'error', summary: 'Erro', detail: error.response.data, life: 3000 })
      isLoading.value = false
    },
  )
}

const checkIfProductAreSelected = catalogCode => {
  return productCatalogsSelected.value.find(e => e.code == catalogCode) != null
}


const addProduct = catalog => {
  if (productCatalogsSelected.value.find(e => e.code == catalog.code) == null) {
    catalog.quantity = 1
    productCatalogsSelected.value.push(catalog)
  } else {
    productCatalogsSelected.value.splice(productCatalogsSelected.value.findIndex(e => e.code == catalog.code), 1)
  }
}

const resetProducts = () => {
  productCatalogsSelected.value = []
  logisticOperatorSelected.value = null
}

onMounted(async () => {
  await loadProductCatalogs();
  await loadLogisticOperators();
})
</script>

<template>
  <div class="product-page-container">
    <div class="products-container">
      <div class="product-container-header">
        <div class="title">
          Produtos
        </div>
      </div>
      <div
        v-if="productCatalogs.length > 0"
        class="products-list"
      >
        <div
          v-for="productCatalog in productCatalogs"
          class="product-item"
          :class="{ 'checked': checkIfProductAreSelected(productCatalog.code) }"
          @click="addProduct(productCatalog)"
        >
          <VIcon
            v-if="checkIfProductAreSelected(productCatalog.code)"
            icon="mdi-check-circle"
            color="rgba(0, 128, 11, 1)"
            class="icon-check"
          />
          <div class="product-header">
            <span>{{ productCatalog.category }}</span>
            <span>{{ productCatalog.code }}</span>
          </div>
          <div class="product-content">
            <span>
              {{ productCatalog.name }}
            </span>
          </div>
          <div class="product-footer">
            <span>
              {{ productCatalog.description }}
            </span>
          </div>
        </div>
        <div class="product-item-hidden" />
        <div class="product-item-hidden" />
        <div class="product-item-hidden" />
        <div class="product-item-hidden" />
        <div class="product-item-hidden" />
        <div class="product-item-hidden" />
      </div>
      <div v-else>
        <div class="no-products-container">
          Não há produtos disponiveis
        </div>
      </div>
    </div>
    <div
      v-if="productCatalogsSelected.length > 0"
      class="selected-prodcuts-container"
    >
      <div class="selected-product-list">
        <div class="selected-product-item selected-product-item-header">
          <span>Produto</span>
          <span>Quantidade</span>
        </div>
        <div
          v-for="productCatalog in productCatalogsSelected"
          class="selected-product-item"
        >
          <span>{{ productCatalog.name }}</span>

          <div class="selected-product-quantity-action">
            <div style="width: 100px;">
              <VTextField
                v-model="productCatalog.quantity"
                type="number"
                class="product-quantity"
              />
            </div>
            <span class="product-action">
              <VIcon @click="addProduct(productCatalog)">
                mdi-close
              </VIcon>
            </span>
          </div>
        </div>
        <div style="margin-top: 12px;">
          <VAutocomplete v-model="logisticOperatorSelected" label="Operadores Logisticos"
              placeholder="Selecionar Operador Logistico" :items="logisticOperators" item-title="name"
              item-value="username" />
        </div>
        <div class="product-submit">
          <VBtn
            color="secondary"
            variant="tonal"
            type="reset"
            @click.prevent="resetProducts"
          >
            Repor
          </VBtn>
          <VBtn rel="noopener noreferrer" color="primary" :disabled="!logisticOperatorSelected || productCatalogsSelected && productCatalogsSelected.length == 0" @click="createOrder">
            Submeter
          </VBtn>
        </div>
      </div>
    </div>
  </div>
</template>
  
<style>
.product-page-container .selected-prodcuts-container {
  width: fit-content;
  min-width: 350px;
  padding-left: 16px;
  border-left: 1px solid #0000002a;
}

.product-page-container .selected-prodcuts-container .selected-product-list {
  display: flex;
  flex-direction: column;
}

.product-page-container .selected-prodcuts-container .selected-product-list .product-submit {
  display: flex;
  justify-content: end;
  width: 100%;
  gap: 16px;
  margin-top: 24px;
}

.product-page-container .selected-prodcuts-container .selected-product-list .selected-product-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  white-space: nowrap;
  border-bottom: 1px solid #0000002a;
  padding: 12px 0px 12px 0px;
}

.product-page-container .selected-prodcuts-container .selected-product-list .selected-product-item-header {
  justify-content: space-between;
  padding-right: 40px;
}

.product-page-container .selected-prodcuts-container .selected-product-list .selected-product-item .product-action {
  cursor: pointer;
}

.product-page-container .selected-prodcuts-container .selected-product-list .selected-product-item .selected-product-quantity-action {
  display: flex;
  align-items: center;
  gap: 12px;
}

.product-page-container {
  display: flex;
  gap: 32px;
}

.products-container {
  padding: 12px;
  width: 100%;
}

.products-container .product-container-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.products-container .product-container-header .title {
  font-size: 18px;
}

.products-container .products-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
}


.products-container .products-list .product-item {
  background-color: #fff;
  box-shadow: 0 0 0 2px #ffffff, 0 0 0 4px rgb(172, 169, 169), 0 1px 2px 0 black;
  padding: 10px;
  border-radius: 8px;
  width: 100%;
  position: relative;
  cursor: pointer;
}

.products-container .products-list .product-item-hidden {
  visibility: hidden;
}

.products-container .products-list .product-item .icon-check {
  position: absolute;
  top: -10px;
  right: -10px;
  background-color: #fff;
  border-radius: 100%;
}

.products-container .products-list .product-item .product-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.products-container .products-list .product-item .product-content {
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 700;
  color: #00000080;
  height: 100px;
  font-size: 18px;
}

.products-container .products-list .product-item.checked {
  background-color: rgba(0, 128, 11, 0.15);
}

.products-container .products-list .product-item .product-footer {
  text-align: center;
}


.products-container .no-products-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 150px;
}

@media (max-width: 767px) {
  .product-page-container {
    flex-direction: column-reverse;
  }

  .product-page-container .selected-prodcuts-container {
    width: 100%;
    border-left: 0;
  }
}
</style>
