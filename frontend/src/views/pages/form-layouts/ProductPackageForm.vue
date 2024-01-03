<script setup>
import { onMounted } from 'vue';
import { ref, inject } from 'vue'
import { useToast } from 'primevue/usetoast';

const axios = inject('axios')

const toast = useToast()

const emit = defineEmits(['addProduct'])
const props = defineProps({
    packageCode: {
        type: Number,
        required: true
    }
})

const productCatalogs = ref([])
const products = ref([])
const packageForm = ref({
    catalogCode: null,
    productCode: null
})

const loadProductCatalogs = async () => {
    await axios.get('product-catalogs')
        .then(response => {
            productCatalogs.value = response.data
        }).catch(error => {
            console.error(error)
            toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema!', life: 3000 });
        })
}

const loadProducts = async () => {
    await axios.get('product-catalogs/' + packageForm.value.catalogCode + '/products/no-package')
        .then(response => {
            products.value = response.data
        }).catch(error => {
            console.error(error)
            toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema!', life: 3000 });
        })
}

const save = (async () => {
    await axios.post('product-packages/' + props.packageCode + '/products/' + packageForm.value.productCode)
        .then(async response => {
            emit('addProduct')
        }).catch(error => {
            if (error.response.status === 409)
                toast.add({ severity: 'error', summary: 'Erro', detail: 'Produto já está associado a uma embalagem!', life: 3000 });
            else {
                console.error(error)
                toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema!', life: 3000 });
            }
        })
})

onMounted(async () => {
    await loadProductCatalogs()
})

</script>

<template>
    <VForm @submit.prevent="save">
        <VRow>
            <VCol cols="12">
                <VAutocomplete v-model="packageForm.catalogCode" label="Catálogo" placeholder="Selecionar Catálogo"
                    :items="productCatalogs" @update:model-value="loadProducts()" item-title="name" item-value="code" />
            </VCol>
            <VCol cols="12" v-if="packageForm.catalogCode && products.length > 0">
                <VAutocomplete v-model="packageForm.productCode" label="Produto" placeholder="Selecionar Produto"
                    :items="products" item-title="code" item-value="code" />

            </VCol>
            <VCol cols="12" class="d-flex gap-4">
                <VBtn type="submit">
                    Adicionar
                </VBtn>
            </VCol>
        </VRow>
    </VForm>
</template>