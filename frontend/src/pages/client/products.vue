
import productCatalogDetailsVue from '../product-catalog/product-catalog-details.vue';

<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from "primevue/usetoast";

const axios = inject('axios')
const toast = useToast();
const isLoading = ref(false);
const productCatalogs = ref([]);
const productCatalogsSelected = ref([]);

const loadProductCatalogs = async () => {
    isLoading.value = true;

    await axios.get('product-catalogs').then(response => {
        isLoading.value = false;
        productCatalogs.value = response.data
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
        finalCostumer: JSON.parse(sessionStorage.getItem('user_info')).username
    }
    await axios.post('clientOrders', payload).then(response => {
        if (response.status == 201) {
            toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Encomenda criado com sucesso', life: 3000, });
            resetProducts()
        }
        isLoading.value = false
    }).catch(
        error => {
            toast.add({ severity: 'error', summary: 'Error', detail: 'Ocorreu um problema ao entrar na aplicação!', life: 3000 });
            isLoading.value = false
        }
    )
}

const checkIfProductAreSelected = (catalogCode) => {
    console.log(productCatalogsSelected.value.find(e => e == catalogCode) != null)
    return productCatalogsSelected.value.find(e => e == catalogCode) != null
}


const addProduct = (catalogCode) => {
    if (productCatalogsSelected.value.find(e => e == catalogCode) == null) {
        productCatalogsSelected.value.push(catalogCode)
    } else {
        productCatalogsSelected.value.splice(productCatalogsSelected.value.findIndex(e => e == catalogCode), 1)
    }
}

const resetProducts = () => {
    productCatalogsSelected.value = []
}

onMounted(async () => {
    await loadProductCatalogs();
})
</script>
<template>
    <div class="products-container">
        <div class="product-container-header">
            <div class="title">
                Produtos
            </div>
            <div class="product-submit">
                <VBtn color="secondary" variant="tonal" type="reset" @click.prevent="resetProducts">
                    Repor
                </VBtn>
                <VBtn rel="noopener noreferrer" color="primary" @click="createOrder">
                    Submeter
                </VBtn>
            </div>
        </div>
        <div class="products-list">

            <div class="product-item" v-for="productCatalog in productCatalogs" @click="addProduct(productCatalog.code)"
                :class="{ 'checked': checkIfProductAreSelected(productCatalog.code) }">
                <VIcon icon="mdi-check-circle" color="rgba(0, 128, 11, 1)" class="icon-check"
                    v-if="checkIfProductAreSelected(productCatalog.code)"></VIcon>
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
        </div>
    </div>
</template>
  
<style>
.products-container {
    padding: 12px;
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

.products-container .product-container-header .product-submit {
    display: flex;
    justify-content: end;
    width: 100%;
    gap: 16px;
}

.products-container .products-list {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 16px;
    height: 100%;
}


.products-container .products-list .product-item {
    background-color: #fff;
    box-shadow: 0 0 0 2px #ffffff, 0 0 0 4px rgb(172, 169, 169), 0 1px 2px 0 black;
    padding: 10px;
    border-radius: 8px;
    width: 22%;
    position: relative;
    cursor: pointer;
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
</style>
  