<script setup>
import { ref, onMounted, inject } from 'vue'
import ProductTable from '@/views/pages/tables/ProductTable.vue'

const axios = inject('axios')
const isLoading = ref(false)
const products = ref([])


const loadProducts = async () => {
    isLoading.value = true;
    await axios.get('product-manufacters/' + JSON.parse(sessionStorage.getItem('user_info')).username + '/products').then(response => {
        isLoading.value = false;
        products.value = response.data

    }).catch(
        error => {
            isLoading.value = false;
            console.error(error)
        }
    )
}

const closeFormAndUpdate = async () => {
    await loadProducts()
}

onMounted(async () => {
    await loadProducts();
})
</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard>
                <div class="product-catalogs-header">
                    <h2>Produtos</h2>
                </div>
                <ProductTable v-if="products && products.length > 0 && !isLoading"
                    @loadProducts="loadProducts" :product-package-view="false" :products="products" />
            </VCard>
        </VCol>
    </VRow>
</template>
<style scoped>
.product-catalogs-header {
    display: flex;
    justify-content: space-between;
    align-self: center;
    padding: 24px;
}
</style>