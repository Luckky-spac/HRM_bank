import { defineRule } from 'vee-validate';
import { required, email, min, max } from '@vee-validate/rules'; // ต้องติดตั้ง package @vee-validate/rules

export function setupVeeValidate() {
  // Register default rules
  defineRule('required', required);
  defineRule('email', email);
  defineRule('min', min);
  defineRule('max', max);

}
