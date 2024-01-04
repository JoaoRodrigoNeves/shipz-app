
import productCatalogDetailsVue from '../product-catalog/product-catalog-details.vue';

<script setup>
import { ref, onMounted } from 'vue';
const axios = inject('axios')

const isLoading = ref(false);
const productCatalogs = ref([]);

const loadProductCatalogs = async () => {
    isLoading.value = true;
    try {
        await axios.get('product-catalogs').then(response => {
            isLoading.value = false;
            productCatalogs.value = response.data

        })
    } catch (error) {
        isLoading.value = false;
        console.log(error)
    }
}

const checkIfProductAreSelected = async (catalogCode) => {
    
}



const addProduct = async (catalogCode) => {
    
}

onMounted(async () => {
    await loadProductCatalogs();
})
</script>
<template>
    <div class="products-container">
        <div class="title">
            Produtos
        </div>
        <div class="products-list" >
            <div class="product-item" @click="addProduct" v-for="productCatalog in productCatalogs" :class="{'checked': checkIfProductAreSelected(productCatalog.code)}">
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
 .products-container{
    padding: 12px;
 }

 .products-container .title{
    font-size: 18px;
    margin-bottom: 24px;
 }

 .products-container .products-list{
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 16px;
 }

 .products-container .products-list .product-item{
    background-color: #fff;
    box-shadow: 0 0 0 2px #ffffff, 0 0 0 4px rgb(172, 169, 169), 0 1px 2px 0 black;
    padding: 10px;
    border-radius: 8px;
    width: 22%;
 }

 .products-container .products-list .product-item .product-header{
    display: flex;
    align-items: center;
    justify-content: space-between;
 }

 .products-container .products-list .product-item .product-content{
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: 700;
    color: #00000080;
    height: 100px;
    font-size: 18px;
 }

 .products-container .products-list .product-item .checked{
    background-color: red;
 }

 .products-container .products-list .product-item .product-footer{
    text-align: center;
 }
</style>
  