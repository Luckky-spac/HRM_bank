<script lang="ts" setup>
import { useField } from 'vee-validate'
import { useId } from 'vue'
const props = defineProps<{
  name: string
  label?: string
  placeholder?: string
  class?: string
  type?: 'text' | 'number' | 'textarea'
}>()
const { value, errorMessage } = useField<string | number | undefined>(() => props.name)
const id = useId()
</script>

<template>
  <fieldset class="fieldset">
    <legend class="fieldset-legend">{{ props.label }}</legend>
    <textarea
      v-if="props.type === 'textarea'"
      v-model="value"
      :id="id"
      :class="props.class"
      :placeholder="props.placeholder"
      :name="props.name"
    ></textarea>
    <input
      v-else
      v-model="value"
      :id="id"
      :type="props.type"
      :class="props.class"
      :placeholder="props.placeholder"
      :name="props.name"
    />
    <span v-if="errorMessage" class="error">{{ errorMessage }}</span>
  </fieldset>
</template>
