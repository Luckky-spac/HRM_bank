// types/employee.d.ts
export interface IEmployee {
  id: number;
  emp_id: string;
  prefix: string | null;
  firstname_la: string;
  lastname_la: string;
  firstname_en: string | null;
  lastname_en: string | null;
  gender: 'M' | 'F' | 'Other' | null;
  phonenumber: string | null;
  add_id: number | null;
  branch_id: number | null;
  dept_id: number | null;
  sect_id: number | null;
  post_id: number | null;
  status: number | 'ACTIVE' | 'INACTIVE';

}
