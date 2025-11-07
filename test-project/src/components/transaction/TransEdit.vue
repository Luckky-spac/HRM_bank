<script lang="ts" setup>
import { ref, watch, defineProps, defineEmits } from 'vue'
import { useTransactionStore } from '@/stores/transaction';
import type { Transaction } from '@/types/transaction';

const transactionStore = useTransactionStore();

const props = defineProps<{
  modelValue: Transaction | null
}>()

const emit = defineEmits<{
  (e: "update:modelValue", value: Transaction | null): void
}>()

const localTxn = ref<Transaction | null>(null)
const submitting = ref(false);

// UTILITY function
const toInputDate = (date: string | undefined): string | undefined => {
  if (!date) return undefined;
  return date.split(' ')[0]
}

watch(
  () => props.modelValue,
  (val) => {
    localTxn.value = val ? { ...val } : null
    if (localTxn.value && localTxn.value.txn_date) {
        // Format Date สำหรับ input type="date"
        localTxn.value.txn_date = toInputDate(localTxn.value.txn_date);
    }
  },
  { immediate: true }
)

const cancel = () => {
  emit("update:modelValue", null)
}

const save = async () => {
  if (!localTxn.value || !localTxn.value.txn_id) return
  submitting.value = true;

  const payload: Partial<Transaction> = {

    source_acct: localTxn.value.source_acct,
    source_acct_name: localTxn.value.source_acct_name,
    s_customer_type: localTxn.value.s_customer_type,
    trans_type: localTxn.value.trans_type,
    dest_acct: localTxn.value.dest_acct,
    dest_acct_name: localTxn.value.dest_acct_name,
    d_customer_type: localTxn.value.d_customer_type,
    trans_amount: localTxn.value.trans_amount,
    currency: localTxn.value.currency,

    // Optional Field that use undefined
    txn_desc: localTxn.value.txn_desc ?? undefined,
    purpose: localTxn.value.purpose ?? undefined,
    relationship: localTxn.value.relationship ?? undefined,
    risk_lv: localTxn.value.risk_lv ?? undefined,
    score: localTxn.value.score ?? undefined,

  }

try {
    const updatedTxn = await transactionStore.updateTransaction(localTxn.value.txn_id, payload);

    alert(`Transaction ID ${updatedTxn.txn_id} updated successfully!`);
    emit("update:modelValue", null);
  } catch (err) {
    console.error(err)
    alert("Failed to save transaction: " + transactionStore.error)
  } finally {
    submitting.value = false;
  }
}

</script>

<template>
  <!-- edit modal -->
  <div v-if="localTxn" class="fixed inset-0 flex items-center justify-center z-50 p-4">
    <div class="relative bg-white rounded-xl shadow-2xl w-full max-w-xl p-6 border border-gray-200">

      <!-- Close button -->
      <div class="flex justify-end mb-4">
        <button @click="cancel"
          class="absolute top-4 right-4 text-gray-400 hover:text-gray-600 text-3xl font-light">&times;</button>
      </div>

      <h2 class="text-2xl font-semibold text-gray-800 mb-6">ແກ້ໄຂທຸລະກຳ</h2>
      <p class="text-sm text-gray-500 mb-4 ">ລະຫັດລູກຄ້າ: {{ localTxn.cus_id }}</p>

      <form @submit.prevent="save" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- Source Name -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ຊື່ ບັນຊີຕົ້ນທາງ</label>
            <input v-model="localTxn.source_acct_name" type="text"
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none" />
          </div>
          <!-- Destination Name -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ຊື່ ບັນຊີປາຍທາງ</label>
            <input v-model="localTxn.dest_acct_name" type="text"
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none" />
          </div>
          <!-- Source Account -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ເລກບັນຊີ ຕົ້ນທາງ</label>
            <input v-model="localTxn.source_acct" type="text" required
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none" />
          </div>
          <!-- Destination Account -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ເລກບັນຊີ ປາຍທາງ</label>
            <input v-model="localTxn.dest_acct" type="text" required
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none" />
          </div>
          <!-- Source Type -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ປະເພດທຸລະກຳ ຕົ້ນທາງ</label>
            <select v-model="localTxn.s_customer_type" required
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none">
              <option value="individual">ລາຍບຸກຄົນ</option>
              <option value="corporate">ອົງກອນ</option>
              <option value="institution">ສະຖາບັນ</option>
            </select>
          </div>
          <!-- Destination Type -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ປະເພດທຸລະກຳ ປາຍທາງ</label>
            <select v-model="localTxn.d_customer_type" required
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none">
              <option value="individual">ລາຍບຸກຄົນ</option>
              <option value="corporate">ອົງກອນ</option>
              <option value="institution">ສະຖາບັນ</option>
            </select>
          </div>
          <!-- Transaction Type -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ປະເພດທຸລະກຳ</label>
            <select v-model="localTxn.trans_type" required
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none">
              <option value="Inward">ເງິນເຂົ້າ</option>
              <option value="outward">ເງິນອອກ</option>
            </select>
          </div>
          <!-- Amount -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ຈຳນວນ</label>
            <input v-model="localTxn.trans_amount" type="number"
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none" />
          </div>
          <!-- Currency -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ຄ່າເງິນ</label>
            <input v-model="localTxn.currency" type="text"
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none" />
          </div>
          <!-- Risk Level -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ລະດັບ ຄວາມສຽງ</label>
            <select v-model="localTxn.risk_lv"
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none">
              <option value="low">ຕ່ຳ</option>
              <option value="medium">ປານກາງ</option>
              <option value="high">ສູງ</option>
            </select>
          </div>
          <!-- Score -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ຄະແນນ ຄວາມສຽງ</label>
            <input v-model="localTxn.score" type="number"
              class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-300 focus:outline-none" />
          </div>

        </div>

        <div class="mt-6 flex justify-end gap-3">
          <button type="submit" :disabled="submitting"
                  class="bg-gradient-to-r from-blue-500 to-blue-600 text-white px-5 py-2 rounded-lg shadow-lg hover:from-blue-600 hover:to-blue-700 focus:ring-2 focus:ring-offset-2 focus:ring-blue-400 disabled:opacity-50">
            {{ submitting ? 'Saving...' : 'Save' }}
          </button>
          <button @click="cancel" type="button"
                 class="bg-gray-100 text-gray-700 px-5 py-2 rounded-lg shadow hover:bg-gray-200 focus:ring-2 focus:ring-offset-2 focus:ring-gray-300 cursor-pointer">
          Cancel
          </button>
        </div>

      </form>
    </div>
  </div>
</template>
