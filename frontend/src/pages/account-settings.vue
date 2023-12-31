<script setup>
import { useRoute } from 'vue-router'
import AccountSettingsAccount from '@/views/pages/account-settings/AccountSettingsAccount.vue'

const route = useRoute()
const activeTab = ref(route.params.tab)

const tabs = [
  {
    title: 'Conta',
    icon: 'bx-user',
    tab: 'account',
  },
]
</script>

<template>
  <div>
    <VTabs v-model="activeTab" show-arrows>
      <VTab v-for="item in tabs" :key="item.icon" :value="item.tab">
        <VIcon size="20" start :icon="item.icon" />
        {{ item.title }}
      </VTab>
    </VTabs>
    <VDivider />

    <VWindow v-model="activeTab" class="mt-5 disable-tab-transition">
      <VWindowItem value="account">
        <AccountSettingsAccount />
      </VWindowItem>

    </VWindow>
  </div>
</template>
