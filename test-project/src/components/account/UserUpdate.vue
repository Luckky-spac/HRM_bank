// src/components/account/UserUpdate.vue

<script lang="ts" setup>
import { Save, X } from 'lucide-vue-next'
import type { User, CreateUserPayload, EmployeeProfile, CustomerProfile } from '@/types/user'
import { useUserStore } from '@/stores/user'
import Input from '@/components/common/BaseInput.vue'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import { z } from 'zod'
import { computed, watch, defineEmits } from 'vue'

const props = defineProps<{
    userData: User;
    isOpen: boolean;
}>();

const emit = defineEmits(['close', 'updated']);
const userStore = useUserStore();

// --- 1. กำหนด Zod Schema สำหรับ UPDATE (Password Optional) ---

const EmployeeSchema = z.object({
    PREFIX: z.string().max(20).optional().or(z.literal('')),
    FIRSTNAME_LA: z.string().min(1, 'ຊື່ (ລາວ) ຕ້ອງບໍ່ຫວ່າງ').max(100),
    LASTNAME_LA: z.string().min(1, 'ນາມສະກຸນ (ລາວ) ຕ້ອງບໍ່ຫວ່າງ').max(100),
    PHONENUMBER: z.string().min(8, 'ເບີໂທລະສັບບໍ່ຖືກຕ້ອງ').max(20),
    GENDER: z.enum(['MALE', 'FEMALE', 'OTHER', '']).optional(),
});

const CustomerSchema = z.object({
    FIRSTNAME_LA: z.string().min(1, 'ຊື່ (ລາວ) ຕ້ອງບໍ່ຫວ່າງ').max(100),
    LASTNAME_LA: z.string().min(1, 'ນາມສະກຸນ (ລາວ) ຕ້ອງບໍ່ຫວ່າງ').max(100),
    NATIONAL_ID: z.string().min(5, 'ເລກປະຈຳຕົວຕ້ອງມີຢ່າງໜ້ອຍ 5 ຕົວ').max(50),
    BIRTHDAY: z.string().optional(),
    GENDER: z.enum(['MALE', 'FEMALE', 'OTHER', '']).optional(),
    NATIONALITY: z.string().max(50).optional(),
    JOB: z.string().max(100).optional(),
});

const baseUpdateUserSchema = z.object({
    username: z.string().max(100).min(3),
    email: z.string().email(),
    password: z.string().min(6, 'Password ຕ້ອງມີຢ່າງໜ້ອຍ 6 ຕົວ').optional().or(z.literal('')),
    userType: z.enum(['BACKEND_STAFF', 'EMPLOYEE', 'CUSTOMER']),
    roleIds: z.array(z.number()).min(1, 'ກະລຸນາເລືອກຢ່າງໜ້ອຍ 1 Role'),
    employeeDetails: EmployeeSchema.partial().nullable(),
    customerDetails: CustomerSchema.partial().nullable(),
});

const refinedUpdateUserSchema = baseUpdateUserSchema.superRefine((data, ctx) => {
    const typedData = data as CreateUserPayload;

    if (typedData.userType === 'EMPLOYEE' && (!typedData.employeeDetails?.FIRSTNAME_LA || !typedData.employeeDetails?.LASTNAME_LA || !typedData.employeeDetails?.PHONENUMBER)) {
        ctx.addIssue({ code: z.ZodIssueCode.custom, message: 'ກະລຸນາປ້ອນ ຊື່, ນາມສະກຸນ, ແລະ ເບີໂທລະສັບ ພະນັກງານ', path: ['employeeDetails'] });
    }
    if (typedData.userType === 'CUSTOMER' && (!typedData.customerDetails?.FIRSTNAME_LA || !typedData.customerDetails?.LASTNAME_LA || !typedData.customerDetails?.NATIONAL_ID)) {
        ctx.addIssue({ code: z.ZodIssueCode.custom, message: 'ກະລຸນາປ້ອນ ຊື່, ນາມສະກຸນ, ແລະ ເລກປະຈຳຕົວ ລູກຄ້າ', path: ['customerDetails'] });
    }
});

