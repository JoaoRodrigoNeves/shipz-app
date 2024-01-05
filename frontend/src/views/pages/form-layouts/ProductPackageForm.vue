<script setup>
import { ref, inject, onMounted, watch } from 'vue'
import { useToast } from 'primevue/usetoast';
//import Calendar from 'primevue/calendar';

const axios = inject('axios')

const toast = useToast()

const emit = defineEmits(['closeFormAndUpdate', 'addProduct'])
const props = defineProps({
    productPackage: {
        type: Object,
        required: false
    },
    isCreating: {
        type: Boolean,
        required: true
    },
    isUpdating: {
        type: Boolean,
        required: true
    },
    isAddingProduct: {
        type: Boolean,
        required: true
    },
})

const productCatalogs = ref([])
const products = ref([])
const productPackage = ref(Object.assign({}, props.productPackage))
const productPackageForm = ref({
    material: null,
    manufacturingDate: null,
    catalogCode: null,
    productCode: null
})

//const maxDate = new Date()

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
    await axios.get('product-catalogs/' + productPackageForm.value.catalogCode + '/products/no-package')
        .then(response => {
            products.value = response.data
        }).catch(error => {
            console.error(error)
            toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema!', life: 3000 });
        })
}

const save = (async () => {

    if (props.isCreating)
        await axios.post('product-packages', {
            type: productPackageForm.value.type,
            material: productPackageForm.value.material,
            manufacturingDate: productPackageForm.value.manufacturingDate
        }).then(() => {
            toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Embalagem de produto criada com sucesso!', life: 3000 });
            emit('closeFormAndUpdate')
        }).catch(error => {
            console.error(error)
            toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema!', life: 3000 });
        })
    else if (props.isUpdating)
        await axios.put('product-packages', {
            code: productPackageForm.value.code,
            type: productPackageForm.value.type,
            material: productPackageForm.value.material,
            manufacturingDate: productPackageForm.value.manufacturingDate
        }).then(() => {
            toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Embalagem de produto atualizada com sucesso!', life: 3000 });
            emit('closeFormAndUpdate')
        }).catch(error => {
            console.error(error)
            toast.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema!', life: 3000 });
        })
    else if (props.isAddingProduct)
        await axios.post('product-packages/' + productPackage.value.code + '/products/' + productPackageForm.value.productCode
        ).then(() => {
            toast.add({ severity: 'info', summary: 'Produto Adicionado', life: 3000 });
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

watch(
    () => props,
    (newProps) => {
        productPackage.value = Object.assign({}, newProps.productPackage)
        productPackageForm.value = productPackage.value
    },
    { immediate: true }
)

onMounted(async () => {
    await loadProductCatalogs()
})

</script>

<template>
    <VForm @submit.prevent="save">
        <VRow v-if="props.isCreating || props.isUpdating">
            <VCol cols="12">
                <VTextField v-model="productPackageForm.type" label="Tipo" placeholder="Primário" />
            </VCol>
            <VCol cols="12">
                <VTextField v-model="productPackageForm.material" label="Material" placeholder="Cartão" />
            </VCol>
            <VCol cols="12">
                <VTextField v-model="productPackageForm.manufacturingDate" label="Data de fabrico"
                    placeholder="01/01/2024" />
                <!--<Calendar v-model="productPackageForm.manufacturingDate" date-format="dd/mm/yy" :max-date="maxDate" />-->
            </VCol>
            <VCol cols="12" class="d-flex gap-4">
                <VBtn type="submit">
                    {{ props.isCreating ? 'Criar' : 'Editar' }}
                </VBtn>
            </VCol>
        </VRow>
        <VRow v-if="props.isAddingProduct">
            <VCol cols="12">
                <VAutocomplete v-model="productPackageForm.catalogCode" label="Catálogo" placeholder="Selecionar Catálogo"
                    :items="productCatalogs" @update:model-value="loadProducts()" item-title="name" item-value="code" />
            </VCol>
            <VCol cols="12" v-if="productPackageForm.catalogCode && products.length > 0">
                <VAutocomplete v-model="productPackageForm.productCode" label="Produto" placeholder="Selecionar Produto"
                    :items="products" item-title="code" item-value="code" />
            </VCol>
            <VCol cols="12" class="d-flex gap-4" v-if="productPackageForm.productCode">
                <VBtn type="submit">
                    Adicionar
                </VBtn>
            </VCol>
        </VRow>
    </VForm>
</template>