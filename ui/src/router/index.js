import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/Home.vue'
import Categories from '../views/Categories.vue'
import Products from '../views/Products.vue'

const routes = [
  {path: '/', name: 'Home', component: Home},
  {path: '/category', name: 'Categories', component: Categories},
  {path: '/product', name: 'Products', component: Products}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