const updateUserSchema = toTypedSchema(refinedUpdateUserSchema);

// --- 2. Initial Values & Form Setup ---
const initialValues = computed<CreateUserPayload>(() => {
    const user = props.userData;
    const isEmp = !!user.EMP_ID;
    const isCus = !!user.CUS_ID;

    let employeeDetails: Partial<EmployeeProfile> | null = null;
    let customerDetails: Partial<CustomerProfile> | null = null;

    if (isEmp && user.profile) {
        employeeDetails = user.profile as EmployeeProfile;
    } else if (isCus && user.profile) {
        customerDetails = user.profile as CustomerProfile;
    }

    return {
        username: user.username,
        email: user.email,
        password: '',
        userType: isEmp ? 'EMPLOYEE' : isCus ? 'CUSTOMER' : 'BACKEND_STAFF',
        roleIds: user.roles.map(r => r.ROLE_ID),
        employeeDetails: employeeDetails as any,
        customerDetails: customerDetails as any,
    }
});

// ลบ setFieldValue, values, errors ออกเพื่อแก้ Unused Vars
const { handleSubmit, defineField, resetForm } = useForm<CreateUserPayload>({
    validationSchema: updateUserSchema,
    initialValues: initialValues.value as any,
});

// Watch userData change to reset form when user is selected/changed
watch(initialValues, (newValues) => {
    resetForm({ values: newValues as any });
}, { deep: true });

// Define Fields (เฉพาะตัวที่ใช้ใน Template)
const [username, usernameAttrs] = defineField('username');
const [email, emailAttrs] = defineField('email');
const [password, passwordAttrs] = defineField('password');
const [userType, userTypeAttrs] = defineField('userType');
const [roleIds] = defineField('roleIds'); // ไม่ใช้ roleIdsAttrs

// Employee Fields
const [empFirstNameLA, empFirstNameLAAttrs] = defineField('employeeDetails.FIRSTNAME_LA');
const [empLastNameLA, empLastNameLAAttrs] = defineField('employeeDetails.LASTNAME_LA');
const [empPhone, empPhoneAttrs] = defineField('employeeDetails.PHONENUMBER');
const [empPrefix, empPrefixAttrs] = defineField('employeeDetails.PREFIX');
const [empGender, empGenderAttrs] = defineField('employeeDetails.GENDER');

// Customer Fields
const [cusFirstNameLA, cusFirstNameLAAttrs] = defineField('customerDetails.FIRSTNAME_LA');
const [cusLastNameLA, cusLastNameLAAttrs] = defineField('customerDetails.LASTNAME_LA');
const [cusNationalID, cusNationalIDAttrs] = defineField('customerDetails.NATIONAL_ID');
const [cusBirthday, cusBirthdayAttrs] = defineField('customerDetails.BIRTHDAY');
const [cusGender, cusGenderAttrs] = defineField('customerDetails.GENDER');
const [cusNationality, cusNationalityAttrs] = defineField('customerDetails.NATIONALITY');
const [cusJob, cusJobAttrs] = defineField('customerDetails.JOB');

// Computed เพื่อใช้ใน Template
const isEmployee = computed(() => userType.value === 'EMPLOYEE');
const isCustomer = computed(() => userType.value === 'CUSTOMER');

// --- เพิ่ม: Drawer Control Function ---
const closeDrawer = () => {
    emit('close');
};


