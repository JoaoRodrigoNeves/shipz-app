<script setup>
</script>

<template>
    <VForm @submit.prevent="save">
        <VRow>
            <VCol cols="12">
                <VSelect v-model="packageForm.productCode" label="Produtor" placeholder="Selecionar Produto"
                    :items="products" item-title="name" item-value="code" />
            </VCol>

            <VCol cols="12" class="d-flex gap-4">
                <VBtn type="submit">
                    {{ props.isCreating ? 'Criar' : 'Editar' }}
                </VBtn>
            </VCol>
        </VRow>
    </VForm>
</template>