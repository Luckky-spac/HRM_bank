<script setup lang="ts">

import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

import type { Employee } from '@/types/employee';
import AppFooter from '@/components/AppFooter.vue';
import AppNavbar from '@/components/AppNavbar.vue';
import EmpCreate from '@/components/employee/empCreate.vue';
import EmpEdit from '@/components/employee/empEdit.vue';

// state
const employees = ref<Employee[]>([])
const loading = ref(true)
const searchQuery = ref('')

const selectedEmployee = ref<Employee | null>(null)
const showEditDrawer = ref(false)



interface Employee {
  id: number
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

const openEdit = (emp: Employee) => {
  console.log('Open edit for:', emp)
  selectedEmployee.value = emp
  showEditDrawer.value = true
}

// call API
const fetchEmployee = async () => {
  loading.value = true
  try {
    const res = await axios.get("http://localhost:3000/api/employee")
    // console.log('Full res:', res)
    // console.log("Response from /employee:", res.data)

    if (Array.isArray(res.data.data)) {
      employees.value = res.data.data
    } else if (Array.isArray(res.data.data.employee)) {
      employees.value = res.data.data.employee
    } else {
      employees.value = []
    }

  } catch (error) {
    console.error('Failed to fetch employee:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchEmployee()
})

const filteredEmployees = computed(() => {
  if (!searchQuery.value) return employees.value;

  const query = searchQuery.value.toLowerCase().trim();
  return employees.value.filter(emp =>
    emp.empId.toLowerCase().includes(query) ||
    emp.firstname_la.toLowerCase().includes(query) ||
    emp.lastname_la.toLowerCase().includes(query) ||
    emp.firstname_en.toLowerCase().includes(query) ||
    emp.lastname_en.toLowerCase().includes(query) ||
    emp.gender.toLowerCase().startsWith(query) ||
    emp.email.toLowerCase().includes(query) ||
    emp.phonenumber.toLowerCase().includes(query)
  )
})

// Function to handle transaction deletion
const handleDelete = async (emp: Employee) => {
  if (!confirm('ທ່ານແນ່ໃຈທີຈະລົບ ຫຼື ບໍ່')) return;
  try {
    await axios.delete(`http://localhost:3000/api/employee/${emp.id}`)
    employees.value = employees.value.filter(e => e.id !== emp.id);
    alert('ທຳການລົບສຳເລັດ')
  } catch (err) {
    console.error('DeleteEmployee Failed:', err)
  }
}


</script>

<template>
  <AppNavbar />

  <div class="p-6">
    <!-- ส่วน Search + ปุ่มเพิ่ม -->
    <div class="flex justify-end items-center mb-6 gap-4">
      <!-- Search -->
      <div class="w-72">
        <label class="input input-bordered flex items-center gap-2">
          <Search class="w-5 h-5 text-gray-500" />
          <input v-model="searchQuery" type="search" placeholder="ຄົ້ນຫາພະນັກງານ..." class="grow" />
        </label>
      </div>

      <!-- Drawer -->
      <div class="drawer drawer-end z-40">
        <input id="create-drawer" type="checkbox" class="drawer-toggle" />
        <div class="drawer-content flex justify-end">
          <label for="create-drawer" class="btn btn-primary">ເພີ່ມພະນັກງານໃຫມ່</label>
        </div>
        <div class="drawer-side">
          <label for="create-drawer" aria-label="close sidebar" class="drawer-overlay"></label>
          <ul class="menu bg-base-200 text-base-content min-h-full w-96 p-4">
            <EmpCreate @employee-created="fetchEmployee" />
          </ul>
        </div>
      </div>
    </div>


    <h1 class="p-2 font-bold text-2xl mb-4">ລາຍຊື່ພະນັກງານ</h1>

    <!-- ตาราง -->
    <div class="card shadow-lg border border-gray-200">
      <div class="max-h-[70vh] overflow-y-auto">
        <table class="table table-auto w-full text-sm border-collapse">
          <thead class="bg-gray-100 sticky top-0 z-10 text-gray-700">
            <tr>
              <th class="text-center px-2 py-2">ລະຫັດ</th>
              <th class="text-center px-2 py-2">ຊື່</th>
              <th class="text-center px-2 py-2">ນາມສະກຸນ</th>
              <th class="text-center px-2 py-2">ເພດ</th>
              <th class="text-center px-2 py-2">Email</th>
              <th class="text-center px-2 py-2">ເບີໂທ</th>
              <th class="text-center px-2 py-2">ທີ່ຢູ່</th>
              <th class="text-center px-2 py-2">ສາຂາ</th>
              <th class="text-center px-2 py-2">ພະແນກ</th>
              <th class="text-center px-2 py-2">ຕຳແໜ່ງ</th>
              <th class="text-center px-2 py-2">ພາກສ່ວນ</th>
              <th class="text-center px-2 py-2">Actions</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="emp in filteredEmployees" :key="emp.id" class="hover:bg-gray-50 transition text-center">
              <td>{{ emp.empId }}</td>
              <td>{{ emp.firstname_la }}</td>
              <td>{{ emp.lastname_la }}</td>
              <td>{{ emp.gender }}</td>
              <td class="truncate max-w-[250px]">{{ emp.email }}</td>
              <td>{{ emp.phonenumber }}</td>
              <td>-</td>
              <td>-</td>
              <td>-</td>
              <td>-</td>
              <td>-</td>
              <td class="flex justify-center gap-1">
                <button @click="openEdit(emp)" class="btn btn-info btn-xs">ແກ້ໄຂ</button>
                <button @click="handleDelete(emp)" class="btn btn-error btn-xs">ລົບ</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>


    <div v-if="showEditDrawer" class="fixed inset-0 z-50 flex justify-end">
      <div @click="showEditDrawer = false" class="drawer-overlay absolute inset-0 bg-black/30"></div>
      <div class=" bg-white w-96 p-4 relative z-10">
        <EmpEdit v-if="selectedEmployee" :employee="selectedEmployee" @updated="fetchEmployee(); showEditDrawer = false"
          @cancel="showEditDrawer = false" />
      </div>
    </div>

  </div>

  <AppFooter />
</template>
