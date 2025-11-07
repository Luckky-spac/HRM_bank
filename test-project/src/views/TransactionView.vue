<script setup lang="ts">

import { ref, onMounted, computed } from "vue"
import { useTransactionStore } from "@/stores/transaction"
import AppNavbar from "@/components/AppNavbar.vue"
import type { Transaction } from "@/types/transaction"
import TransCreate from "@/components/transaction/TransCreate.vue"
import TransEdit from "@/components/transaction/TransEdit.vue"
import AppFooter from "@/components/AppFooter.vue"

const transactionStore = useTransactionStore()
const createDrawerInput = ref<HTMLInputElement | null>(null)
const editingTxn = ref<Transaction | null>(null)

// Search state
const searchQuery = ref('')

// --- Pagination State & Logic ---
const currentPage = ref(1)
const perPage = 10 // ກໍານົດ 10 records ຕໍ່ໜ້າ

const filteredTransactionList = computed<Transaction[]>(() => {
  if (!searchQuery.value) {
    return transactionStore.transactionList
  }

  const query = searchQuery.value.toLowerCase()

  return transactionStore.transactionList.filter(txn => {
    // ກວດສອບຊື່ບັນຊີຕົ້ນທາງ ຫຼື ປາຍທາງ ຫຼື ID
    const isMatch = (
      txn.source_acct_name?.toLowerCase().includes(query) ||
      txn.dest_acct_name?.toLowerCase().includes(query) ||
      txn.txn_id?.toString().includes(query) // ຄົ້ນຫາດ້ວຍ ID ກໍໄດ້
    )
    return isMatch
  })
})

const totalPages = computed(() => {
  return Math.ceil(filteredTransactionList.value.length / perPage)
})

const paginatedTransactionList = computed<Transaction[]>(() => {
  const start = (currentPage.value - 1) * perPage
  const end = start + perPage
  return filteredTransactionList.value.slice(start, end)
})

// Method pagination
const goToPage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}
// --- End Pagination Logic ---


onMounted(async () => {
  await transactionStore.getTransactionWithRisk()
  console.log('transaction =', transactionStore.transactionList)
})

const isEditDrawerOpen = computed<boolean>({
  get() {
    return editingTxn.value !== null;
  },
  set(val: boolean) {
    if (!val) {
      editingTxn.value = null;
    }
  }
})

const handleTransactionCreate = async () => {
  if (createDrawerInput.value) {
    createDrawerInput.value.checked = false
  }
  // ຫຼັງຈາກເພີ່ມແລ້ວ, ໃຫ້ກັບໄປໜ້າທໍາອິດເພື່ອສະແດງຂໍ້ມູນໃໝ່
  currentPage.value = 1;
  await transactionStore.getTransactionWithRisk();
}

const editTransaction = (txn: Transaction) => {
  console.log('Edit clicked', txn)
  editingTxn.value = { ...txn }
  console.log('Edit transaction State:', editingTxn.value)
}

const handleCloseEditModal = async (value: Transaction | null) => {
  editingTxn.value = value;
  if (value === null) {
    console.log('Edit completed. Starting data reload...')
    await transactionStore.getTransactionWithRisk();
    console.log('Data reload completed. Displaying updated list.')
  }
}

const handleSearch = (query: string) => {
  searchQuery.value = query
  currentPage.value = 1
  console.log('Search query:', query)
}

const handleDelete = async (txnId: number) => {
  if (confirm('ທ່ານແນ່ໃຈບໍວ່າຕ້ອງການລຶບທຸລະກຳນີ້?')) {
    try {
      await transactionStore.deleteTransaction(txnId.toString())
      alert('ລຶບທຸລະກຳສຳເລັດແລ້ວ!')
      // ຫຼັງຈາກລຶບແລ້ວ, ໃຫ້ກັບໄປໜ້າທໍາອິດ
      currentPage.value = 1;
      await transactionStore.getTransactionWithRisk()
    } catch (err) {
      console.error('Failed to delete transaction:', err)
      alert('ລຶບທຸລະກຳບໍ່ສຳເລັດ.')
    }
  }
}

</script>

