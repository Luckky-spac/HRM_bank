<script lang="ts" setup>
import UserCreate from '@/components/account/UserCreate.vue'
import UserList from '@/components/account/UserList.vue'
import UserUpdate from '@/components/account/UserUpdate.vue'
import AppNavbar from '@/components/AppNavbar.vue'
import { useUserStore } from '@/stores/user'
import { Search, Plus } from 'lucide-vue-next' // 💡 Import Plus สำหรับปุ่ม
import { ref, watch, onMounted } from 'vue'

const userStore = useUserStore()
const searchQuery = ref('')

// --- Drawer/Modal State ---
const isCreateDrawerOpen = ref(false);
const isUpdateDrawerOpen = ref(false);
const selectedUserId = ref<number | null>(null);

// --- Lifecycle & Search Logic ---
onMounted(() => {
    // 💡 แก้ไข: Pinia Store Action ถูกเรียกใช้ใน UserList.vue แล้ว (แต่ก็ควรเรียกที่นี่ด้วย)
    userStore.getAllUsers();
});

watch(searchQuery, (newQuery) => {
    userStore.searchUsers(newQuery)
})

// --- Handlers ---
const openCreateDrawer = () => {
    isCreateDrawerOpen.value = true;
};

// 💡 เมื่อ User ถูกสร้าง/แก้ไขสำเร็จ: ปิด Drawer และ Refresh List
const handleUserSuccess = () => {
    isCreateDrawerOpen.value = false;
    isUpdateDrawerOpen.value = false;
    selectedUserId.value = null;
    userStore.getAllUsers();
};

const openUpdateDrawer = (id: number) => {
    selectedUserId.value = id;
    isUpdateDrawerOpen.value = true;
};

const closeDrawer = () => {
    isCreateDrawerOpen.value = false;
    isUpdateDrawerOpen.value = false;
    selectedUserId.value = null;
};
</script>

<template>
  <AppNavbar />

  <div class="p-4">
    <div class="flex justify-between items-center mb-4">
      <div>
        <label class="input input-bordered flex items-center gap-2">
          <Search />
          <input v-model="searchQuery" type="search" required placeholder="ຄົ້ນຫາຜູ້ໃຊ້ງານ" />
        </label>
      </div>
      <div class="flex items-center gap-2">
        <button @click="openCreateDrawer" class="btn btn-primary text-white">
            <span v-if="userStore.processing" class="loading loading-spinner loading-xs"></span>
            <Plus v-else :size="16" class="mr-1" /> ເພີ່ມຜູ້ໃຊ້ໃໝ່
        </button>
        <button @click="userStore.getAllUsers()" class="btn btn-neutral">
          <span v-if="userStore.loading" class="loading loading-spinner loading-xs"></span>
          Reload
        </button>
      </div>
    </div>

    <UserList @open-update="openUpdateDrawer" />
  </div>

  <div v-if="isCreateDrawerOpen">
    <UserCreate @creation-success="handleUserSuccess" @close-drawer="closeDrawer" />
  </div>

  <div v-if="isUpdateDrawerOpen && selectedUserId">
    <UserUpdate :user-id="selectedUserId" @update-success="handleUserSuccess" @close-drawer="closeDrawer" />
  </div>
</template>

<style scoped></style>
