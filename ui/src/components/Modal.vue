<script setup>
import {onMounted, onUnmounted} from "vue";

defineProps({
  show: Boolean
})

const emit = defineEmits(['closeModal']);

const close = () => emit('closeModal');

function onEsc(event) {
  if (event.key === 'Escape') {
    close();
  }
}

onMounted(() => {
window.addEventListener('keydown', onEsc);
});

onUnmounted(() => {
  window.removeEventListener('keydown', onEsc);
});

</script>

<template>
  <div class="modal-backdrop" v-if="show" @click="close"></div>
  <div class="modal" v-if="show">
    <div class="modal-content">
      <div class="modal-header" v-if="$slots.header">
        <slot name="header" />
      </div>
      <div class="modal-body">
        <slot />
      </div>
      <div class="modal-footer" v-if="$slots.footer">
        <slot name="footer" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop {
  position: absolute;
  inset: 0;
  background: black;
  opacity: 30%;
}

.modal {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);

  display: flex;
  align-items: center;
  justify-content: center;

  z-index: 9999;
}

.modal-content {
  display: flex;
  flex-direction: column;
  border: 1px solid white;
  border-radius: 10px;
  background: #2c3e50;
}

.modal-content > * {
  padding: 10px;
}

.modal-header {
  color: #00ff00;
  display: flex;
}

.modal-body {
  display: flex;
  flex-grow: 1;
}

.modal-footer {
  display: flex;
}

.modal-footer > * {
  flex-grow: 1;
}

</style>