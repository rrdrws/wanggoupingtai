import { createRouter, createWebHistory } from 'vue-router'
import HomeView from './views/HomeView.vue'
import ProductList from './views/ProductList.vue'
import ProductDetail from './views/ProductDetail.vue'
import UserLoginView from './views/UserLogin.vue' // Corrected import name assuming file is UserLogin.vue but component name is UserLoginView
import UserRegistration from './views/UserRegistration.vue'
import ShoppingCart from './views/ShoppingCart.vue'
import ProductPublishView from './views/ProductPublish.vue'
// import UserProfile from './views/UserProfile.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/products',
    name: 'ProductList',
    component: ProductList
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: ProductDetail,
    props: true
  },
  {
    path: '/login',
    name: 'Login',
    component: UserLoginView // Use corrected name
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
  {
    path: '/publish-product', // New route for publishing products
    name: 'ProductPublish',
    component: ProductPublishView
    // meta: { requiresAuth: true, roles: ['ADMIN', 'SELLER'] } // Example for future auth guard
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
