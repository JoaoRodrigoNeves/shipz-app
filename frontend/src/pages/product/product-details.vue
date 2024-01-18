<script setup>
import { ref, onMounted, inject } from 'vue'
import ProductPackageTable from '@/views/pages/tables/ProductPackageTable.vue';
import { useRouter } from 'vue-router'

const axios = inject('axios')
const isLoading = ref(false)
const router = useRouter()

const productCatalog = ref(null)
const productPackages = ref(null)

const loadProductCatalogDetails = async () => {
    isLoading.value = true;
    await axios.get('products/' + router.currentRoute.value.params.code + '/product-catalog').then(response => {
        isLoading.value = false;
        productCatalog.value = response.data
    }).catch(
        error => {
            isLoading.value = false;
            console.error(error)
        }
    )
}

const loadProductPackages = async () => {
    isLoading.value = true;
    await axios.get('products/' + router.currentRoute.value.params.code + '/product-package').then(response => {
        isLoading.value = false;
        productPackages.value = response.data
    }).catch(
        error => {
            isLoading.value = false;
            console.error(error)
        }
    )
}
const goBack = () => {
    router.back()
}

onMounted(async () => {
    await loadProductCatalogDetails();
    await loadProductPackages();
})
</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard v-if="productCatalog">
                <div class="product-details-header">
                    <VIcon size="35" icon="mdi-arrow-left-bold-circle" @click="goBack" />
                    <h2>{{ "Produto - P" + router.currentRoute.value.params.code }}</h2>
                </div>

                <div class="product-details">
                    <div class="catalog-item">
                        <label>
                            Código
                        </label>
                        <span>
                            {{ "P" + productCatalog.code }}
                        </span>
                    </div>
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
                    <h2>Embalagens de Produto</h2>
                </div>
                <div v-if="productPackages && productPackages.length > 0 && !isLoading">
                    <ProductPackageTable v-if="productPackages && productPackages.length > 0 && !isLoading" :productPackages="productPackages" />
                </div>
                <div v-else class="no-products">
                    Não tem embalagens de produto associados a este produto
                </div>
            </VCard>
        </VCol>
    </VRow>
</template>
<style scoped>
.product-details-header {
    display: flex;
    justify-content: start;
    gap: 12px;
    align-items: center;
    padding: 24px;
}

.product-details-actions {
    display: flex;
    align-items: center;
    gap: 8px;
}

.product-details {
    display: flex;
    padding: 0 24px;
    gap: 16px 0px;
    flex-wrap: wrap;
}

.product-details .catalog-item {
    display: flex;
    flex-direction: column;
    width: 50%;
}

.product-details .catalog-item label {
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