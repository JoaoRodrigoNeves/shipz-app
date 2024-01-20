<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const router = useRouter()
const toast = useToast()
const confirm = useConfirm();
const axios = inject('axios')
const isLoading = ref(false)


const emit = defineEmits(['loadTransportPackageCatalogs'])
const props = defineProps({
    transportPackagesCatalogs: {
        type: Object,
        required: true
    }
})

const transportPackagesCatalogs = ref(Object.assign({}, props.transportPackagesCatalogs))

const navigateTo = (path) => {
    router.push({ path: path })
}

const removeTransportPackageCatalog = (transportPackage) => {

    confirm.require({
        message: 'Tem a certeza que pretende apagar a embalagem de transporte ' + transportPackage.name + ' ?',
        header: 'Apagar Embalagem de Transporte',
        rejectLabel: 'Não',
        acceptLabel: 'Sim',
        accept: async () => {
            isLoading.value = true;

            await axios.delete('transport-package-catalogs/' + transportPackage.code).then(response => {
                isLoading.value = false
                emit('loadTransportPackageCatalogs')
            }).catch(
                error => {
                    toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possivel apagar a embalagem de transporte com o código TP' + transportPackage.code, life: 3000 });
                    isLoading.value = false;
                    console.error(error)
                }
            )
        }
    });
}

watch(
    () => props,
    (newProps) => {
        transportPackagesCatalogs.value = Object.assign({}, newProps.transportPackagesCatalogs)
    },
    { immediate: true }
)
</script>

<template>
    <VTable fixed-header>
        <thead>
            <tr>
                <th class="text-uppercase">
                    Código
                </th>
                <th>
                    Nome
                </th>
                <th>
                    Material
                </th>
                <th>
                    Volume
                </th>
                <th>
                    Ações
                </th>
            </tr>
        </thead>

        <tbody>
            <tr v-for="item in transportPackagesCatalogs" :key="item.code">
                <td style="width: 20%;">
                    {{ item.code }}
                </td>
                <td style="width: 20%; text-align: center;">
                    {{ item.name }}
                </td>
                <td style="width: 30%; text-align: center;">
                    {{ item.material }}
                </td>
                <td style="width: 30%; text-align: center;">
                    {{ item.volume + " cm³" }}
                </td>
                <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
                    <VBtn rel="noopener noreferrer" color="primary" @click="navigateTo('/transport-packages/' + item.code)">
                        <VIcon size="20" icon="bx-show" />
                        <VTooltip activator="parent" location="top">
                            <span>Ver Detalhes</span>
                        </VTooltip>
                    </VBtn>
                    <VBtn rel="noopener noreferrer" color="primary" @click="removeTransportPackageCatalog(item)">
                        <VIcon size="20" icon="bx-trash" />
                        <VTooltip activator="parent" location="top">
                            <span>Remover Embalagem de Transporte</span>
                        </VTooltip>
                    </VBtn>
                </td>

            </tr>
        </tbody>
    </VTable>
</template>

<style scoped></style>