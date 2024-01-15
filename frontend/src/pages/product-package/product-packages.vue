<script setup>
import ProductPackageTable from '@/views/pages/tables/ProductPackageTable.vue';
import ProductPackageForm from '@/views/pages/form-layouts/ProductPackageForm.vue';
import { onMounted } from 'vue';
import { ref, inject } from 'vue'

const axios = inject('axios')
const isLoading = ref(false)
const isCreatingOrUpdating = ref(false)
const isCreating = ref(false)
const productPackages = ref([])
const productPackageToUpdate = ref(null)

const loadProductPackages = async () => {
    isLoading.value = true;
    await axios.get('product-packages/').then(
        response => {
            isLoading.value = false;
            productPackages.value = response.data
        }
    ).catch(
        error => {
            isLoading.value = false;
            console.error(error)
        }
    )
}

const updateProductPackage = async (productPackage) => {
    productPackageToUpdate.value = productPackage
    isCreatingOrUpdating.value = true
    isCreating.value = false
}

const closeFormAndUpdate = async () => {
    isCreatingOrUpdating.value = false
    productPackageToUpdate.value = null
    await loadProductPackages()
}

onMounted(async () => {
    await loadProductPackages();
})

</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard v-if="!isCreatingOrUpdating">
                <div class="product-packages-header">
                    <h2>Embalagens</h2>
                    <VBtn rel="noopener noreferrer" color="primary" @click="isCreatingOrUpdating = true; isCreating = true">
                        <VIcon size="20" icon="bx-plus" />
                        <VTooltip activator="parent" location="top">
                            <span>Criar Embalagem</span>
                        </VTooltip>
                    </VBtn>
                </div>
                <ProductPackageTable v-if="productPackages && productPackages.length > 0 && !isLoading"
                    @updateProductPackage="updateProductPackage" @loadProductPackages="loadProductPackages"
                    :productPackages="productPackages" />
            </VCard>
            <VCard v-if="isCreatingOrUpdating">
                <VCard>
                    <div class="product-packages-header">
                        <h2>{{ isCreating ? 'Criar Embalagem de Produto' : 'Editar Embalagem de Produto' }}</h2>
                    </div>
                    <VCardText>
                        <ProductPackageForm @closeFormAndUpdate="closeFormAndUpdate"
                            :productPackage="productPackageToUpdate" :is-adding-product="false" :is-creating="isCreating"
                            :is-updating="!isCreating" />
                    </VCardText>
                </VCard>
            </VCard>
        </VCol>
    </VRow>
</template>

<style scoped>
.product-packages-header {
    display: flex;
    justify-content: space-between;
    align-self: center;
    padding: 24px;
}
</style>