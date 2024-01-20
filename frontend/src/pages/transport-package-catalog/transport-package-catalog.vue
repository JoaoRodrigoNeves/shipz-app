<script setup>
import { ref, onMounted, inject } from 'vue'
import TransportPackageCatalogTable from '@/views/pages/tables/TransportPackageCatalogTable.vue'
import TransportPackageCatalogForm from '@/views/pages/form-layouts/TransportPackageCatalogForm.vue'

const axios = inject('axios')
const isLoading = ref(false)
const isCreating = ref(false)
const transportPackages = ref([])

const loadTransportPackagesCatalogs = async () => {
    isLoading.value = true;
    await axios.get('transport-package-catalogs').then(response => {
        isLoading.value = false;
        transportPackages.value = response.data
    }).catch(
        error => {
            isLoading.value = false;
            console.error(error)
        }
    )
}

const closeFormAndUpdate = async () => {
    isCreating.value = false
    await loadTransportPackagesCatalogs()
}

onMounted(async () => {
    await loadTransportPackagesCatalogs();
})
</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard v-if="!isCreating">
                <div class="transport-package-header">
                    <h2>Embalagens de Transporte</h2>
                    <VBtn rel="noopener noreferrer" color="primary" @click="isCreating = true">
                        <VIcon size="20" icon="bx-plus" />
                        <VTooltip activator="parent" location="top">
                            <span>Criar Embalagem de Transporte</span>
                        </VTooltip>
                    </VBtn>
                </div>
                <div v-if="transportPackages && transportPackages.length > 0">
                    <TransportPackageCatalogTable v-if="transportPackages && !isLoading"
                        @loadTransportPackageCatalogs="loadTransportPackagesCatalogs"
                        :transportPackagesCatalogs="transportPackages" />
                </div>
                <div v-else class="no-transport-packages">
                    Não há embalagens de transporte registadas
                </div>
            </VCard>
            <VCard v-else>
                <VCard>
                    <div class="transport-package-header">
                        <h2>Criar Embalagem de Transporte</h2>
                    </div>
                    <VCardText>
                        <TransportPackageCatalogForm @closeFormAndUpdate="closeFormAndUpdate"></TransportPackageCatalogForm>
                    </VCardText>
                </VCard>
            </VCard>
        </VCol>
    </VRow>
</template>
<style scoped>
.transport-package-header {
    display: flex;
    justify-content: space-between;
    align-self: center;
    padding: 24px;
}

.no-transport-packages {
    padding: 0 24px 24px 24px;
}
</style>