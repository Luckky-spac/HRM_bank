<script lang="ts" setup>
import { ref, defineEmit } from 'vue'
import axios from 'axios';

interface EmployeeForm {
  empId: string
  prefix: string
  firstname_la: string
  lastname_la: string
  firstname_en: string
  lastname_en: string
  gender: string
  email: string
  phonenumber: string
}

const emit = defineEmits<{
  (e: 'employee-created'): void
}>()

const form = ref<EmployeeForm>({
  empId: '',
  prefix: '',
  firstname_la: '',
  lastname_la: '',
  firstname_en: '',
  lastname_en: '',
  gender: '',
  email: '',
  phonenumber: ''
})

const loading = ref(false)
const error = ref('')

const submitForm = async () => {
  loading.value = true
  error.value = ''
  try {
    await axios.post('http://localhost:3000/api/employee', form.value)
    emit('employee-created')
    // reset form
    form.value = {
      empId: '',
      prefix: '',
      firstname_la: '',
      lastname_la: '',
      firstname_en: '',
      lastname_en: '',
      gender: '',
      email: '',
      phonenumber: ''
    }
  } catch (err) {
    console.error('Failed to create employee:', err)
    error.value = err.response?.data?.message || 'Failed to create employee'
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  form.value = {
    empId: '',
    prefix: '',
    firstname_la: '',
    lastname_la: '',
    firstname_en: '',
    lastname_en: '',
    gender: '',
    email: '',
    phonenumber: ''
  }
}

</script>

<template>

  <div>
    <h2 class="text-xl font-bold mb-4">ເພີ່ມພະນັກງານໃໝ່</h2>

    <form @submit.prevent="submitForm" class="space-y-2">
      <input v-model="form.empId" placeholder="ລະຫັດພະນັກງານ" class="input input-bordered w-full" required />
      <select v-model="form.prefix" class="input input-bordered w-full" required>
        <option value="">ເລືອກຄຳນຳໜ້າ</option>
        <option value="Mr">ທ້າວ</option>
        <option value="Miss">ນາງ</option>
      </select>
      <input v-model="form.firstname_la" placeholder="ຊື່ແທ້" class="input input-bordered w-full" required />
      <input v-model="form.lastname_la" placeholder="ນາມສະກຸນ" class="input input-bordered w-full" required />
      <input v-model="form.firstname_en" placeholder="ຊື່ແທ້ເປັນພາສາອັງກິດ" class="input input-bordered w-full" />
      <input v-model="form.lastname_en" placeholder="ນາມສະກຸນເປັນພາສາອັງກິດ" class="input input-bordered w-full" />
      <select v-model="form.gender" class="input input-bordered w-full" required>
        <option value="">ເລືອກເພດ</option>
        <option value="Male">ເພດຊາຍ</option>
        <option value="Female">ເພດຍິງ</option>
      </select>
      <input v-model="form.email" type="email" placeholder="Email" class="input input-bordered w-full" />
      <input v-model="form.phonenumber" placeholder="ເບີໂທລະສັບ" class="input input-bordered w-full" required />

      <div class="flex justify-between items-center">
        <button type="submit" class="btn btn-primary" :disabled="loading">
          <span v-if="loading" class="ladong loading-spinner loading-xs"></span>
          <span v-else>ບັນທຶກ</span>
        </button>

        <button @click="resetForm" type="button" class="btn btn-error">Cancel</button>
      </div>
    </form>

  </div>

</template>
