<script setup>
import { ref, onMounted, inject } from 'vue'
import { useToast } from "primevue/usetoast";
const toast = useToast();
const axios = inject('axios')

const emit = defineEmits(['closeFormAndUpdate'])

const productCatalogForm = ref({
    code: 6,
    name: '',
    productManufacterUsername: JSON.parse(sessionStorage.getItem('user_info')).username
})

const save = (async () => {

    try {
        const response = await axios.post('product-catalogs', productCatalogForm.value)

        if (response.status == 201) {
            toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Catálogo criado com sucesso', life: 3000 });

            productCatalogForm.value = {
                code: '',
                name: '',
                productManufacterUsername: JSON.parse(sessionStorage.getItem('user_info')).username
            }

            emit('closeFormAndUpdate')
        }

    } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Ocorreu um problema ao entrar na aplicação!', life: 3000 });

    }
})
</script>

<template>
    <VForm @submit.prevent="save">
        <VRow>
            <VCol cols="12">
                <VTextField v-model="productCatalogForm.name" label="Nome do Catálogo" placeholder="Iphone 15" />
            </VCol>
            <VCol cols="12" class="d-flex gap-4">
                <VBtn type="submit">
                    Criar
                </VBtn>

                <VBtn type="reset" color="secondary" variant="tonal">
                    Reset
                </VBtn>
            </VCol>
        </VRow>
    </VForm>
</template>