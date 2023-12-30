<script setup>
import { ref, inject } from 'vue'
import { useRouter } from 'vue-router';

const emit = defineEmits(['loadOrders'])
const axios = inject('axios')
const router = useRouter()

const props = defineProps({
    orders: {
        type: Object,
        required: true
    }
    
})

const orders = ref(Object.assign({}, props.orders))

const navigateTo = (path) => {
    router.push({ path: path })
}

watch(
    () => props,
    (newProps) => {
        orders.value = Object.assign({}, newProps.orders)
    },
    { immediate: true }
)
</script>

<template>
    <VTable fixed-header>
        <thead>
            <tr>
                <th class="text-uppercase">
                    Código de encomenda
                </th>
                <th>
                    Operador Logistico
                </th>
                <th>
                    Número de produtos
                </th>
                <th>
                    Ações
                </th>
            </tr>
        </thead>

        <tbody>
            <tr v-for="item in orders" :key="item.code">
                <td style="width: 30%;">
                    {{ item.code }}
                </td>
                <td style="width: 20%; text-align: center;">
                    {{ item.logisticOperator ? item.logisticOperator : "Sem operador logistico" }}
                </td>
                <td style="width: 100%; text-align: center;">
                    {{ item.productsDTO.length > 0 ? item.productsDTO.length : "Sem produtos" }}
                </td>
                <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
                    <VBtn rel="noopener noreferrer" color="primary" @click="navigateTo('order/' + item.code)">
                        <VIcon size="20" icon="bx-show" />
                        <VTooltip activator="parent" location="top">
                            <span>Ver Produtos</span>
                        </VTooltip>
                    </VBtn>
                </td>
            </tr>
        </tbody>
    </VTable>
</template>
<style scoped></style>