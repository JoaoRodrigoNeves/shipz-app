<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from "primevue/usetoast";
import { useConfirm } from "primevue/useconfirm";
import moment from 'moment'
import ProductTable from '@/views/pages/tables/ProductTable.vue'

const axios = inject('axios')
const router = useRouter()
const toast = useToast()
const confirm = useConfirm()

const isLoading = ref(false)
const isUpdating = ref(false)
const isAddingProduct = ref(false)
const productPackage = ref([])
const products = ref([])

const packageCode = router.currentRoute.value.params.code

const loadProductPackage = async () => {
    isLoading.value = true;
    await axios.get('product-packages/' + packageCode)
        .then(response => {
            isLoading.value = false
            productPackage.value = response.data
        }).catch(error => {
            isLoading.value = false
            console.error(error)
        })
}

const loadProducts = async () => {
    isLoading.value = true
    await axios.get('product-packages/' + packageCode + '/products')
        .then(response => {
            isLoading.value = false
            products.value = response.data
        }).catch(error => {
            isLoading.value = false
            console.error(error)
        })
}

const formatDate = (value) => {
    return moment(String(value)).format('DD/MM/YYYY HH:mm:ss')
}

const goBack = (value) => {
    router.back();
}


onMounted(async () => {
  await loadProductPackage()
  await loadProducts()
})
</script>

<template>
    <VRow v-if="!isUpdating && !isAddingProduct">
        <VCol cols="12">
            <VCard v-if="productPackage">
                <div class="product-package-details-header">
                    <VIcon size="35" icon="mdi-arrow-left-bold-circle" @click="goBack" style="cursor: pointer;"/>
                    <h2>Embalagem de Produto - {{ "PP" +productPackage.code }}</h2>
                </div>
                <div class="product-package-details">
                    <div class="product-package-item">
                        <label>
                            Código
                        </label>
                        <span>
                            {{ "PP" + productPackage.code }}
                        </span>
                    </div>
                    <div class="product-package-item">
                        <label>
                            Tipo
                        </label>
                        <span>
                            {{ productPackage.typeName }}
                        </span>
                    </div>
                    <div class="product-package-item">
                        <label>
                            Material
                        </label>
                        <span>
                            {{ productPackage.material }}
                        </span>
                    </div>
                    <div class="product-package-item">
                        <label>
                            Data de Fabrico
                        </label>
                        <span>
                            {{ formatDate(productPackage.createdAt) }}
                        </span>
                    </div>

                </div>
                <div class="products-actions">
                    <h2>Produtos</h2>
                </div>
                <div v-if="products && products.length > 0 && !isLoading">
                    <ProductTable v-if="!isLoading" @loadProducts="loadProducts"
                        :product-package-view="true" :products="products" />
                </div>
                <div v-else class="no-products">
                    Não tem produtos associados a esta embalagem
                </div>
            </VCard>
        </VCol>
    </VRow>
</template>

<style scoped>
.product-package-details-header {
    display: flex;
    justify-content: start;
    gap: 12px;
    align-items: center;
    padding: 24px;
}

.product-package-details-actions {
    display: flex;
    align-items: center;
    gap: 8px;
}

.product-package-details {
    display: flex;
    padding: 0 24px;
    gap: 16px 0px;
    flex-wrap: wrap;
}

.product-package-details .product-package-item {
    display: flex;
    flex-direction: column;
    width: 50%;
}

.product-package-details .product-package-item label {
    opacity: 0.7;
    font-size: 14px;
}

.product-package-details .product-package-item span {
    text-transform: capitalize;
}

.products-actions {
    display: flex;
    justify-content: space-between;
    padding: 24px;
}

.no-products {
    padding: 0 24px 24px 24px;
}

.product-packages-header {
    display: flex;
    justify-content: space-between;
    align-self: center;
    padding: 24px;
}
</style>
