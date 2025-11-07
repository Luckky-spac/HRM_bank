<script lang="ts" setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router';
import { useTransactionStore } from '@/stores/transaction';
import type { Transaction } from '@/types/transaction';

const router = useRouter();
const transactionStore = useTransactionStore()
const emit = defineEmits(['transaction-created'])
const submitting = ref(false)

function formatDateForMySQL(date: Date | string) {
  const d = new Date(date);
  if (isNaN(d.getTime())) return '';
  // return 'YYYY_MM_DD HH:mm:ss'
  return d.toISOString().slice(0, 19).replace("T", " ");
}

// reactive form data
const form = ref<Partial<Transaction>>({
  cus_id: undefined,
  txn_date: formatDateForMySQL(new Date()),
  source_acct: '',
  source_acct_name: '',
  s_customer_type: 'individual',
  trans_type: 'Inward',
  dest_acct: '',
  dest_acct_name: '',
  d_customer_type: 'individual',
  trans_amount: 0,
  currency: 'LAK',
  txn_desc: '',
  purpose: '',
  relationship: '',
  risk_lv: 'low',
  score: 0
})


const submit = async () => {
  // validation
  if (!form.value.cus_id) {
    alert('CUS_ID is required (customer id).');
    return;
  }
  if (!form.value.source_acct || !form.value.dest_acct) {
    alert('Source and Dest account numbers are required.');
    return;
  }
  submitting.value = true;

  try {
    // ensure txn_date format
    if (form.value.txn_date) {
      form.value.txn_date = formatDateForMySQL(form.value.txn_date);
    }
      await transactionStore.createTransaction(form.value);
      alert('Transaction created successfull!');
      emit('transaction-created')
    //reset form
      form.value = {
        cus_id: undefined,
        txn_date: formatDateForMySQL(new Date()),
        source_acct: '',
        source_acct_name: '',
        s_customer_type: 'individual',
        trans_type: 'Inward',
        dest_acct: '',
        dest_acct_name: '',
        d_customer_type: 'individual',
        trans_amount: 0,
        currency: 'LAK',
        txn_desc: '',
        purpose: '',
        relationship: '',
        risk_lv: 'low',
        score: 0,
      }

  } catch (err) {
    console.error(err)
    alert('Failed to create transaction')
  } finally {
    submitting.value = false;
  }
};

</script>

<template>
  <div class="max-w-4xl mx-auto p-6 bg-white shadow-lg rounded-lg border border-gray-200">
    <h2 class="text-2xl font-semibold mb-6 text-gray-800">ເພີ່ມທຸລະກຳໃໝ່</h2>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <input v-model="form.cus_id" placeholder="CUS_ID"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />

        <input v-model="form.currency" placeholder="ສະກຸນເງິນ"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />

      <input v-model="form.source_acct_name" placeholder="ຊື່ບັນຊີຕົ້ນທາງ"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />

              <input v-model="form.dest_acct_name" placeholder="ຊື່ບັນຊີປາຍທາງ"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />

      <input v-model="form.source_acct" required placeholder="ເລກບັນຊີຕົ້ນທາງ"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />

              <input v-model="form.dest_acct" required placeholder="ເລກບັນຊີປາຍທາງ"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />

      <select v-model="form.s_customer_type"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300">
        <option value="individual">ລາຍບຸກຄົນ</option>
        <option value="corporate">ອົງກອນ</option>
        <option value="institution">ສະຖາບັນ</option>
      </select>

      <select v-model="form.d_customer_type"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300">
        <option value="individual">ລາຍບຸກຄົນ</option>
        <option value="corporate">ອົງກອນ</option>
        <option value="institution">ສະຖາບັນ</option>
      </select>

       <input v-model="form.trans_amount" type="number" placeholder="ຈຳນວນ"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />

      <select v-model="form.trans_type"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300">
        <option value="Inward">ເງິນເຂົ້າ</option>
        <option value="outward">ເງິນອອກ</option>
      </select>

      <input v-model="form.txn_desc" placeholder="ຄຳອະທຶບາຍເພີ່ມເຕິມ"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />
      <input v-model="form.purpose" placeholder="ຈຸດປະສົງ"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />
      <input v-model="form.relationship" placeholder="ຄວາມສຳພັນ"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />

      <select v-model="form.risk_lv"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300">
        <option value="low">ຕ່ຳ</option>
        <option value="medium">ປານກາງ</option>
        <option value="high">ສູງ</option>
      </select>

      <input v-model="form.score" placeholder="ຄະແນນຄວາມສຽງ"
        class="input input-bordered w-full rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-300" />
    </div>

    <div class="mt-6 flex justify-end gap-3">
      <button @click="submit"
        class="bg-blue-500 hover:bg-blue-600 text-white font-semibold px-5 py-2 rounded-lg shadow">
        {{ submitting ? 'Saving...' : 'ເພີ່ມທຸລະກຳ' }}
      </button>
      <button @click="() => { form.txn_desc=''; }" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-semibold px-5 py-2 rounded-lg shadow">
        Cancel
      </button>
    </div>
  </div>
</template>
