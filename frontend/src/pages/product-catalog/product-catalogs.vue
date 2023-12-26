<script setup>
import { ref, onMounted, inject } from 'vue'
import ProductCatalogTable from '@/views/pages/tables/ProductCatalogTable.vue'

const axios = inject('axios')
const isLoading = ref(false)

const productCatalogs = ref([])



const loadProductCatalogs = async () => {
    isLoading.value = true;
    try {
        await axios.get('product-manufacters/'+ JSON.parse(sessionStorage.getItem('user_info')).username + '/product-catalogs').then(response => {
            isLoading.value = false;
            productCatalogs.value = response.data

        })
    } catch (error) {
        isLoading.value = false;
        console.log(error)
    }
}

onMounted(async () => {
    await loadProductCatalogs();
})
</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard title="Catálogo de Produtos">
                <ProductCatalogTable v-if="productCatalogs && !isLoading" :productCatalogs="productCatalogs"/>
            </VCard>
        </VCol>
    </VRow>
</template>