// --- 3. Submission Handler ---
const update = handleSubmit(async (values) => { // ใช้ values ที่ส่งมาจาก handleSubmit (ไม่เป็น Unused Var)
    // 1. ปรับปรุง Payload ก่อนส่ง (ใช้ User ID)
    const payload: CreateUserPayload & { USER_ID: number, profile?: any } = {
        USER_ID: props.userData.USER_ID,
        username: values.username,
        email: values.email,
        password: values.password || undefined,
        userType: values.userType,
        roleIds: values.roleIds,
        employeeDetails: null, // ไม่ส่งไป
        customerDetails: null, // ไม่ส่งไป
    };

    if (values.userType === 'EMPLOYEE') {
        payload.profile = {
            ...(values.employeeDetails as any),
            EMP_ID: props.userData.EMP_ID
        } as any;
    } else if (values.userType === 'CUSTOMER') {
        payload.profile = {
            ...(values.customerDetails as any),
            CUS_ID: props.userData.CUS_ID
        } as any;
    }

    try {
        await userStore.updateUser(payload as any)
        alert('ແກ້ໄຂຜູ້ໃຊ້ງານສຳເລັດແລ້ວ!')
        emit('updated');
        closeDrawer();
    } catch (err: any) {
        alert(`Error: ${err.message || 'ບໍ່ສາມາດແກ້ໄຂຜູ້ໃຊ້ໄດ້'}`)
    }
});
</script>

