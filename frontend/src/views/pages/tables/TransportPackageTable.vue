<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import moment from 'moment'
import { useConfirm } from "primevue/useconfirm";

const router = useRouter()
const confirm = useConfirm();
const isLoading = ref(false);
const axios = inject('axios')
const emit = defineEmits(['loadTransportPackages'])

const props = defineProps({
    transportPackages: {
        type: Object,
        required: true
    },
    canDelete: {
        type: Boolean,
        required: false
    }
})

const transportPackages = ref(Object.assign({}, props.transportPackages))

const formatDate = (value) => {
    return moment(String(value)).format('DD/MM/YYYY HH:mm:ss')
}

const removeTransportPackage = (transportPackage) => {
    confirm.require({
        message: 'Tem a certeza que pretende apagar a embalagem de transporte ' + transportPackage.code + ' ?',
        header: 'Apagar Embalagem de Transporte',
        rejectLabel: 'Não',
        acceptLabel: 'Sim',
        accept: async () => {
            isLoading.value = true;

            await axios.delete('transport-packages/' + transportPackage.code).then(response => {
                isLoading.value = false
                emit('loadTransportPackages')
            }).catch(
                error => {
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
        transportPackages.value = Object.assign({}, newProps.transportPackages)
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
                    Material
                </th>
                <th>
                    Volume
                </th>
                <th>
                    Data de Criação
                </th>
                <th v-if="canDelete">
                    Ações 
                </th>
            </tr>
        </thead>

        <tbody>
            <tr v-for="item in transportPackages" :key="item.code">
                <td style="width: 20%;">
                    {{ item.code }}
                </td>
                <td style="width: 20%; text-align: center;">
                    {{ item.material }}
                </td>
                <td style="width: 30%; text-align: center;">
                    {{ item.volume + " cm³" }}
                </td>
                <td style="width: 30%; text-align: center;">
                    {{ formatDate(item.createdAt) }}
                </td>
                <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
                    <VBtn rel="noopener noreferrer" color="primary" @click="removeTransportPackage(item)">
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