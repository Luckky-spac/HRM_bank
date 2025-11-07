// src/types/customer.d.ts
export interface ICustomer {
  id: number;
  cus_id: string;
  user_id: string;
  firstname_la: string;
  lastname_la: string;
  firstname_en: string | null;
  lastname_en: string | null;
  birthday: string | null;
  gender: 'M' | 'F' | 'Other' | null;
  nationality: string | null;
  job: string | null;
  add_id: number | null;
  marital_status: string | null;
  national_id: string | null;
  status: number | 'ACTIVE' | 'INACTIVE';
  update_at: string;
  create_at: string;
}
