export interface Transaction {
  row_num?: number
  txn_id: number
  cus_id: string
  txn_date: string
  source_acct: string
  source_acct_name: string
  s_customer_type: 'individual' | 'corporate' | 'institution'
  trans_type: 'Inward' | 'outward'
  dest_acct: string
  dest_acct_name: string
  d_customer_type: 'individual' | 'corporate' | 'institution'
  trans_amount: number
  currency?: string
  txn_desc?: string
  purpose?: string
  relationship?: string
  createdAt?: string
  updateAt?: string

  // from table transaction_risk
  risk_lv?: string
  score?: number
}

export interface TransactionCreateForm extends Partial<Transaction> {

  cus_id: string
  txn_date: string
  source_acct: string
  source_acct_name: string
  s_customer_type: 'individual' | 'corporate' | 'institution'
  trans_type: 'Inward' | 'outward'
  dest_acct: string
  dest_acct_name: string
  d_customer_type: 'individual' | 'corporate' | 'institution'
  trans_amount: number
  currency?: string
  txn_desc?: string
  purpose?: string
  relationship?: string

  // from table transaction_risk
  risk_lv?: string
  score?: number
}

export type TransactionPayload = Omit<Partial<Transaction>, 'txn_id' | 'cus_id' | 'txn_date'>;
