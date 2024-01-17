<script setup>
import { useTheme } from 'vuetify'
import VerticalNavSectionTitle from '@/@layouts/components/VerticalNavSectionTitle.vue'
import upgradeBannerDark from '@images/pro/upgrade-banner-dark.png'
import upgradeBannerLight from '@images/pro/upgrade-banner-light.png'
import VerticalNavLayout from '@layouts/components/VerticalNavLayout.vue'
import VerticalNavLink from '@layouts/components/VerticalNavLink.vue'

// Components
import NavbarThemeSwitcher from '@/layouts/components/NavbarThemeSwitcher.vue'
import UserProfile from '@/layouts/components/UserProfile.vue'

const user = JSON.parse(sessionStorage.getItem('user_info'))

const vuetifyTheme = useTheme()

const upgradeBanner = computed(() => {
  return vuetifyTheme.global.name.value === 'light' ? upgradeBannerLight : upgradeBannerDark
})
</script>

<template>
  <VerticalNavLayout>
    <!-- 👉 navbar -->
    <template #navbar="{ toggleVerticalOverlayNavActive }">
      <div class="d-flex h-100 align-center">
        <!-- 👉 Vertical nav toggle in overlay mode -->
        <IconBtn class="ms-n3 d-lg-none" @click="toggleVerticalOverlayNavActive(true)">
          <VIcon icon="bx-menu" />
        </IconBtn>
        <VSpacer />

        <NavbarThemeSwitcher class="me-2" />

        <UserProfile />
      </div>
    </template>

    <template #vertical-nav-content>
      <VerticalNavSectionTitle :item="{
        heading: 'Conta',
      }" v-if="!user" />
      <VerticalNavLink :item="{
        title: 'Definições da Conta',
        icon: 'mdi-account-cog-outline',
        to: '/account-settings',
      }" v-if="user" />
      <VerticalNavSectionTitle :item="{
        heading: 'Gestão',
      }" v-if="user" />
      <VerticalNavLink :item="{
        title: 'Produtos',
        icon: 'mdi-baguette',
        to: '/products-list',
      }" v-if="user && user.role === 'FinalCostumer'" />
      <VerticalNavLink :item="{
        title: 'Catálogo',
        icon: 'mdi-list-box',
        to: '/product-catalogs',
      }" v-if="user && user.role === 'ProductManufacter'" />
      <VerticalNavLink :item="{
        title: 'Produtos',
        icon: 'mdi-baguette',
        to: '/products',
      }" v-if="user && user.role === 'ProductManufacter'" />
      <VerticalNavLink :item="{
        title: 'Embalagens de Produto',
        icon: 'mdi-package-variant-closed',
        to: '/product-packages'
      }" v-if="user && user.role == 'ProductManufacter'" />
      <VerticalNavLink :item="{
        title: 'Sensores',
        icon: 'mdi-leak',
        to: '/sensors'
      }" v-if="user && user.role == 'ProductManufacter'" />
      <VerticalNavLink :item="{
        title: 'Encomendas',
        icon: 'bi-box-seam',
        to: '/orders',
      }" v-if="user && user.role != 'FinalCostumer'" />


      <!-- 👉 Pages -->
      <VerticalNavSectionTitle :item="{
        heading: 'Pages',
      }" v-if="!user" />
      <VerticalNavLink :item="{
        title: 'Login',
        icon: 'bx-log-in',
        to: '/login',
      }" v-if="!user" />

      <VerticalNavLink :item="{
        title: 'Sensores',
        icon: 'mdi-thermometer-low',
        to: '/sensor-observation',
      }" v-if="!user" />
    </template>
    <slot />
  </VerticalNavLayout>
</template>

<style lang="scss" scoped>
.meta-key {
  border: thin solid rgba(var(--v-border-color), var(--v-border-opacity));
  border-radius: 6px;
  block-size: 1.5625rem;
  line-height: 1.3125rem;
  padding-block: 0.125rem;
  padding-inline: 0.25rem;
}
</style>
