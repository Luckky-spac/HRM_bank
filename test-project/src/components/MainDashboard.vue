<script lang="ts" setup>
import axios from 'axios';
import { ref, onMounted } from 'vue'
import { useUserStore, type UserStore } from '@/stores/user';
import { RouterLink } from 'vue-router';

const userStore: UserStore = useUserStore()
const totalUsers = ref(0)
const totalEmployees = ref(0)


onMounted(async () => {
  await userStore.getAllUsers(1, 1000)
  totalUsers.value = userStore.userList.length

  const res = await axios.get('http://localhost:3000/api/employee?page=1&limit=1000')
  totalEmployees.value = res.data.meta?.total ?? 0
})

const options = {
  chart: {
    id: 'vuechart-example'
  },
  xaxis: {
    categories: [2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025]
  }
}
const series = [{
  name: 'series-1',
  data: [30, 40, 45, 50, 49, 60, 70, 91]
}]


</script>

<template>

  <h1 class="text-4xl mb-4 font-bold">Dashboard</h1>
  <div class="flex mb-4">
    <div class="stats w-full shadow">

      <div class="stat">
        <div class="stat-figure text-secondary">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640" class="w-10">
            <path
              d="M320 312C253.7 312 200 258.3 200 192C200 125.7 253.7 72 320 72C386.3 72 440 125.7 440 192C440 258.3 386.3 312 320 312zM289.5 368L350.5 368C360.2 368 368 375.8 368 385.5C368 389.7 366.5 393.7 363.8 396.9L336.4 428.9L367.4 544L368 544L402.6 405.5C404.8 396.8 413.7 391.5 422.1 394.7C484 418.3 528 478.3 528 548.5C528 563.6 515.7 575.9 500.6 575.9L139.4 576C124.3 576 112 563.7 112 548.6C112 478.4 156 418.4 217.9 394.8C226.3 391.6 235.2 396.9 237.4 405.6L272 544.1L272.6 544.1L303.6 429L276.2 397C273.5 393.8 272 389.8 272 385.6C272 375.9 279.8 368.1 289.5 368.1z" />
          </svg>
        </div>
        <RouterLink :to="{ name: 'employee' }" class="stat-title text-blue-600">Employee</RouterLink>
        <div class="stat-value">{{ totalEmployees }}</div>
      </div>

      <div class="stat">
        <div class="stat-figure text-secondary">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640" class="w-10">
            <path
              d="M566.6 214.6L470.6 310.6C461.4 319.8 447.7 322.5 435.7 317.5C423.7 312.5 416 300.9 416 288L416 224L96 224C78.3 224 64 209.7 64 192C64 174.3 78.3 160 96 160L416 160L416 96C416 83.1 423.8 71.4 435.8 66.4C447.8 61.4 461.5 64.2 470.7 73.3L566.7 169.3C579.2 181.8 579.2 202.1 566.7 214.6zM169.3 566.6L73.3 470.6C60.8 458.1 60.8 437.8 73.3 425.3L169.3 329.3C178.5 320.1 192.2 317.4 204.2 322.4C216.2 327.4 224 339.1 224 352L224 416L544 416C561.7 416 576 430.3 576 448C576 465.7 561.7 480 544 480L224 480L224 544C224 556.9 216.2 568.6 204.2 573.6C192.2 578.6 178.5 575.8 169.3 566.7z" />
          </svg>
        </div>
        <div class="stat-title text-black font-bold">Transaction</div>
        <RouterLink :to="{ name: 'transaction' }" class="stat-value  text-blue-500">MORE DETAIL</RouterLink>
      </div>

      <div class="stat">
        <div class="stat-figure text-secondary">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640" class="w-10">
            <path
              d="M320 80C377.4 80 424 126.6 424 184C424 241.4 377.4 288 320 288C262.6 288 216 241.4 216 184C216 126.6 262.6 80 320 80zM96 152C135.8 152 168 184.2 168 224C168 263.8 135.8 296 96 296C56.2 296 24 263.8 24 224C24 184.2 56.2 152 96 152zM0 480C0 409.3 57.3 352 128 352C140.8 352 153.2 353.9 164.9 357.4C132 394.2 112 442.8 112 496L112 512C112 523.4 114.4 534.2 118.7 544L32 544C14.3 544 0 529.7 0 512L0 480zM521.3 544C525.6 534.2 528 523.4 528 512L528 496C528 442.8 508 394.2 475.1 357.4C486.8 353.9 499.2 352 512 352C582.7 352 640 409.3 640 480L640 512C640 529.7 625.7 544 608 544L521.3 544zM472 224C472 184.2 504.2 152 544 152C583.8 152 616 184.2 616 224C616 263.8 583.8 296 544 296C504.2 296 472 263.8 472 224zM160 496C160 407.6 231.6 336 320 336C408.4 336 480 407.6 480 496L480 512C480 529.7 465.7 544 448 544L192 544C174.3 544 160 529.7 160 512L160 496z" />
          </svg>
        </div>
        <RouterLink :to="{ name: 'UserList' }" class="stat-title text-blue-600">Accounts</RouterLink>
        <div class="stat-value">{{ totalUsers }}</div>
      </div>

    </div>
  </div>

  <div>
    <apexchart width="500" type="bar" :options="options" :series="series"></apexchart>
  </div>

</template>
