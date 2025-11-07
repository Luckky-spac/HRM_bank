<script lang="ts" setup>
import { ref, defineProps, defineEmits, watch } from 'vue'
import type { Employee } from '@/types/employee';
import axios from 'axios'

const props = defineProps<{
  employee: Employee | null;
}>()

const emit = defineEmits(['updated', 'cancel'])

const form = ref({
  empId: "",
  prefix: "",
  firstname_la: "",
  lastname_la: "",
  firstname_en: "",
  lastname_en: "",
  gender: "",
  email: "",
  phonenumber: "",
})

const loading = ref(false)

watch(() => props.employee, (val) => {
  if (val) {
    form.value = { ...val }
  } else {
    resetForm()
  }
},
  { immediate: true }
)

const resetForm = () => {
  form.value = {
    empId: "",
    prefix: "",
    firstname_la: "",
    lastname_la: "",
    firstname_en: "",
    lastname_en: "",
    gender: "",
    email: "",
    phonenumber: "",
  }
}

const submitForm = async () => {
  loading.value = true
  try {
    console.log('update employee:', props.employee?.id, form.value)
    await axios.put(`http://localhost:3000/api/employee/${props.employee.id}`, form.value)
    emit("updated")
    resetForm()
  } catch (err) {
    console.error("Failed to update employee:", err)
  } finally {
    loading.value = false
  }
}


</script>

<template>

  <div>
    <h2 class="text-lg font-bold mb-4">ແກ້ໄຂຂໍ້ມູນພະນັກງານ</h2>
    <form @submit.prevent="submitForm" class="space-y-2">
      <input v-model="form.empId" placeholder="ລະຫັດພະນັກງານ" class="input input-bordered w-full" />
      <select v-model="form.prefix" class="input input-bordered w-full">
        <option value="">ເລືອກຄຳນຳ</option>
        <option value="Mr">ທ້າວ</option>
        <option value="Miss">ນາງ</option>
      </select>
      <input v-model="form.firstname_la" placeholder="ຊື່ແທ້" class="input input-bordered w-full" />
      <input v-model="form.lastname_la" placeholder="ນາມສະກຸນ" class="input input-bordered w-full" />
      <input v-model="form.firstname_en" placeholder="ຊື່ແທ້ເປັນພາສາອັງກິດ" class="input input-bordered w-full" />
      <input v-model="form.lastname_en" placeholder="ນາມສະກຸນພາສາອັງກິດ" class="input input-bordered w-full" />
      <select v-model="form.gender" class="input input-bordered w-full">
        <option value="">ເລືອກເພດ</option>
        <option value="Male">ເພດຊາຍ</option>
        <option value="Female">ເພດຍິງ</option>
        <option value="Other">ອື່ນໆ</option>
      </select>
      <input v-model="form.email" placeholder="Email" class="input input-bordered w-full" />
      <input v-model="form.phonenumber" placeholder="ເບີໂທລະສັບ" class="input input-bordered w-full" />

      <div class="flex justify-between mt-4">
        <button type="submit" class="btn btn-primary" :disabled="loading">
          {{ loading ? "ກຳລັງບັນທຶກ..." : "ບັນທຶກແລ້ວ" }}
        </button>
        <button @click="emit('cancel')" type="button" class="btn">ຍົກເລິກ</button>
      </div>
    </form>
  </div>

</template>
