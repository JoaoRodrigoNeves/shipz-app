<script setup>
import { ref, onMounted, inject } from 'vue'
import { useToast } from "primevue/usetoast";
const toast = useToast();
const axios = inject('axios')

const emit = defineEmits(['closeFormAndUpdate'])

const props = defineProps({

    productCatalogToUpdate: {
        type: Object,
        required: false
    },
    isCreating: {
        type: Boolean,
        required: true
    }
})
const productCatalogToUpdate = ref(Object.assign({}, props.productCatalogToUpdate))

const productCatalogForm = ref({
    code: -1,
    name: '',
    catalogArea: '',
    category: '',
    description: '',
    productManufacterUsername: JSON.parse(sessionStorage.getItem('user_info')).username
})

const save = (async () => {
    if (props.isCreating) {
        try {
            productCatalogForm.value.productManufacterUsername= JSON.parse(sessionStorage.getItem('user_info')).username

            const response = await axios.post('product-catalogs', productCatalogForm.value)

            if (response.status == 201) {
                toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Catálogo criado com sucesso', life: 3000 });

                productCatalogForm.value = {
                    code: -1,
                    name: '',
                    catalogArea: '',
                    category: '',
                    description: '',
                    productManufacterUsername: JSON.parse(sessionStorage.getItem('user_info')).username
                }

                emit('closeFormAndUpdate')
            }

        } catch (error) {
            toast.add({ severity: 'error', summary: 'Error', detail: 'Ocorreu um problema ao entrar na aplicação!', life: 3000 });

        }
    } else {
        try {
            const response = await axios.put('product-catalogs/' + productCatalogForm.value.code, productCatalogForm.value)

            if (response.status == 200) {
                toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Catálogo atualizado com sucesso', life: 3000 });

                productCatalogForm.value = {
                    code: -1,
                    name: '',
                    catalogArea: '',
                    category: '',
                    description: '',
                    productManufacterUsername: JSON.parse(sessionStorage.getItem('user_info')).username
                }
                emit('closeFormAndUpdate')
            }

        } catch (error) {
            toast.add({ severity: 'error', summary: 'Error', detail: 'Ocorreu um problema ao entrar na aplicação!', life: 3000 });
        }
    }

})

watch(
    () => props,
    (newProps) => {
        productCatalogToUpdate.value = Object.assign({}, newProps.productCatalogToUpdate)
        productCatalogForm.value = productCatalogToUpdate.value
    },
    { immediate: true }
)
</script>

<template>
    <VForm @submit.prevent="save">
        <VRow>
            <VCol cols="12">
                <VTextField v-model="productCatalogForm.name" label="Nome do Catálogo" placeholder="Iphone 15" />
            </VCol>
            <VCol cols="12">
                <VTextField v-model="productCatalogForm.catalogArea" label="Área do Catálogo" placeholder="Tecnologia" />
            </VCol>
            <VCol cols="12">
                <VTextField v-model="productCatalogForm.category" label="Categoria do Catálogo" placeholder="Telemóveis" />
            </VCol>
            <VCol cols="12">
                <VTextField v-model="productCatalogForm.description" label="Descrição" />
            </VCol>
            <VCol cols="12" class="d-flex gap-4">
                <VBtn type="submit">
                    {{props.isCreating ? 'Criar' : 'Editar'}}
                </VBtn>

                <VBtn type="reset" color="secondary" variant="tonal">
                    Reset
                </VBtn>
            </VCol>
        </VRow>
    </VForm>
</template>
