export type User = {
  row_num?: number
  id?: string
  name: string
  email: string
  role: 'admin' | 'manager' | 'user'
  age: number
  address: string
  photo_url: string
  // optional extended fields
  userId?: string
  username?: string
  nameLa?: string
  nameEn?: string
  gender?: 'male' | 'female'
  customerEmail?: string
  employeeEmail?: string
}
