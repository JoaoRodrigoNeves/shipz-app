<script setup>
import { ref, onMounted, inject } from 'vue'
import ProductTable from '@/views/pages/tables/ProductTable.vue'

const axios = inject('axios')
const isLoading = ref(false)
const isCreatingOrUpdating = ref(false)
const isCreating = ref(false)
const products = ref([])
const productToUpdate = ref(null)


const loadProducts = async () => {
    isLoading.value = true;
    try {
        await axios.get('product-manufacters/' + JSON.parse(sessionStorage.getItem('user_info')).username + '/products').then(response => {
            isLoading.value = false;
            products.value = response.data

        })
    } catch (error) {
        isLoading.value = false;
        console.log(error)
    }
}

const closeFormAndUpdate = async () => {
    isCreatingOrUpdating.value = false
    await loadProducts()
}

const updateProduct = async (product) => {
    productToUpdate.value = product
    isCreatingOrUpdating.value = true
    isCreating.value = false;
}

onMounted(async () => {
    await loadProducts();
})
</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard v-if="!isCreatingOrUpdating">
                <div class="product-catalogs-header">
                    <h2>Produtos</h2>
                    <VBtn rel="noopener noreferrer" color="primary" @click="isCreatingOrUpdating = true; isCreating = true">
                        <VIcon size="20" icon="bx-plus" />
                    </VBtn>
                </div>
                <ProductTable :products="products" />
            </VCard>
            <!--<VCard v-if="isCreatingOrUpdating">

                <VCard>
                    <div class="product-catalogs-header">
                        <h2>{{ isCreating ? 'Criar Catálogo' : 'Editar Catálogo' }}</h2>
                    </div>
                    <VCardText>
                        <ProductCatalogForm @closeFormAndUpdate="closeFormAndUpdate" :productCatalogToUpdate="productCatalogToUpdate" :isCreating="isCreating"></ProductCatalogForm>
                    </VCardText>
                </VCard>
            </VCard>-->
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