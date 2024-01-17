<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from "primevue/usetoast";
import { useConfirm } from "primevue/useconfirm";
import SensorTable from '@/views/pages/tables/SensorsTable.vue'

const axios = inject('axios')
const isLoading = ref(false)
const router = useRouter()
const confirm = useConfirm();
const toast = useToast();

</script>

<template>
    
</template>
<style scoped>

</style>