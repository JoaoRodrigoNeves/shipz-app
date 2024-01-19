<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import moment from 'moment'

const router = useRouter()

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

const formatDate = (value) => {
    return moment(String(value)).format('DD/MM/YYYY HH:mm:ss')
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
                    {{ "PP" +item.code }}
                </td>
                <td style="width: 20%; text-align: center;">
                    {{ item.typeName }}
                </td>
                <td style="width: 30%; text-align: center;">
                    {{ item.material }}
                </td>
                <td style="width: 30%; text-align: center;">
                    {{ formatDate(item.createdAt) }}
                </td>
                <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
                    <VBtn rel="noopener noreferrer" color="primary" @click="navigateTo('/product-package/' + item.code)">
                        <VIcon size="20" icon="bx-show" />
                        <VTooltip activator="parent" location="top">
                            <span>Ver Detalhes</span>
                        </VTooltip>
                    </VBtn>
                </td>

            </tr>
        </tbody>
    </VTable>
</template>

<style scoped></style>
