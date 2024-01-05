<script setup>
import { ref, inject, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from 'primevue/usetoast';

const axios = inject('axios')

const router = useRouter()
const confirm = useConfirm()
const toast = useToast()

const emit = defineEmits(['loadProductPackages', 'updateProductPackage'])
const props = defineProps({
    productPackages: {
        type: Object,
        required: true
    }
})

const productPackages = ref(Object.assign({}, props.productPackages))

const navigateTo = (path) => {
    router.push({ path: path })
}

const updateProductPackage = (productPackage) => {
    emit('updateProductPackage', productPackage)
}

const deleteProductPackage = (productPackage) => {
    confirm.require({
        message: 'Tem a certeza que pretende apagar a embalagem #' + productPackage.code + ' ?',
        header: 'Apagar Embalagem',
        rejectLabel: 'Não',
        acceptLabel: 'Sim',
        accept: async () => {
            await axios.delete('product-packages/' + productPackage.code).then(() => {
                toast.add({ severity: 'info', summary: 'Embalagem Apagada', life: 3000 });
                emit('loadProductPackages')
            }).catch(error => {
                console.error(error)
                toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema!', life: 3000 });
            })
        }
    })
}

watch(
    () => props,
    (newProps) => {
        productPackages.value = Object.assign({}, newProps.productPackages)
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
                    Tipo
                </th>
                <th>
                    Material
                </th>
                <th>
                    Data de Fabrico
                </th>
                <th>
                    Ações
                </th>
            </tr>
        </thead>

        <tbody>
            <tr v-for="item in productPackages" :key="item.code">
                <td style="width: 20%;">
                    {{ item.code }}
                </td>
                <td style="width: 30%; text-align: center;">
                    {{ item.type }}
                </td>
                <td style="width: 30%; text-align: center;">
                    {{ item.material }}
                </td>
                <td style="width: 10%; text-align: center;">
                    {{ item.manufacturingDate }}
                </td>
                <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
                    <VBtn rel="noopener noreferrer" color="primary" @click="navigateTo('product-package/' + item.code)">
                        <VIcon size="20" icon="bx-show" />
                        <VTooltip activator="parent" location="top">
                            <span>Ver Detalhes</span>
                        </VTooltip>
                    </VBtn>
                    <VBtn rel="noopener noreferrer" color="primary" @click="updateProductPackage(item)">
                        <VIcon size="20" icon="bx-pencil" />
                        <VTooltip activator="parent" location="top">
                            <span>Editar Embalagem</span>
                        </VTooltip>
                    </VBtn>
                    <VBtn rel="noopener noreferrer" color="primary" @click="deleteProductPackage(item)">
                        <VIcon size="20" icon="bx-trash" />
                        <VTooltip activator="parent" location="top">
                            <span>Apagar Embalagem</span>
                        </VTooltip>
                    </VBtn>

                </td>

            </tr>
        </tbody>
    </VTable>
</template>

<style scoped></style>