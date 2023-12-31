<script setup>
import { ref, inject, watch } from 'vue';
import { useConfirm } from "primevue/useconfirm";

const axios = inject('axios')

const confirm = useConfirm()

const emit = defineEmits(['loadProductPackages', 'updateProductPackage'])
const props = defineProps({
    productPackages: {
        type: Object,
        required: true
    }
})

const productPackages = ref(Object.assign({}, props.productPackages))

const updateProductPackage = (productPackage) => {
    emit('updateProductPackage', productPackage)
}

const deleteProductPackage = (productPackage) => {
    confirm.require({
        message: 'Tem a certeza que pretende apagar a embalagem ' + productPackage.code + ' ?',
        header: 'Apagar Embalagem',
        rejectLabel: 'Não',
        acceptLabel: 'Sim',
        accept: async () => {
            try {
                await axios.delete('product-packages/' + productPackage.code).then(response => {
                    emit('loadProductPackages')
                })
            } catch (error) {
                console.log(error)
            }
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
                    Estado
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
                <td style="width: 30%;">
                    {{ item.code }}
                </td>
                <td style="width: 100%; text-align: center;">
                    {{ item.type }}
                </td>
                <td style="width: 100%; text-align: center;">
                    {{ item.material }}
                </td>
                <td style="width: 100%; text-align: center;">
                    {{ item.status }}
                </td>
                <td style="width: 100%; text-align: center;">
                    {{ item.manufacturingDate }}
                </td>
                <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
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