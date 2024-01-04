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
      <VerticalNavLink :item="{
        title: 'Dashboard',
        icon: 'bx-home',
        to: '/dashboard',
      }" />
      <VerticalNavLink :item="{
        title: 'Produtos',
        icon: 'bx-home',
        to: '/products-list',
      }" />
      <VerticalNavLink :item="{
        title: 'Catálogo',
        icon: 'bx-home',
        to: '/product-catalogs',
      }" />
      <VerticalNavLink :item="{
        title: 'Produtos',
        icon: 'bx-home',
        to: '/products',
      }" />
      <VerticalNavLink :item="{
        title: 'Embalagens de Produto',
        icon: 'bx-home',
        to: '/product-packages'
      }" />
      <VerticalNavLink :item="{
        title: 'Encomendas',
        icon: 'bi-box-seam',
        to: '/orders',
      }" />
      <VerticalNavLink :item="{
        title: 'Account Settings',
        icon: 'mdi-account-cog-outline',
        to: '/account-settings',
      }" />

      <!-- 👉 Pages -->
      <VerticalNavSectionTitle :item="{
        heading: 'Pages',
      }" />
      <VerticalNavLink :item="{
        title: 'Login',
        icon: 'bx-log-in',
        to: '/login',
      }" />

      <VerticalNavLink :item="{
        title: 'Error',
        icon: 'bx-info-circle',
        to: '/no-existence',
      }" />

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
