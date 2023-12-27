<script setup>
import { ref, onMounted, inject } from 'vue'
import ProductTable from '@/views/pages/tables/ProductTable.vue'
import { useRouter } from 'vue-router'
import ProductCatalogForm from '@/views/pages/form-layouts/ProductCatalogForm.vue'

const axios = inject('axios')
const isLoading = ref(false)
const router = useRouter()

const products = ref([])
const productCatalog = ref(null)
const isCreatingOrUpdating = ref(false)
const productCatalogToUpdate = ref(null)
const loadProductCatalogDetails = async () => {
    isLoading.value = true;
    try {
        await axios.get('product-catalogs/' + router.currentRoute.value.params.code).then(response => {
            isLoading.value = false;
            productCatalog.value = response.data

        })
    } catch (error) {
        isLoading.value = false;
        console.log(error)
    }
}

const loadProductCatalogProducts = async () => {
    isLoading.value = true;
    try {
        await axios.get('product-catalogs/' + router.currentRoute.value.params.code + '/products').then(response => {
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
    await loadProductCatalogDetails()
}

const updateProductCatalog = async (productCatalog) => {
    productCatalogToUpdate.value = productCatalog
    isCreatingOrUpdating.value = true
}

onMounted(async () => {
    await loadProductCatalogDetails();
    await loadProductCatalogProducts();
})
</script>

<template>
    <VRow v-if="!isCreatingOrUpdating">
        <VCol cols="12">
            <VCard v-if="productCatalog">
                <div class="product-catalog-details-header">
                    <h2>{{ productCatalog.name }}</h2>
                    <div class="product-catalog-details-actions">
                        <VBtn rel="noopener noreferrer" color="primary" @click="updateProductCatalog(productCatalog)">
                            <VIcon size="20" icon="bx-pencil" />
                        </VBtn>
                        <VBtn rel="noopener noreferrer" color="primary" v-if="products && products.length == 0">
                            <VIcon size="20" icon="bx-trash" />
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
                    <VBtn rel="noopener noreferrer" color="primary">
                        <VIcon size="20" icon="bx-plus" />
                    </VBtn>
                </div>
                <div v-if="products && products.length > 0 && !isLoading">
                    <ProductTable :products="products" />
                </div>
                <div v-else class="no-products">
                    Não tem produtos associados a este catálogo
                </div>
            </VCard>
        </VCol>
    </VRow>
    <VCard v-if="isCreatingOrUpdating">

        <VCard>
            <div class="product-catalogs-header">
                <h2>Editar Catálogo</h2>
            </div>
            <VCardText>
                <ProductCatalogForm @closeFormAndUpdate="closeFormAndUpdate"
                    :productCatalogToUpdate="productCatalogToUpdate" :isCreating="false"></ProductCatalogForm>
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