<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter()

const emit = defineEmits(['loadSensors'])
const props = defineProps({
    sensors: {
        type: Object,
        required: true
    }
})

const sensors = ref(Object.assign({}, props.sensors))

const navigateTo = (path) => {
    router.push({ path: path })
}

watch(
    () => props,
    (newProps) => {
        sensors.value = Object.assign({}, newProps.sensors)
    },
    { immediate: true }
)
</script>

<template>
    <VTable fixed-header>
        <thead>
            <tr>
                <th class="text-uppercase">
                    Código
                </th>
                <th>
                    Tipo
                </th>
                <th>
                    Ações
                </th>
            </tr>
        </thead>

        <tbody>
            <tr v-for="item in sensors" :key="item.code">
                <td style="width: 20%;">
                    {{ "S" + item.code }}
                </td>
                <td style="width: 100%; text-align: center;">
                    {{ item.type }}
                </td>
                <td class="d-flex align-center justify-end gap-x-2" style="width: fit-content">
                    <VBtn rel="noopener noreferrer" color="primary" @click="navigateTo('/sensor/' + item.code)">
                        <VIcon size="20" icon="bx-show" />
                        <VTooltip activator="parent" location="top">
                            <span>Ver Detalhes</span>
                        </VTooltip>
                    </VBtn>
                </td>

            </tr>
        </tbody>
    </VTable>
</template>

<style scoped></style>