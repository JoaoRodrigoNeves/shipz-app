<script setup>
import avatar1 from '@images/avatars/avatar-1.png'

const user_info = JSON.parse(sessionStorage.getItem('user_info'))

const logout = () => {
  sessionStorage.removeItem('token');
  sessionStorage.removeItem('user_info');
}
</script>

<template>
  <VBadge dot location="bottom right" offset-x="3" offset-y="3" color="success" bordered>
    <VAvatar class="cursor-pointer" color="primary" variant="tonal">
      <VImg :src="avatar1" />
      <VMenu activator="parent" width="230" location="bottom end" offset="14px">
        <VList>
          <VListItem>
            <template #prepend>
              <VListItemAction start>
                <VBadge dot location="bottom right" offset-x="3" offset-y="3" color="success">
                  <VAvatar color="primary" variant="tonal">
                    <VImg :src="avatar1" />
                  </VAvatar>
                </VBadge>
              </VListItemAction>
            </template>

            <VListItemTitle class="font-weight-semibold">
              {{ user_info.name }}
            </VListItemTitle>
            <VListItemSubtitle>{{ user_info.role }}</VListItemSubtitle>
          </VListItem>
          <VDivider class="my-2" />

          <!-- Divider -->
          <!-- 👉 Logout -->
          <VListItem to="/login" @click="logout">
            <template #prepend>
              <VIcon class="me-2" icon="bx-log-out" size="22" />
            </template>

            <VListItemTitle>Logout</VListItemTitle>
          </VListItem>
        </VList>
      </VMenu>
      <!-- !SECTION -->
    </VAvatar>
  </VBadge>
</template>
