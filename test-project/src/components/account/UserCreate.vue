<script lang="ts" setup>
import { Save } from 'lucide-vue-next'
// 💡 ใช้ IUserDTO แทน CreateUserPayload (ตาม Service Layer)
import type { IUserDTO as CreateUserPayload } from '@/types/user'
import { useUserStore } from '@/stores/user'
import Input from '@/components/common/BaseInput.vue'

// 💡 ใช้ useForm
import { useForm } from 'vee-validate'
import { onMounted, computed, watch } from 'vue'

const userStore = useUserStore()
const emit = defineEmits(['creation-success', 'close-drawer']);

// --- 1. Initial Values & Validation Rules ---

const initialValues: CreateUserPayload = {
  username: '',
  email: '',
  password: '',
  user_type: 'CUSTOMER',
  role_id: null as any, // 💡 ใช้ role_id
  status: 'ACTIVE',
  emp_id: null,
  cus_id: null,
}

// 💡 สร้าง Validation Schema
const rules = computed(() => ({
    username: 'required|min:3|max:100',
    email: 'required|email',
    password: 'required|min:6',
    user_type: 'required',
    role_id: 'required',
    emp_id: (value: string | null | undefined) => {
        if (user_type.value === 'EMPLOYEE' && !value) {
            return 'ກະລຸນາປ້ອນ ລະຫັດພະນັກງານ';
        }
        return true;
    },
    cus_id: (value: string | null | undefined) => {
        if (user_type.value === 'CUSTOMER' && !value) {
            return 'ກະລຸນາປ້ອນ ລະຫັດລູກຄ້າ';
        }
        return true;
    }
}));


// 💡 ใช้ resetForm จาก useForm
const { handleSubmit, defineField, resetForm } = useForm<CreateUserPayload>({
    initialValues: initialValues as any,
    validationSchema: rules,
    validateOnMount: false,
});

// Define Fields - ใช้ชื่อ Field ที่ถูกต้องตาม DTO (snake_case)
const [username, usernameAttrs] = defineField('username');
const [email, emailAttrs] = defineField('email');
const [password, passwordAttrs] = defineField('password');
const [user_type, userTypeAttrs] = defineField('user_type');
const [role_id, roleIdAttrs] = defineField('role_id');
const [emp_id, empIdAttrs] = defineField('emp_id');
const [cus_id, cusIdAttrs] = defineField('cus_id');

// --- 2. Computed & Initial Setup ---
const isEmployee = computed(() => user_type.value === 'EMPLOYEE');
const isCustomer = computed(() => user_type.value === 'CUSTOMER');

// 💡 Reset Logic เมื่อเปลี่ยน user_type (ใช้ resetForm ที่ได้จาก useForm)
watch(user_type, (newValue, oldValue) => {
    if (newValue === 'EMPLOYEE' && oldValue !== 'EMPLOYEE') {
        resetForm({
            values: {
                ...initialValues,
                user_type: 'EMPLOYEE',
                emp_id: '',
                cus_id: null,
                role_id: null as any
            },
            errors: { cus_id: undefined }
        });
    } else if (newValue === 'CUSTOMER' && oldValue !== 'CUSTOMER') {
         resetForm({
            values: {
                ...initialValues,
                user_type: 'CUSTOMER',
                emp_id: null,
                cus_id: '',
                role_id: null as any
            },
            errors: { emp_id: undefined }
        });
    }
});


// โหลด Roles เมื่อ Component ถูก Mount
onMounted(async () => {
    try {
      await userStore.fetchRoles();
    } catch (e) {
      console.error("Failed to fetch roles:", e);
    }
})

// --- 3. Submission Handler ---
const save = handleSubmit(async (values) => {
    // เตรียม Payload ให้เป็น DTO ที่ถูกต้อง
    const payload: any = {
        ...values,
        // ตรวจสอบและส่งค่า null/ID ตาม user_type
        cus_id: values.user_type === 'CUSTOMER' ? values.cus_id : null,
        emp_id: values.user_type === 'EMPLOYEE' ? values.emp_id : null,
    };

    try {
        await userStore.createUser(payload)
        alert('ສ້າງຜູ້ໃຊ້ງານໃໝ່ສຳເລັດແລ້ວ!')
        resetForm() // ใช้ resetForm() ที่ได้จาก useForm
        emit('creation-success');
    } catch (err: any) {
        alert(`Error: ${err.message || 'ບໍ່ສາມາດສ້າງຜູ້ໃຊ້ໄດ້'}`)
    }
})

