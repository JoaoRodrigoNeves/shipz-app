<script setup>
import { ref, onMounted, inject, computed } from 'vue'
import { useToast } from "primevue/usetoast";
const toast = useToast();
const axios = inject('axios')

const emit = defineEmits(['closeForm'])
const isLoading = ref(false)

const props = defineProps({
    isCreating: {
        type: Boolean,
        required: true
    }
})
const sensorForm = ref({
    type: null,
})

const validateForm = () => {
    return !sensorForm.value.type;
}

const save = (async () => {
    if (validateForm()) {
        toast.add({ severity: 'error', summary: 'Erro', detail: 'Selecione uma opção', life: 3000 });
        return;
    }
    isLoading.value = true

    if (props.isCreating) {
        await axios.post('sensors', sensorForm.value).then(response => {
            if (response.status == 201) {
                toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Sensor criado com sucesso', life: 3000 });
                sensorForm.value = {
                    type: null,
                }
                isLoading.value = false
                emit('closeForm')
            }
        }).catch(
            error => {
                isLoading.value = false;
                console.error(error)
            }
        )
    }
})
</script>

<template>
    <VForm @submit.prevent="save">
        <VRow>
            <VCol cols="12">
                <v-autocomplete label="Tipo de sensor" v-model="sensorForm.type"
                    :items="['Temperatura', 'Humidade', 'Pressão']"></v-autocomplete>
            </VCol>
            <VCol cols="12" class="d-flex gap-4">
                <VBtn type="submit">
                    {{ props.isCreating ? 'Criar' : 'Editar' }}
                </VBtn>

                <VBtn type="reset" color="secondary" variant="tonal">
                    Reset
                </VBtn>
            </VCol>
        </VRow>
    </VForm>
</template>
<style scoped>

</style>
