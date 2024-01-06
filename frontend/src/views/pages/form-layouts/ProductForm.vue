<script setup>
import { ref, onMounted, inject } from 'vue'
import { useToast } from "primevue/usetoast";
const toast = useToast();
const axios = inject('axios')
const isLoading = ref(false)
const emit = defineEmits(['closeFormAndUpdate'])
const productCatalogs = ref([])
const props = defineProps({

    productToUpdate: {
        type: Object,
        required: false
    },
    isCreating: {
        type: Boolean,
        required: true
    }
})
const productToUpdate = ref(Object.assign({}, props.productToUpdate))

const productForm = ref({
    code: -1,
    productCatalogCode: null,
    productManufacterUsername: JSON.parse(sessionStorage.getItem('user_info')).username
})


const loadProductCatalogs = async () => {
    isLoading.value = true;
    await axios.get('product-manufacters/' + JSON.parse(sessionStorage.getItem('user_info')).username + '/product-catalogs').then(response => {
        productCatalogs.value = response.data
        if (productToUpdate.value.productCatalogCode >= 0) {
            productForm.value.productCatalogCode = response.data.find((e) => e.code == productToUpdate.value.productCatalogCode)
        }
        isLoading.value = false;

    }).catch(
        error => {
            isLoading.value = false;
            console.error(error)
        }
    )
}

const save = (async () => {
    isLoading.value = true;

    if (props.isCreating) {
        var payload = {
            productCatalogCode: productForm.value.productCatalogCode
        }

        await axios.post('products', payload).then(response => {
            if (response.status == 201) {
                toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto criado com sucesso', life: 3000, });

                emit('closeFormAndUpdate')
            }
            isLoading.value = false;

        }).catch(
            error => {
                isLoading.value = false;
                console.error(error)
            }
        )
    } else {
        var payload = {
            productCatalogCode: productForm.value.productCatalogCode,
            code: productForm.value.code
        }
        await axios.put('products', payload).then(response => {
            if (response.status == 200) {
                toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Catálogo atualizado com sucesso', life: 3000 });
                emit('closeFormAndUpdate')
            }
            isLoading.value = false;
        }).catch(
            error => {
                isLoading.value = false;
                console.error(error)
            }
        )
    }

})

watch(
    () => props,
    async (newProps) => {
        productToUpdate.value = Object.assign({}, newProps.productToUpdate)
        if (!productCatalogs.value) {
            await loadProductCatalogs();

        }
        if (productToUpdate.value.productCatalogCode >= 0) {
            productForm.value.code = newProps.productToUpdate.code
        }

    },
    { immediate: true }
)

onMounted(async () => {
    await loadProductCatalogs();

})
</script>

<template>
    <VForm @submit.prevent="save">
        <VRow>
            <VCol cols="12">
                <VSelect v-model="productForm.productCatalogCode" label="Catálogo" placeholder="Selecionar Catálogo"
                    :items="productCatalogs" item-title="name" item-value="code" />
            </VCol>

            <VCol cols="12" class="d-flex gap-4">
                <VBtn type="submit">
                    {{ props.isCreating ? 'Criar' : 'Editar' }}
                </VBtn>
            </VCol>
        </VRow>
    </VForm>
</template>
