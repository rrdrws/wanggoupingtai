<template>
  <nav class="category-navigation">
    <h2>Categories</h2>
    <ul>
      <li v-for="category in categories" :key="category.id">
        <router-link :to="category.link">{{ category.name }}</router-link>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: 'CategoryNavigation',
  data() {
    return {
      // Placeholder categories - in a real app, these would come from an API or config
      categories: [
        { id: 1, name: 'Electronics', link: '/products?category=Electronics' },
        { id: 2, name: 'Books', link: '/products?category=Books' },
        { id: 3, name: 'Clothing', link: '/products?category=Clothing' },
        { id: 4, name: 'Home & Kitchen', link: '/products?category=Home%20%26%20Kitchen' },
        { id: 5, name: 'Sports & Outdoors', link: '/products?category=Sports%20%26%20Outdoors' },
        { id: 6, name: 'Beauty & Health', link: '/products?category=Beauty%20%26%20Health' },
        { id: 7, name: 'Toys & Games', link: '/products?category=Toys%20%26%20Games' },
      ]
    };
  }
}
</script>

<style scoped>
.category-navigation {
  background-color: #fff;
  padding: 15px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.category-navigation h2 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 1.2em;
  color: #333;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.category-navigation ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-navigation li a {
  display: block;
  padding: 8px 0px;
  color: #007bff;
  text-decoration: none;
  border-radius: 3px;
  transition: background-color 0.2s ease;
}

.category-navigation li a:hover {
  background-color: #f0f0f0;
  color: #0056b3;
}
</style>