<template>
  <div :class="['drawer drawer-end z-50', { 'drawer-open': props.isOpen }]">
    <input id="update-user-drawer" type="checkbox" class="drawer-toggle" :checked="props.isOpen" @change="closeDrawer" />

    <div class="drawer-content">
      </div>

    <div class="drawer-side">
      <label for="update-user-drawer" aria-label="close sidebar" class="drawer-overlay" @click="closeDrawer"></label>
      <div class="menu bg-white text-base-content min-h-full w-80 sm:w-96 p-6 shadow-xl">
        <div class="flex justify-between items-center border-b pb-2 mb-4">
            <h1 class="text-2xl font-bold text-gray-800">ແກ້ໄຂຜູ້ໃຊ້: {{ props.userData.username }}</h1>
            <button @click="closeDrawer" class="btn btn-ghost btn-sm btn-circle"><X :size="20" /></button>
        </div>

        <form @submit.prevent="update" class="my-4 space-y-4 overflow-y-auto max-h-[85vh] pr-2">

          <h2 class="text-lg font-semibold border-b pb-1 text-gray-700">ຂໍ້ມູນບັນຊີເຂົ້າໃຊ້</h2>

          <Input name="username" type="text" label="Username" class="input input-bordered w-full" v-model="username" v-bind="usernameAttrs" />
          <Input name="email" type="text" label="Email" class="input input-bordered w-full" v-model="email" v-bind="emailAttrs" />
          <Input name="password" type="password" label="Password (ປ່ອຍຫວ່າງຖ້າບໍ່ຕ້ອງການປ່ຽນ)" class="input input-bordered w-full" v-model="password" v-bind="passwordAttrs" />

          <h2 class="text-lg font-semibold border-b pb-1 text-gray-700 pt-4">ປະເພດຜູ້ໃຊ້ ແລະ ສິດທິ</h2>

          <div class="form-control">
            <label class="label"><span class="label-text">ປະເພດບັນຊີ:</span></label>
            <select class="select select-bordered w-full" v-model="userType" v-bind="userTypeAttrs">
              <option value="BACKEND_STAFF">Backend User (Admin/Staff)</option>
              <option value="EMPLOYEE">Employee (ພະນັກງານ)</option>
              <option value="CUSTOMER">Customer (ລູກຄ້າ)</option>
            </select>
          </div>

          <div class="flex flex-wrap gap-2 p-3 border rounded-lg bg-gray-50">
            <div v-for="role in userStore.roleList" :key="role.ROLE_ID" class="form-control">
              <label class="label cursor-pointer gap-2">
                <input type="checkbox" :value="role.ROLE_ID" class="checkbox checkbox-primary checkbox-sm"
                  @change="() => {
                    const id = role.ROLE_ID as number;
                    const currentRoles = Array.isArray(roleIds.value) ? roleIds.value : [];
                    const index = currentRoles.findIndex(r => r === id);
                    if (index === -1) {
                      roleIds.value = [...currentRoles, id]; // ใช้ roleIds.value
                    } else {
                      roleIds.value = currentRoles.filter((r: number) => r !== id); // ใช้ roleIds.value
                    }
                  }"
                  :checked="(roleIds.value as number[]).includes(role.ROLE_ID as number)"
                />
                <span class="label-text text-sm font-semibold">{{ role.ROLE_NAME }}</span>
              </label>
            </div>
            </div>

          <h2 v-if="isEmployee || isCustomer" class="text-lg font-semibold border-b pb-1 text-gray-700 pt-4">
            ຂໍ້ມູນລາຍລະອຽດ {{ isEmployee ? 'ພະນັກງານ' : 'ລູກຄ້າ' }}
          </h2>

          <div v-if="isEmployee" class="space-y-4 border p-4 rounded-lg bg-gray-50">
            <div class="grid grid-cols-2 gap-4">
                <Input name="employeeDetails.PREFIX" type="text" label="ຄຳນຳໜ້າ" class="input input-bordered w-full" v-model="empPrefix" v-bind="empPrefixAttrs" />
                <Input name="employeeDetails.GENDER" type="select" label="ເພດ" class="select select-bordered w-full" v-model="empGender" v-bind="empGenderAttrs" :options="['MALE', 'FEMALE', 'OTHER']" />
            </div>
            <Input name="employeeDetails.FIRSTNAME_LA" type="text" label="ຊື່ (ລາວ) *" class="input input-bordered w-full" v-model="empFirstNameLA" v-bind="empFirstNameLAAttrs" />
            <Input name="employeeDetails.LASTNAME_LA" type="text" label="ນາມສະກຸນ (ລາວ) *" class="input input-bordered w-full" v-model="empLastNameLA" v-bind="empLastNameLAAttrs" />
            <Input name="employeeDetails.PHONENUMBER" type="text" label="ເບີໂທລະສັບ *" class="input input-bordered w-full" v-model="empPhone" v-bind="empPhoneAttrs" />

            </div>

          <div v-else-if="isCustomer" class="space-y-4 border p-4 rounded-lg bg-gray-50">
            <Input name="customerDetails.FIRSTNAME_LA" type="text" label="ຊື່ (ລາວ) *" class="input input-bordered w-full" v-model="cusFirstNameLA" v-bind="cusFirstNameLAAttrs" />
            <Input name="customerDetails.LASTNAME_LA" type="text" label="ນາມສະກຸນ (ລາວ) *" class="input input-bordered w-full" v-model="cusLastNameLA" v-bind="cusLastNameLAAttrs" />
            <Input name="customerDetails.NATIONAL_ID" type="text" label="ເລກປະຈຳຕົວ *" placeholder="012345678" class="input input-bordered w-full" v-model="cusNationalID" v-bind="cusNationalIDAttrs" />

            <div class="grid grid-cols-2 gap-4">
                <Input name="customerDetails.GENDER" type="select" label="ເພດ" class="select select-bordered w-full" v-model="cusGender" v-bind="cusGenderAttrs" :options="['MALE', 'FEMALE', 'OTHER']" />
                <Input name="customerDetails.BIRTHDAY" type="date" label="ວັນເດືອນປີເກີດ" class="input input-bordered w-full" v-model="cusBirthday" v-bind="cusBirthdayAttrs" />
            </div>

            <Input name="customerDetails.NATIONALITY" type="text" label="ສັນຊາດ" class="input input-bordered w-full" v-model="cusNationality" v-bind="cusNationalityAttrs" />
            <Input name="customerDetails.JOB" type="text" label="ອາຊີບ" class="input input-bordered w-full" v-model="cusJob" v-bind="cusJobAttrs" />
          </div>

          <div class="sticky bottom-0 bg-white pt-4 pb-2 border-t mt-6 space-x-2 flex justify-end">
            <button class="btn btn-primary text-white" :disabled="userStore.processing">
              <span v-if="userStore.processing" class="loading loading-spinner loading-xs"></span>
              <Save v-else :size="16" /> ບັນທຶກການແກ້ໄຂ
            </button>
            <button type="button" @click="closeDrawer" class="btn btn-neutral">ຍົກເລິກ</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* You can add specific styles here if needed */
</style>
