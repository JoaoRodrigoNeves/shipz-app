<script setup>
import { ref, onMounted, inject, computed } from 'vue'
import { useToast } from "primevue/usetoast";
const toast = useToast();
const axios = inject('axios')

const emit = defineEmits(['closeFormAndUpdate'])
const isLoading = ref(false)

const transportPackageForm = ref({
    code: -1,
    name: null,
    material: null,
    volume: null,
    logisticOperatorUsername: JSON.parse(sessionStorage.getItem('user_info')).username
})

const validateForm = () => {
    return !transportPackageForm.value.name || !transportPackageForm.value.material || !transportPackageForm.value.volume;
}

const save = (async () => {
    console.log(transportPackageForm.value)
    if (validateForm()) {
        toast.add({ severity: 'error', summary: 'Erro', detail: 'O12312', life: 3000 });
        return;
    }
    isLoading.value = true

    await axios.post('transport-package-catalogs', transportPackageForm.value).then(response => {
        if (response.status == 201) {
            toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Catálogo criado com sucesso', life: 3000 });
            transportPackageForm.value = {
                code: -1,
                name: null,
                material: null,
                volume: null,
                logisticOperatorUsername: JSON.parse(sessionStorage.getItem('user_info')).username
            }
            isLoading.value = false
            emit('closeFormAndUpdate')
        }
    }).catch(
        error => {
            if (error.request.status == 409)
                toast.add({ severity: 'error', summary: 'Erro', detail: error.response.data, life: 3000 })
            else
                toast.add({ severity: 'error', summary: 'Erro', life: 3000 })
            isLoading.value = false;
            console.error(error)
        }
    )
})


const resetForm = () => {
    transportPackageForm.value = {
        code: -1,
        name: null,
        material: null,
        volume: null,
        logisticOperatorUsername: JSON.parse(sessionStorage.getItem('user_info')).username
    }
}
</script>

<template>
    <VForm @submit.prevent="save">
        <VRow>
            <VCol cols="12">
                <VTextField v-model="transportPackageForm.name" label="Nome" placeholder="Média" />
            </VCol>
            <VCol cols="12">
                <VTextField v-model="transportPackageForm.material" label="Material" placeholder="Cartão" />
            </VCol>
            <VCol cols="12">
                <VTextField type="number" v-model="transportPackageForm.volume" label="Volume" placeholder="20 cm³" />
            </VCol>
            <VCol cols="12" class="d-flex gap-4">
                <VBtn type="submit">
                    Criar
                </VBtn>

                <VBtn type="reset" color="secondary" variant="tonal" @click="resetForm">
                    Reset
                </VBtn>
            </VCol>
        </VRow>
    </VForm>
</template>
<style scoped>
.package-item {
    display: flex;
    align-items: center;
    gap: 16px;
}
</style>