// ฟังก์ชันปิด Drawer (ใช้ resetForm ที่ได้จาก useForm)
const closeDrawer = () => {
    resetForm(); // ใช้ resetForm()
    emit('close-drawer'); // ส่ง Event ให้ Parent ปิด Drawer
}
</script>

<template>
  <div class="drawer drawer-end z-50">
    <input id="create-user-drawer" type="checkbox" class="drawer-toggle" :checked="true" @change="!$event.target.checked && closeDrawer()"/>

    <div class="drawer-side">
      <label for="create-user-drawer" aria-label="close sidebar" class="drawer-overlay" @click="closeDrawer"></label>
      <div class="menu bg-white text-base-content min-h-full w-80 sm:w-96 p-6 shadow-xl">
        <h1 class="text-2xl font-bold border-b pb-2 mb-4 text-gray-800">ສ້າງຜູ້ໃຊ້ໃໝ່</h1>

        <form @submit.prevent="save" class="my-4 space-y-4">

          <h2 class="text-lg font-semibold border-b pb-1 text-gray-700">ຂໍ້ມູນບັນຊີເຂົ້າໃຊ້</h2>

          <Input name="username" type="text" label="Username" placeholder="John_Doe" class="input input-bordered w-full" v-model="username" v-bind="usernameAttrs" />
          <Input name="email" type="text" label="Email" placeholder="John@example.com" class="input input-bordered w-full" v-model="email" v-bind="emailAttrs" />
          <Input name="password" type="password" label="Password" placeholder="******" class="input input-bordered w-full" v-model="password" v-bind="passwordAttrs" />

          <h2 class="text-lg font-semibold border-b pb-1 text-gray-700 pt-4">ປະເພດຜູ້ໃຊ້</h2>
          <div class="form-control">
            <label class="label"><span class="label-text">ປະເພດບັນຊີ:</span></label>
            <select class="select select-bordered w-full" v-model="user_type" v-bind="userTypeAttrs">
              <option value="EMPLOYEE">Employee (ພະນັກງານ)</option>
              <option value="CUSTOMER">Customer (ລູກຄ້າ)</option>
            </select>
          </div>

          <h2 class="text-lg font-semibold border-b pb-1 text-gray-700 pt-4">ສິດທິ(Role)</h2>
          <div v-if="userStore.isLoadingRoles" class="loading loading-spinner"></div>
          <div v-else class="form-control">
            <label class="label"><span class="label-text">ເລືອກ Role ຫຼັກ:</span></label>
            <select class="select select-bordered w-full" v-model="role_id" v-bind="roleIdAttrs">
                <option :value="null" disabled>-- ກະລຸນາເລືອກ Role --</option>
                <option v-for="role in userStore.roleList" :key="role.role_id" :value="role.role_id">
                    {{ role.role_name }}
                </option>
            </select>
            <p class="text-error text-xs mt-1" v-if="roleIdAttrs['aria-errormessage']">{{ roleIdAttrs['aria-errormessage'] }}</p>
          </div>

          <h2 v-if="isEmployee || isCustomer" class="text-lg font-semibold border-b pb-1 text-gray-700 pt-4">
            ຂໍ້ມູນລາຍລະອຽດ {{ isEmployee ? 'ພະນັກງານ' : 'ລູກຄ້າ' }}
          </h2>

          <div v-if="isEmployee" class="space-y-4 border p-4 rounded-lg bg-gray-50">
            <Input
                name="emp_id"
                type="text"
                label="ລະຫັດພະນັກງານ *"
                placeholder="EMP001"
                class="input input-bordered w-full"
                v-model="emp_id"
                v-bind="empIdAttrs" />
          </div>

          <div v-else-if="isCustomer" class="space-y-4 border p-4 rounded-lg bg-gray-50">
            <Input
                name="cus_id"
                type="text"
                label="ລະຫັດລູກຄ້າ *"
                placeholder="CUS999"
                class="input input-bordered w-full"
                v-model="cus_id"
                v-bind="cusIdAttrs" />
          </div>

          <div class="mt-6 space-x-2 border-t pt-4 flex justify-end">
            <button type="submit" class="btn btn-primary text-white" :disabled="userStore.processing">
              <span v-if="userStore.processing" class="loading loading-spinner loading-xs"></span>
              <Save v-else :size="16" />ບັນທຶກ
            </button>
            <label for="create-user-drawer" class="btn btn-neutral" @click="closeDrawer">ຍົກເລິກ</label>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* สไตล์ Drawer */
.drawer-toggle:checked ~ .drawer-side {
  pointer-events: auto;
}
</style>
