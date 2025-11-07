import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Transaction } from '@/types/transaction'
import { apiFetch } from '@/services/api'

interface ApiResponse<T> {
  data?: T;
}

// UNTILITY function date for format API that must YYYY-MM_DD HH:mm:ss
function formatDateForMySQL(date: Date | string | undefined): string | undefined {
  if(!date) return undefined;
  const d = new Date(date);
  if (isNaN(d.getTime())) return undefined;
  return d.toISOString().slice(0, 19).replace("T", " ")
}

export const useTransactionStore = defineStore('transaction', () => {
  const transactionList = ref<Transaction[]>([])
  const totalRecord = ref(0)
  const loading = ref(false)
  const error = ref<string | null>(null)

  // GET all transactions
  const getAllTransactions = async () => {
    if (loading.value) return
    loading.value = true
    error.value = null
    try {
      const res = await apiFetch<ApiResponse<Transaction[]>>('/transactions', {
        method: 'GET',
      });
      const apiData = Array.isArray(res.data) ? res.data : (res as unknown as Transaction[])
      transactionList.value = apiData;
      totalRecord.value = transactionList.value.length;
    } catch (err: unknown) {
      const errorObj = err instanceof Error ? err : new Error(String(err));
      console.error(errorObj)
      error.value = errorObj.message || 'Error loading transaction.'
    } finally {
      loading.value = false
    }
  }

  // Get transaction by ID
  const getTransactionById = async (id: number) => {
    try {
      const res = await apiFetch<Transaction>(`/transactions/${id}`, {
        method: 'GET',
      });
      return res;
    } catch (err: unknown) {
      const errorObj = err instanceof Error ? err : new Error(String(err));
      console.error('getTransactionById error:', errorObj);
      throw errorObj;
    }
  }

  // GET tranasction with risk info
  const getTransactionWithRisk = async () => {
    if (loading.value) return
    loading.value = true
    error.value = null
    try {
      const res = await apiFetch<ApiResponse<Transaction[]>> ('/transactions?withRisk=true', {
        method: 'GET',
      });
      // if backedn return array directly, adapt
      const apiData = Array.isArray(res.data ) ? res.data : res as unknown as Transaction[];
      transactionList.value = apiData;
    } catch (er){
      const errorObj = er instanceof Error ? er : new Error(String(er));
      console.error('getTransactionWithRIsk', errorObj);
      error.value = errorObj.message;
      throw errorObj;
    } finally {
      loading.value = false;
    }
  }

  // CREATE transaction
  const createTransaction = async (payload: Partial<Transaction>) => {
    loading.value = true
    error.value = null
    try {
      if (payload.txn_date) {
        payload.txn_date = formatDateForMySQL(payload.txn_date);
      }
      const res = await apiFetch<Transaction>('/transactions', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: payload,
      });
      // optional push to local list
      transactionList.value.unshift(res);
      return res;
    } catch (err: unknown) {
      const errorObj = err instanceof Error ? err : new Error(String(err));
      console.error('CreateTransaction error', errorObj)
      error.value = errorObj.message;
      throw errorObj;
    } finally {
      loading.value = false;
    }
  }

  // UPDATE transaction
  const updateTransaction = async (txn_id: number, payload: Partial<Transaction>) => {
    loading.value = true
    error.value = null
    try {
      const updateTxn = await apiFetch<Transaction>(`/transactions/${txn_id}`, {
        method: 'PUT',
        body: payload,
      });
      const idToNumber = Number(txn_id);
      const index = transactionList.value.findIndex(t => t.txn_id === idToNumber)
      if (index !== -1) {
        transactionList.value[index] = updateTxn;
      }
      return updateTxn;
    } catch (err: unknown) {
      const errorObj = err instanceof Error ? err : new Error(String(err));
      console.error('UpdateTransaction error', errorObj)
      error.value = errorObj.message;
      throw errorObj;
    }
  }

  // DELETE transaction
  const deleteTransaction = async (id: number | string) => {
    loading.value = true
    error.value = null
    try {
      await apiFetch(`/transactions/${id}`, {
        method: 'DELETE',
      });
    } catch (err: unknown) {
      const errorObj = err instanceof Error ? err : new Error(String(err));
      console.error(errorObj);
      error.value = errorObj.message || 'Error deleting transaction';
      throw err;
    } finally {
      loading.value = false;
    }
  }
  return {
    transactionList,
    totalRecord,
    loading,
    error,
    getAllTransactions,
    getTransactionById,
    getTransactionWithRisk,
    createTransaction,
    updateTransaction,
    deleteTransaction,
  }
})
