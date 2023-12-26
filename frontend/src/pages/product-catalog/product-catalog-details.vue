<script setup>
import { ref, onMounted, inject } from 'vue'
import ProductTable from '@/views/pages/tables/ProductTable.vue'
import { useRouter } from 'vue-router'

const axios = inject('axios')
const isLoading = ref(false)
const router = useRouter()

const products = ref([])

const loadProductCatalogDetails = async () => {
    isLoading.value = true;
    try {
        console.log()
        await axios.get('product-catalogs/' + router.currentRoute.value.params.code).then(response => {
            isLoading.value = false;
            console.log(response.data)
            products.value = response.data

        })
    } catch (error) {
        isLoading.value = false;
        console.log(error)
    }
}

const loadProductCatalogProducts = async () => {
    isLoading.value = true;
    try {
        console.log()
        await axios.get('product-catalogs/' + router.currentRoute.value.params.code + '/products').then(response => {
            isLoading.value = false;
            console.log(response.data)
            products.value = response.data

        })
    } catch (error) {
        isLoading.value = false;
        console.log(error)
    }
}

onMounted(async () => {
    await loadProductCatalogDetails();
    await loadProductCatalogProducts();
})
</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard title="Catálogo de Produtos">
                <ProductTable v-if="products && !isLoading" :products="products"/>
            </VCard>
        </VCol>
    </VRow>
</template>
