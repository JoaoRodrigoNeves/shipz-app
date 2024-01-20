<script setup>
import { ref, inject } from 'vue'
import { useToast } from "primevue/usetoast"

const props = defineProps({

  productCatalogToUpdate: {
    type: Object,
    required: false,
  },
  isCreating: {
    type: Boolean,
    required: true,
  },
})

const emit = defineEmits(['closeFormAndUpdate'])
const toast = useToast()
const axios = inject('axios')

const isLoading = ref(false)

const productCatalogToUpdate = ref(Object.assign({}, props.productCatalogToUpdate))
const isSecondaryPackageActive = ref(productCatalogToUpdate.value.maxSecondaryPackage != null)
const isTertiaryPackageActive = ref(productCatalogToUpdate.value.maxTertiaryPackage != null)

const sensors = ["Temperatura", "Humidade", "Pressão", "Gps", "Dano"]

const productCatalogForm = ref({
  code: -1,
  name: null,
  catalogArea: null,
  category: null,
  description: null,
  productManufacterUsername: JSON.parse(sessionStorage.getItem('user_info')).username,
  maxSecondaryPackage: null,
  maxTertiaryPackage: null,
  primaryPackageVolume: null,
  primaryPackageMaterial: '',
  secondaryPackageMaterial: '',
  tertiaryPackageMaterial: '',
  sensors: null
})

const validateForm = () => {
  return !productCatalogForm.value.name ||
    !productCatalogForm.value.catalogArea ||
    !productCatalogForm.value.category ||
    !productCatalogForm.value.description ||
    !productCatalogForm.value.primaryPackageMaterial ||
    !productCatalogForm.value.primaryPackageVolume ||
    isSecondaryPackageActive.value ? !(productCatalogForm.value.maxSecondaryPackage && productCatalogForm.value.secondaryPackageMaterial) : false ||
      isTertiaryPackageActive.value ? !(productCatalogForm.value.maxTertiaryPackage && productCatalogForm.value.tertiaryPackageMaterial) : false
}

const save = (async () => {
  if (validateForm()) {
    toast.add({ severity: 'warn', summary: 'Campos por preencher', life: 3000 })
    return
  }
  isLoading.value = true

  if (props.isCreating) {
    productCatalogForm.value.productManufacterUsername = JSON.parse(sessionStorage.getItem('user_info')).username

    await axios.post('product-catalogs', productCatalogForm.value).then(response => {
      if (response.status == 201) {
        toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Catálogo criado com sucesso', life: 3000 })
        productCatalogForm.value = {
          code: -1,
          name: '',
          catalogArea: '',
          category: '',
          description: '',
          productManufacterUsername: JSON.parse(sessionStorage.getItem('user_info')).username,
          maxSecondaryPackage: null,
          maxTertiaryPackage: null,
          primaryPackageVolume: null,
          primaryPackageMaterial: '',
          secondaryPackageMaterial: '',
          tertiaryPackageMaterial: '',
          sensors: null
        }
        isLoading.value = false
        emit('closeFormAndUpdate')
      }
    }).catch(
      error => {
        toast.add({ severity: 'error', summary: 'Erro', detail: error.response.data, life: 3000 })
        isLoading.value = false
        console.error(error)
      },
    )
  } else {
    console.log(JSON.parse(sessionStorage.getItem('user_info')).username)
    await axios.put('product-catalogs/' + productCatalogForm.value.code, productCatalogForm.value).then(response => {
      if (response.status == 200) {
        toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Catálogo atualizado com sucesso', life: 3000 })
        productCatalogForm.value = {
          code: -1,
          name: '',
          catalogArea: '',
          category: '',
          description: '',
          productManufacterUsername: JSON.parse(sessionStorage.getItem('user_info')).username,
          maxSecondaryPackage: null,
          maxTertiaryPackage: null,
          primaryPackageVolume: null,
          primaryPackageMaterial: '',
          secondaryPackageMaterial: '',
          tertiaryPackageMaterial: '',
          sensors: null
        }
        isLoading.value = false
        emit('closeFormAndUpdate')
      }
    }).catch(
      error => {
        isLoading.value = false
        console.error(error)
      },
    )
  }
})


const resetPackageNumber = isSecondaryPackage => {
  if (isSecondaryPackage && !isSecondaryPackageActive.value) {
    productCatalogForm.value.maxSecondaryPackage = null
    productCatalogForm.value.secondaryPackageMaterial = ''
  }

  if (!isSecondaryPackage && !isTertiaryPackageActive.value) {
    productCatalogForm.value.maxTertiaryPackage = null
    productCatalogForm.value.tertiaryPackageMaterial = ''

  }
}

watch(
  () => props,
  newProps => {
    productCatalogToUpdate.value = Object.assign({}, newProps.productCatalogToUpdate)
    productCatalogForm.value = productCatalogToUpdate.value
  },
  { immediate: true },
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
        <VSelect v-model="productCatalogForm.sensors" :items="sensors" label="Sensor" multiple />
      </VCol>
      <VCol cols="12">
        <div>Caixa Primária</div>
      </VCol>
      <VCol cols="6">
        <div class="package-item" :class="{ 'package-item-disabled': isSecondaryPackageActive }">
          <VCheckbox :model-value="true" readonly />
          <VTextField type="number" readonly model-value="1" class="product-quantity" style="width: 200px;"
            label="Quantidade" />
          <VTextField v-model="productCatalogForm.primaryPackageVolume" type="number" class="product-quantity"
            style="width: 300px;" label="Volume" />
          <VTextField v-model="productCatalogForm.primaryPackageMaterial" label="Material" class="product-quantity"
            style="width: 100%;" />
        </div>
      </VCol>
      <VCol cols="12">
        <div>Caixa Secundária</div>
      </VCol>
      <VCol cols="6">
        <div class="package-item" :class="{ 'package-item-disabled': isSecondaryPackageActive }">
          <VCheckbox v-model="isSecondaryPackageActive" @input="resetPackageNumber(true)" />
          <VTextField v-model="productCatalogForm.maxSecondaryPackage" type="number" :disabled="!isSecondaryPackageActive"
            class="product-quantity" label="Quantidade" />
          <VTextField v-model="productCatalogForm.secondaryPackageMaterial" :disabled="!isSecondaryPackageActive"
            label="Material" class="product-quantity" style="width: 100%;" />
        </div>
      </VCol>
      <VCol cols="12">
        <div>Caixa Terciária</div>
      </VCol>
      <VCol cols="6">
        <div class="package-item" :class="{ 'package-item-disabled': isTertiaryPackageActive }">
          <VCheckbox v-model="isTertiaryPackageActive" @input="resetPackageNumber(false)" />
          <VTextField v-model="productCatalogForm.maxTertiaryPackage" type="number" :disabled="!isTertiaryPackageActive"
            class="product-quantity" label="Quantidade" />
          <VTextField v-model="productCatalogForm.tertiaryPackageMaterial" :disabled="!isTertiaryPackageActive"
            label="Material" class="product-quantity" style="width: 100%;" />
        </div>
      </VCol>
      <VCol cols="12" class="d-flex gap-4">
        <VBtn type="submit" :disabled="isLoading">
          {{ props.isCreating ? 'Criar' : 'Editar' }}
        </VBtn>

        <VBtn type="reset" color="secondary" variant="tonal" :disabled="isLoading">
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
