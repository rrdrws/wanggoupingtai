import { createRouter, createWebHistory } from 'vue-router'
import HomeView from './views/HomeView.vue' // Changed from HelloWorld to a view
import ProductList from './views/ProductList.vue'
import ProductDetail from './views/ProductDetail.vue'
import UserLogin from './views/UserLogin.vue'
import UserRegistration from './views/UserRegistration.vue'
import ShoppingCart from './views/ShoppingCart.vue'
// import UserProfile from './views/UserProfile.vue' // Example for a user-specific page

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView // Default component, can be ProductList or a dedicated Home page
  },
  {
    path: '/products',
    name: 'ProductList',
    component: ProductList
  },
  {
    path: '/product/:id', // Changed path to be more RESTful (singular 'product')
    name: 'ProductDetail',
    component: ProductDetail,
    props: true
  },
  {
    path: '/login',
    name: 'Login',
    component: UserLogin
  },
  {
    path: '/register',
    name: 'Register',
    component: UserRegistration
  },
  {
    path: '/cart',
    name: 'Cart',
    component: ShoppingCart
  },
  // {
  //   path: '/profile',
  //   name: 'UserProfile',
  //   component: UserProfile
  //   // meta: { requiresAuth: true } // Example for protected route
  // },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ './views/AboutView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
