<script setup>
import { ref, onMounted, inject } from 'vue'
import ProductCatalogTable from '@/views/pages/tables/ProductCatalogTable.vue'
import ProductCatalogForm from '@/views/pages/form-layouts/ProductCatalogForm.vue'

const axios = inject('axios')
const isLoading = ref(false)
const isCreatingOrUpdating = ref(false)
const isCreating = ref(false)
const productCatalogs = ref([])
const productCatalogToUpdate = ref(null)


const loadProductCatalogs = async () => {
    isLoading.value = true;
    try {
        await axios.get('product-manufacters/' + JSON.parse(sessionStorage.getItem('user_info')).username + '/product-catalogs').then(response => {
            isLoading.value = false;
            productCatalogs.value = response.data

        })
    } catch (error) {
        isLoading.value = false;
        console.log(error)
    }
}

const closeFormAndUpdate = async () => {
    isCreatingOrUpdating.value = false
    await loadProductCatalogs()
}

const updateProductCatalog = async (productCatalog) => {
    productCatalogToUpdate.value = productCatalog
    isCreatingOrUpdating.value = true
    isCreating.value = false;
}


onMounted(async () => {
    await loadProductCatalogs();
})
</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard v-if="!isCreatingOrUpdating">
                <div class="product-catalogs-header">
                    <h2>Catálogo de Produtos</h2>
                    <VBtn rel="noopener noreferrer" color="primary" @click="isCreatingOrUpdating = true; isCreating = true">
                        <VIcon size="20" icon="bx-plus" />
                    </VBtn>
                </div>
                <ProductCatalogTable v-if="productCatalogs && !isLoading" @updateProductCatalog="updateProductCatalog" :productCatalogs="productCatalogs" />
            </VCard>
            <VCard v-if="isCreatingOrUpdating">

                <VCard>
                    <div class="product-catalogs-header">
                        <h2>{{ isCreating ? 'Criar Catálogo' : 'Editar Catálogo' }}</h2>
                    </div>
                    <VCardText>
                        <ProductCatalogForm @closeFormAndUpdate="closeFormAndUpdate" :productCatalogToUpdate="productCatalogToUpdate"></ProductCatalogForm>
                    </VCardText>
                </VCard>
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