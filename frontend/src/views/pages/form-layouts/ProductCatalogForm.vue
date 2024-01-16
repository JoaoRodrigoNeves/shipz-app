<script setup>
import { ref, onMounted, inject } from 'vue'
import { useToast } from "primevue/usetoast";
const toast = useToast();
const axios = inject('axios')

const emit = defineEmits(['closeFormAndUpdate'])
const isLoading = ref(false)

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
const isSecondaryPackageActive = ref(productCatalogToUpdate.value.maxSecondaryPackage != null)
const isTertiaryPackageActive = ref(productCatalogToUpdate.value.maxTertiaryPackage != null)
const productCatalogForm = ref({
    code: -1,
    name: '',
    catalogArea: '',
    category: '',
    description: '',
    productManufacterUsername: JSON.parse(sessionStorage.getItem('user_info')).username,
    maxSecondaryPackage: null,
    maxTertiaryPackage: null,
    primaryPackageMaterial: '',
    secondaryPackageMaterial: '',
    tertiaryPackageMaterial: ''
})

const save = (async () => {
    isLoading.value = true

    if (props.isCreating) {
        productCatalogForm.value.productManufacterUsername = JSON.parse(sessionStorage.getItem('user_info')).username

        await axios.post('product-catalogs', productCatalogForm.value).then(response => {
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
                isLoading.value = false
                emit('closeFormAndUpdate')
            }
        }).catch(
            error => {
                isLoading.value = false;
                console.error(error)
            }
        )
    } else {
        await axios.put('product-catalogs/' + productCatalogForm.value.code, productCatalogForm.value).then(response => {
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
                isLoading.value = false
                emit('closeFormAndUpdate')
            }
        }).catch(
            error => {
                isLoading.value = false;
                console.error(error)
            }
        )
    }
})


const resetPackageNumber = (isSecondaryPackage) => {
  if(isSecondaryPackage && !isSecondaryPackageActive.value){
    productCatalogForm.value.maxSecondaryPackage = null;
    productCatalogForm.value.secondaryPackageMaterial = '';
  }

  if(!isSecondaryPackage && !isTertiaryPackageActive.value){
    productCatalogForm.value.maxTertiaryPackage = null;
    productCatalogForm.value.tertiaryPackageMaterial = '';

  }
}

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
            <VCol cols="12">
                <div>Caixa Primária</div>
            </VCol>
            <VCol cols="12">
                <div class="package-item" :class="{'package-item-disabled': isSecondaryPackageActive}">
                    <VCheckbox :model-value="true" readonly></VCheckbox>
                    <VTextField type="number" readonly model-value="1" class="product-quantity" />
                    <VTextField v-model="productCatalogForm.primaryPackageMaterial" label="Material" class="product-quantity" />
                </div>
            </VCol>
            <VCol cols="12">
                <div>Caixa Secundária</div>
            </VCol>
            <VCol cols="12">
                <div class="package-item" :class="{'package-item-disabled': isSecondaryPackageActive}">
                    <VCheckbox v-model="isSecondaryPackageActive" @input="resetPackageNumber(true)"></VCheckbox>
                    <VTextField type="number" :disabled="!isSecondaryPackageActive" v-model="productCatalogForm.maxSecondaryPackage" class="product-quantity" />
                    <VTextField v-model="productCatalogForm.secondaryPackageMaterial" :disabled="!isSecondaryPackageActive" label="Material" class="product-quantity"/>
                </div>
            </VCol>
            <VCol cols="12">
                <div>Caixa Terciária</div>
            </VCol>
            <VCol cols="12">
                <div class="package-item" :class="{'package-item-disabled': isTertiaryPackageActive}">
                    <VCheckbox v-model="isTertiaryPackageActive" @input="resetPackageNumber(false)"></VCheckbox>
                    <VTextField type="number" :disabled="!isTertiaryPackageActive" v-model="productCatalogForm.maxTertiaryPackage" class="product-quantity" />
                    <VTextField v-model="productCatalogForm.tertiaryPackageMaterial" :disabled="!isTertiaryPackageActive" label="Material" class="product-quantity"/>
                </div>
                
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
.package-item{
    display: flex;
    align-items: center;
    gap: 16px;
}
</style>
