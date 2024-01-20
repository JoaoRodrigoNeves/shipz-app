<script setup>
import { ref, onMounted, inject } from 'vue'
import TransportPackageTable from '@/views/pages/tables/TransportPackageTable.vue'
import { useRouter } from 'vue-router'
import { useToast } from "primevue/usetoast";
import { useConfirm } from "primevue/useconfirm";

const axios = inject('axios')
const isLoading = ref(false)
const router = useRouter()
const confirm = useConfirm();
const toast = useToast();

const transportPackageCatalog = ref(null)

const loadTransportPackageCatalogDetails = async () => {
    isLoading.value = true;
    await axios.get('transport-package-catalogs/' + router.currentRoute.value.params.code).then(response => {
        isLoading.value = false;
        transportPackageCatalog.value = response.data
    }).catch(
        error => {
            isLoading.value = false;
            console.error(error)
        }
    )
}

const deleteTransportPackageCatalogConfirm = (transportPackageCatalogItem) => {
    confirm.require({
        message: 'Tem a certeza que pretende apagar o catálogo ' + transportPackageCatalogItem.name + ' ?',
        header: 'Apagar Catálogo',
        rejectLabel: 'Não',
        acceptLabel: 'Sim',
        accept: async () => {
            isLoading.value = true;

            await axios.delete('transport-package-catalogs/' + transportPackageCatalogItem.code).then(response => {
                isLoading.value = false
                router.back()
            }).catch(
                error => {
                    isLoading.value = false;
                    console.error(error)
                }
            )
        }
    });
}

const goBack = () => {
    router.back()
}

onMounted(async () => {
    await loadTransportPackageCatalogDetails();
})
</script>

<template>
    <VRow>
        <VCol cols="12">
            <VCard v-if="transportPackageCatalog">
                <div class="transport-package-catalog-details-header">
                    <div style="display: flex; align-items: center; gap: 12px;">
                        <VIcon size="35" icon="mdi-arrow-left-bold-circle" @click="goBack" style="cursor: pointer;" />
                        <h2>{{ "Embalagem de Transporte - " + transportPackageCatalog.name }}</h2>

                    </div>

                    <div class="transport-package-catalog-details-actions">
                        <VBtn rel="noopener noreferrer" color="primary" v-if="transportPackageCatalog.transportPackageDTOList && transportPackageCatalog.transportPackageDTOList.length == 0"
                            @click="deleteTransportPackageCatalogConfirm(transportPackageCatalog)">
                            <VIcon size="20" icon="bx-trash" />
                            <VTooltip activator="parent" location="top">
                                <span>Apagar Embalagem de Transporte</span>
                            </VTooltip>
                        </VBtn>
                    </div>
                </div>
                <div class="transport-package-catalog-details">
                    <div class="catalog-item">
                        <label>
                            Nome
                        </label>
                        <span>
                            {{ transportPackageCatalog.name }}
                        </span>
                    </div>
                    <div class="catalog-item">
                        <label>
                            Material
                        </label>
                        <span>
                            {{ transportPackageCatalog.material }}
                        </span>
                    </div>
                    <div class="catalog-item">
                        <label>
                            Volume
                        </label>
                        <span>
                            {{ transportPackageCatalog.volume + " cm³" }}
                        </span>
                    </div>
                </div>
                <div class="products-actions">
                    <h2>Embalagens</h2>
                </div>
                <div
                    v-if="transportPackageCatalog.transportPackageDTOList && transportPackageCatalog.transportPackageDTOList.length > 0 && !isLoading">
                    <TransportPackageTable v-if="!isLoading" :transport-packages="transportPackageCatalog.transportPackageDTOList" />
                </div>
                <div v-else class="no-transport-packages">
                    Não tem embalagens de transporte
                </div>
            </VCard>
        </VCol>
    </VRow>
</template>
<style scoped>
.transport-package-catalog-details-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24px;
}

.transport-package-catalog-details-actions {
    display: flex;
    align-items: center;
    gap: 8px;
}

.transport-package-catalog-details {
    display: flex;
    padding: 0 24px;
    gap: 16px 0px;
    flex-wrap: wrap;
}

.transport-package-catalog-details .catalog-item {
    display: flex;
    flex-direction: column;
    width: 50%;
}

.transport-package-catalog-details .catalog-item label {
    opacity: 0.7;
    font-size: 14px;
}

.products-actions {
    display: flex;
    justify-content: space-between;
    padding: 24px;
}

.products-actions .product-quantity {
    display: flex;
    align-items: center;
    gap: 12px;
}

.no-transport-packages {
    padding: 0 24px 24px 24px;
}

.transport-package-catalogs-header {
    display: flex;
    justify-content: space-between;
    align-self: center;
    padding: 24px;
}
</style>