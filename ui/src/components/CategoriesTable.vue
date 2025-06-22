<script setup>
import Modal from "@/components/Modal.vue";
import {computed, onMounted, ref, reactive} from 'vue'

const categories = reactive([])

const isSaving = ref(false);
const errorsDuringSave = reactive({});

const selectedCategory = ref(null);
const newCategory = ref(null);

const originalCategoryName = computed(() => {
  if (selectedCategory.value) {
    return categories.find(c => c.id === selectedCategory.value.id).name;
  }

  return null;
});

const categoryNameError = computed(() => {
  return errorsDuringSave.name;
});

const possibleParentCategories = computed(() => {
  if (selectedCategory.value) {
    return categories.filter(c => c.id !== selectedCategory.value.id);
  }

  return categories;
})

onMounted(async () => {
  const response = await fetch('/api/categories')
  if (response.ok) {
    const data = await response.json()
    categories.splice(0, categories.length, ...data)
  } else {
    console.error('Error when fetch categories!')
  }
});

function getParentName(id) {
  if (!id) {
    return ''
  }

  return categories.find(c => c.id === id)?.name || '';
}

function deleteCategory(id) {
  fetch(`/api/categories/${id}`, {
    method: 'DELETE'
  }).then(() => {
    const index = categories.findIndex(c => c.id === id);
    if (index !== -1) {
      categories.splice(index, 1);
    }
  });
}

function editCategory(category) {
  selectedCategory.value = {...category};
}

function saveEditedCategory() {
  isSaving.value = true;

  if (selectedCategory.value) {
    saveCategory();
  } else {
    createCategory();
  }
}

function saveCategory() {
  fetch(`/api/categories/${selectedCategory.value.id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(selectedCategory.value)
  }).then(async resp => {
    if (resp.ok) {
      const index = categories.findIndex(c => c.id === selectedCategory.value.id);
      categories[index] = {...selectedCategory.value};
      selectedCategory.value = null;
      updateErrorsDuringSave({});
    } else {
      if (resp.status === 400) {
        updateErrorsDuringSave(await resp.json());
      }
    }

    isSaving.value = false;
  });
}

function createCategory() {
  fetch(`/api/categories`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(newCategory.value)
  }).then(async resp => {
    if (resp.ok) {
      categories.push(await resp.json());
      newCategory.value = null;
      updateErrorsDuringSave({});
    } else {
      if (resp.status === 400) {
        updateErrorsDuringSave(await resp.json());
      }
    }

    isSaving.value = false;
  });
}

function updateErrorsDuringSave(errors) {
  for (const key in errorsDuringSave) {
    delete errorsDuringSave[key];
  }

  Object.assign(errorsDuringSave, errors);
}

</script>

<template>
  <table v-if="categories.length">
    <thead>
    <tr>
      <th>ID</th>
      <th>Name</th>
      <th>Parent</th>
      <th></th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="category in categories" :key="category.id">
      <td>{{ category.id }}</td>
      <td>{{ category.name }}</td>
      <td>{{ getParentName(category.parentId) }}</td>
      <td style="width: 0">
        <div style="display: flex; gap: 5px">
          <button @click="editCategory(category)">Edit</button>
          <button @click="deleteCategory(category.id)">Delete</button>
        </div>
      </td>
    </tr>
    </tbody>
  </table>
  <p v-else>No categories found! Please create first</p>

  <button @click="newCategory = {}">Create</button>

  <Teleport to="body">
    <Modal :show="Boolean(selectedCategory || newCategory)"
           @closeModal="selectedCategory = null; newCategory = null; errorsDuringSave = {}">
      <template #header>
        <h1 v-if="selectedCategory">Category: '{{ originalCategoryName }}'</h1>
        <h1 v-else>New category</h1>
      </template>

      <div style="display: flex; flex-direction: column; gap: 10px; flex-grow: 1" @keydown.enter="saveEditedCategory">
        <div>
          <label for="categoryName" class="form-label">Name:</label>
          <input id="categoryName" v-if="selectedCategory" v-model="selectedCategory.name" class="form-control"
                 :disabled="isSaving">
          <input id="categoryName" v-else v-model="newCategory.name" class="form-control" :disabled="isSaving">

          <div style="color: red" v-if="categoryNameError">
            {{ categoryNameError }}
          </div>
        </div>

        <div v-show="possibleParentCategories.length">
          <label for="parentCategory" class="form-label">Parent category:</label>
          <select id="parentCategory" class="form-control" v-if="selectedCategory" v-model="selectedCategory.parentId"
                  :disabled="isSaving">
            <option :value="null">---</option>
            <option v-for="category in possibleParentCategories" :value="category.id">
              {{ category.name }}
            </option>
          </select>

          <select id="parentCategory" class="form-control" v-else v-model="newCategory.parentId" :disabled="isSaving">
            <option :value="undefined">---</option>
            <option v-for="category in possibleParentCategories" :value="category.id" :key="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
      </div>

      <template #footer>
        <button @click="saveEditedCategory" :disabled="isSaving">{{ selectedCategory ? 'Save' : 'Create' }}</button>
      </template>
    </Modal>
  </Teleport>
</template>

<style scoped>
input, select {
  width: 100%;
}
</style>