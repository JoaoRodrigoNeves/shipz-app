<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from "primevue/usetoast";
import { useConfirm } from "primevue/useconfirm";

import ProductTable from '@/views/pages/tables/ProductTable.vue'
import PackageForm from '@/views/pages/form-layouts/PackageForm.vue'

const axios = inject('axios')
const router = useRouter()
const confirm = useConfirm()
const toast = useToast()

const isLoading = ref(false)
const isCreatingOrUpdatingProductPackage = ref(false)
const productPackage = ref([])
const productPackageToUpdate = ref(null)
const products = ref([])

const loadProductPackage = async () => {
    isLoading.value = true;
    await axios.get('product-packages/' + router.currentRoute.value.params.code)
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
    await axios.get('product-packages/' + router.currentRoute.value.params.code + '/products')
        .then(response => {
            isLoading.value = false
            products.value = response.data
        }).catch(error => {
            isLoading.value = false
            console.error(error)
        })
}

/*
const addProduct = async () => {
    isLoading.value = true
    await axios.post('product-packages/' + router.currentRoute.value.params.code + '/products/add')
        .then(response => {
            isLoading.value = false
        }).catch(error => {
            isLoading.value = false
            console.error(error)
            toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema!', life: 3000 });
        })
}
*/

onMounted(async () => {
    await loadProductPackage()
    await loadProducts()
})

</script>

<template>
    <VRow v-if="!isCreatingOrUpdatingProductPackage">
        <VCol cols="12">
            <VCard v-if="productPackage">
                <div class="product-package-details-header">
                    <h2>{{ productPackage.name }}</h2>
                    <div class="product-package-details-actions">
                        <!--
                        <VBtn rel="noopener noreferrer" color="primary" @click="updateProductPackage(productPackage)">
                            <VIcon size="20" icon="bx-pencil" />
                            <VTooltip activator="parent" location="top">
                                <span>Editar Embalagem</span>
                            </VTooltip>
                        </VBtn>
                        -->
                        <!--
                        <VBtn rel="noopener noreferrer" color="primary" v-if="productPackage && productPackage.length == 0"
                            @click="deleteProductPackageConfirm(productPackage)">
                            <VIcon size="20" icon="bx-trash" />
                            <VTooltip activator="parent" location="top">
                                <span>Apagar Embalagem</span>
                            </VTooltip>
                        </VBtn>
                        -->
                    </div>

                </div>

                <div class="product-package-details">
                    <div class="product-package-item">
                        <label>
                            Código
                        </label>
                        <span>
                            {{ productPackage.code }}
                        </span>
                    </div>
                    <div class="product-package-item">
                        <label>
                            Tipo
                        </label>
                        <span>
                            {{ productPackage.type }}
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
                            Estado
                        </label>
                        <span>
                            {{ productPackage.status }}
                        </span>
                    </div>
                    <div class="product-package-item">
                        <label>
                            Data de Fabrico
                        </label>
                        <span>
                            {{ productPackage.manufacturingDate }}
                        </span>
                    </div>

                </div>
                <div class="products-actions">
                    <h2>Produtos</h2>
                    <!--
                    <VBtn rel="noopener noreferrer" color="primary" @click="addProduct">
                        <VIcon size="20" icon="bx-plus" />
                        <VTooltip activator="parent" location="top">
                            <span>Adicionar Produto</span>
                        </VTooltip>
                    </VBtn>
                    -->
                </div>
                <div v-if="products && products.length > 0 && !isLoading">
                    <ProductTable v-if="!isLoading" @loadProducts="loadProducts" :products="products" />
                </div>
                <div v-else class="no-products">
                    Não tem produtos associados a esta embalagem
                </div>
            </VCard>
        </VCol>
    </VRow>
    <VCard v-if="isCreatingOrUpdatingProductPackage">
        <VCard>
            <div class="product-packages-header">
                <h2>Editar Embalagem</h2>
            </div>
            <VCardText>
                <PackageForm @closeFormAndUpdate="closeFormAndUpdate" :packageToUpdate="packageToUpdate"
                    :isCreating="isCreating"></PackageForm>
            </VCardText>
        </VCard>
    </VCard>
</template>

<style scoped>
.product-package-details-header {
    display: flex;
    justify-content: space-between;
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