<template>
  <div class="min-h-screen bg-white text-gray-800">
    <AppNavbar />

    <div class="p-4 md:p-8 lg:p-10 max-w-7xl mx-auto">
      <header class="mb-8">
        <h1 class="text-3xl font-extrabold mb-6 flex items-center text-gray-900">
          <svg class="w-8 h-8 mr-3 fill-current text-black" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
            <path
              d="M160 64C124.7 64 96 92.7 96 128L96 512C96 547.3 124.7 576 160 576L448 576C483.3 576 512 547.3 512 512L512 128C512 92.7 483.3 64 448 64L160 64zM272 352L336 352C380.2 352 416 387.8 416 432C416 440.8 408.8 448 400 448L208 448C199.2 448 192 440.8 192 432C192 387.8 227.8 352 272 352zM248 256C248 225.1 273.1 200 304 200C334.9 200 360 225.1 360 256C360 286.9 334.9 312 304 312C273.1 312 248 286.9 248 256zM576 144C576 135.2 568.8 128 560 128C551.2 128 544 135.2 544 144L544 208C544 216.8 551.2 224 560 224C568.8 224 576 216.8 576 208L576 144zM576 272C576 263.2 568.8 256 560 256C551.2 256 544 263.2 544 272L544 336C544 344.8 551.2 352 560 352C568.8 352 576 344.8 576 336L576 272zM560 384C551.2 384 544 391.2 544 400L544 464C544 472.8 551.2 480 560 480C568.8 480 576 472.8 576 464L576 400C576 391.2 568.8 384 560 384z" />
          </svg>
          ລາຍຊື່ທຸລະກຳ
        </h1>

        <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
          <div class="flex-grow max-w-full md:max-w-md">
            <label class="input input-bordered flex items-center gap-2 w-full bg-white shadow-md">
              <svg class="w-5 h-5 opacity-70" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                <path
                  d="M480 272C480 317.9 465.1 360.3 440 394.7L566.6 521.4C579.1 533.9 579.1 554.2 566.6 566.7C554.1 579.2 533.8 579.2 521.3 566.7L394.7 440C360.3 465.1 317.9 480 272 480C157.1 480 64 386.9 64 272C64 157.1 157.1 64 272 64C386.9 64 480 157.1 480 272zM272 416C351.5 416 416 351.5 416 272C416 192.5 351.5 128 272 128C192.5 128 128 192.5 128 272C128 351.5 192.5 416 272 416z" />
              </svg>
              <input v-model="searchQuery" type="text" class="grow" placeholder="ຄົ້ນຫາທຸລະກຳ..." />
            </label>
          </div>

          <div class="flex items-center gap-3 w-full md:w-auto">
            <button @click="transactionStore.getTransactionWithRisk()" :disabled="transactionStore.loading"
              class="btn btn-ghost text-gray-700 hover:bg-gray-100">
              <span v-if="transactionStore.loading" class="loading loading-spinner loading-xs"></span>
              <svg v-else class="w-5 h-5 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                <path
                  d="M544.1 256L552 256C565.3 256 576 245.3 576 232L576 88C576 78.3 570.2 69.5 561.2 65.8C552.2 62.1 541.9 64.2 535 71L483.3 122.8C439 86.1 382 64 320 64C191 64 84.3 159.4 66.6 283.5C64.1 301 76.2 317.2 93.7 319.7C111.2 322.2 127.4 310 129.9 292.6C143.2 199.5 223.3 128 320 128C364.4 128 405.2 143 437.7 168.3L391 215C384.1 221.9 382.1 232.2 385.8 241.2C389.5 250.2 398.3 256 408 256L544.1 256zM573.5 356.5C576 339 563.8 322.8 546.4 320.3C529 317.8 512.7 330 510.2 347.4C496.9 440.4 416.8 511.9 320.1 511.9C275.7 511.9 234.9 496.9 202.4 471.6L249 425C255.9 418.1 257.9 407.8 254.2 398.8C250.5 389.8 241.7 384 232 384L88 384C74.7 384 64 394.7 64 408L64 552C64 561.7 69.8 570.5 78.8 574.2C87.8 577.9 98.1 575.8 105 569L156.8 517.2C201 553.9 258 576 320 576C449 576 555.7 480.6 573.4 356.5z" />
              </svg>
              <span>ໂຫຼດຂໍ້ມູນ</span>
            </button>

            <div class="drawer drawer-end z-40">
              <input ref="createDrawerInput" id="create-drawer" type="checkbox" class="drawer-toggle" />
              <div class="drawer-content">
                <label for="create-drawer" class="btn btn-primary text-white shadow-lg">
                  <svg class="w-5 h-5 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path
                      d="M136 192C136 125.7 189.7 72 256 72C322.3 72 376 125.7 376 192C376 258.3 322.3 312 256 312C189.7 312 136 258.3 136 192zM48 546.3C48 447.8 127.8 368 226.3 368L285.7 368C384.2 368 464 447.8 464 546.3C464 562.7 450.7 576 434.3 576L77.7 576C61.3 576 48 562.7 48 546.3zM544 160C557.3 160 568 170.7 568 184L568 232L616 232C629.3 232 640 242.7 640 256C640 269.3 629.3 280 616 280L568 280L568 328C568 341.3 557.3 352 544 352C530.7 352 520 341.3 520 328L520 280L472 280C458.7 280 448 269.3 448 256C448 242.7 458.7 232 472 232L520 232L520 184C520 170.7 530.7 160 544 160z" />
                  </svg>
                  <span>ເພີ່ມທຸລະກຳໃຫມ່</span>
                </label>
              </div>

              <div class="drawer-side">
                <label for="create-drawer" class="drawer-overlay"></label>
                <div class="menu bg-white min-h-full w-80 sm:w-96 p-6 shadow-xl">
                  <h2 class="text-2xl font-bold mb-4 text-gray-800 border-b pb-2">ເພີ່ມທຸລະກຳ</h2>
                  <TransCreate @transaction-created="handleTransactionCreate" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </header>
      <div class="relative min-h-[300px]">

        <div v-if="transactionStore.loading"
          class="absolute inset-0 flex items-center justify-center bg-white bg-opacity-80 z-20 transition-opacity duration-300 rounded-2xl shadow-xl">
          <div class="text-center text-blue-600">
            <span class="loading loading-spinner loading-lg text-blue-600"></span>
            <p class="mt-4 text-lg font-semibold">ກຳລັງໂຫຼດຂໍ້ມູນທຸລະກຳ...</p>
          </div>
        </div>

        <div :class="{ 'opacity-50 pointer-events-none': transactionStore.loading }">

          <div v-if="transactionStore.error" class="card bg-red-500 text-white shadow-lg my-8">
            <div class="card-body">
              <h2 class="card-title">ເກີດຂໍ້ຜິດພາດໃນການໂຫຼດຂໍ້ມູນ!</h2>
              <p>Error: {{ transactionStore.error }}</p>
              <div class="card-actions justify-end">
                <button @click="transactionStore.getTransactionWithRisk()" class="btn btn-sm bg-white text-red-500 hover:bg-gray-100">
                  ລອງໃໝ່ອີກຄັ້ງ
                </button>
              </div>
            </div>
          </div>
          <div v-else-if="transactionStore.transactionList.length === 0"
            class="alert alert-info shadow-lg my-8 text-blue-800 bg-blue-100 border-blue-200">
            <svg class="w-6 h-6 stroke-current shrink-0" fill="none" viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
            <span>ຍັງບໍ່ມີທຸລະກຳໃນລະບົບ. ກະລຸນາເພີ່ມທຸລະກຳໃໝ່.</span>
          </div>

          <div v-else class="overflow-x-auto bg-white shadow-xl rounded-2xl border border-gray-200">
            <table class="table w-full">
              <thead class="bg-gray-100 text-gray-700 uppercase text-sm font-semibold sticky top-0">
                <tr>
                  <th class="p-4 text-left">ID</th>
                  <th class="p-4 text-left">ບັນຊີຕົ້ນທາງ</th>
                  <th class="p-4 text-left">ບັນຊີປາຍທາງ</th>
                  <th class="p-4 text-center">ຈຳນວນ</th>
                  <th class="p-4 text-center">ວັນທີ</th>
                  <th class="p-4 text-center">ຄວາມສ່ຽງ</th>
                  <th class="p-4 text-center">ຄະແນນ</th>
                  <th class="p-4 text-center">Actions</th>
                </tr>
              </thead>

              <tbody>
                <tr v-for="txn in paginatedTransactionList" :key="txn.txn_id"
                  class="hover:bg-gray-50 transition duration-150">
                  <td class="p-4 font-mono text-xs">{{ txn.txn_id }}</td>
                  <td class="p-4">
                    <p class="font-bold text-gray-800">{{ txn.source_acct_name }}</p>
                    <p class="text-sm text-gray-500">{{ txn.source_acct }}</p>
                  </td>
                  <td class="p-4">
                    <p class="font-bold text-gray-800">{{ txn.dest_acct_name }}</p>
                    <p class="text-sm text-gray-500">{{ txn.dest_acct }}</p>
                  </td>
                  <td class="p-4 text-center">
                    <div class="font-bold text-lg text-blue-600">
                      {{ txn.trans_amount }}
                    </div>
                    <div class="text-sm text-gray-500">
                      {{ txn.currency }}
                    </div>
                  </td>
                  <td class="p-4 text-center text-sm">
                    {{ new Date(txn.txn_date).toLocaleDateString('en-CA') }}
                  </td>
                  <td class="p-4 text-center">
                    <div class="badge" :class="{
                      'badge-error': txn.risk_lv === 'High',
                      'badge-warning': txn.risk_lv === 'Medium',
                      'badge-success': txn.risk_lv === 'Low' || txn.risk_lv === null
                    }">
                      {{ txn.risk_lv || 'ປົກກະຕິ' }}
                    </div>
                  </td>
                  <td class="p-4 text-center font-semibold">{{ txn.score ?? '-' }}</td>

                  <td class="p-4 text-center">
                    <div class="flex justify-center gap-2">
                      <button @click="editTransaction(txn)" class="btn btn-sm btn-info btn-square tooltip"
                        data-tip="ແກ້ໄຂ">
                        <svg class="w-5 h-5 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                          <path
                            d="M96 128C78.3 128 64 142.3 64 160C64 177.7 78.3 192 96 192L182.7 192C195 220.3 223.2 240 256 240C288.8 240 317 220.3 329.3 192L544 192C561.7 192 576 177.7 576 160C576 142.3 561.7 128 544 128L329.3 128C317 99.7 288.8 80 256 80C223.2 80 195 99.7 182.7 128L96 128zM96 288C78.3 288 64 302.3 64 320C64 337.7 78.3 352 96 352L342.7 352C355 380.3 383.2 400 416 400C448.8 400 477 380.3 489.3 352L544 352C561.7 352 576 337.7 576 320C576 302.3 561.7 288 544 288L489.3 288C477 259.7 448.8 240 416 240C383.2 240 355 259.7 342.7 288L96 288zM96 448C78.3 448 64 462.3 64 480C64 497.7 78.3 512 96 512L150.7 512C163 540.3 191.2 560 224 560C256.8 560 285 540.3 297.3 512L544 512C561.7 512 576 497.7 576 480C576 462.3 561.7 448 544 448L297.3 448C285 419.7 256.8 400 224 400C191.2 400 163 419.7 150.7 448L96 448z" />
                        </svg>
                      </button>

                      <button @click="handleDelete(txn.txn_id)" class="btn btn-sm btn-error btn-square tooltip"
                        data-tip="ລົບ">
                        <svg class="w-5 h-5 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                          <path
                            d="M232.7 69.9L224 96L128 96C110.3 96 96 110.3 96 128C96 145.7 110.3 160 128 160L512 160C529.7 160 544 145.7 544 128C544 110.3 529.7 96 512 96L416 96L407.3 69.9C402.9 56.8 390.7 48 376.9 48L263.1 48C249.3 48 237.1 56.8 232.7 69.9zM512 208L128 208L149.1 531.1C150.7 556.4 171.7 576 197 576L443 576C468.3 576 489.3 556.4 490.9 531.1L512 208z" />
                        </svg>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
              <tfoot>
                <tr class="bg-gray-100 text-gray-700 text-sm font-semibold">
                  <td colspan="8" class="p-4 font-extrabold text-right">
                    <span class="text-gray-700 mr-2">ລວມທັງໝົດ:</span>
                    <span class="text-black">{{ transactionStore.transactionList.length }} ທຸລະກຳ</span>
                  </td>
                </tr>
              </tfoot>
            </table>
          </div>
        </div>

        <div v-if="totalPages > 1" class="flex justify-center mt-6">
          <div class="join shadow-md">
            <button class="join-item btn btn-sm md:btn-md"
                    :disabled="currentPage === 1"
                    @click="goToPage(currentPage - 1)">
              «
            </button>

            <button v-for="page in totalPages" :key="page"
                    class="join-item btn btn-sm md:btn-md"
                    :class="{'btn-active btn-primary text-white': currentPage === page}"
                    @click="goToPage(page)">
              {{ page }}
            </button>

            <button class="join-item btn btn-sm md:btn-md"
                    :disabled="currentPage === totalPages"
                    @click="goToPage(currentPage + 1)">
              »
            </button>
          </div>
        </div>
        </div>
    </div>
  </div>

  <div class="drawer drawer-end z-50">
    <input v-model="isEditDrawerOpen" id="edit-drawer" type="checkbox" class="drawer-toggle" />
    <div class="drawer-content"></div>
    <div class="drawer-side">
      <label for="edit-drawer" class="drawer-overlay"></label>
      <div class="menu bg-white min-h-full w-80 sm:w-96 p-6 shadow-xl">
        <h2 class="text-2xl font-bold mb-4 text-gray-800 border-b pb-2">ແກ້ໄຂທຸລະກຳ</h2>
        <TransEdit v-if="editingTxn" v-model="editingTxn" @update:model-value="handleCloseEditModal" />
      </div>
    </div>
  </div>

  <AppFooter></AppFooter>
</template>
