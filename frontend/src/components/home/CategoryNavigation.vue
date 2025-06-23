<template>
  <nav class="category-navigation-panel"> <!-- Changed class name -->
    <h2 class="panel-title">商品分类</h2>
    <ul class="category-list">
      <li v-for="category in categories" :key="category.id" class="category-item">
        <router-link :to="category.link" class="category-link">
          <span class="category-icon">{{ category.icon }}</span>
          <span class="category-name">{{ category.name }}</span>
        </router-link>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: 'CategoryNavigation',
  data() {
    return {
      categories: [
        { id: 1, name: '电子产品', icon: '📱', link: '/products?category=Electronics' },
        { id: 2, name: '图书音像', icon: '📚', link: '/products?category=Books' },
        { id: 3, name: '服装服饰', icon: '👕', link: '/products?category=Clothing' },
        { id: 4, name: '家居厨具', icon: '🏠', link: '/products?category=Home%20%26%20Kitchen' },
        { id: 5, name: '运动户外', icon: '🚴', link: '/products?category=Sports%20%26%20Outdoors' },
        { id: 6, name: '美妆健康', icon: '💄', link: '/products?category=Beauty%20%26%20Health' },
        { id: 7, name: '玩具游戏', icon: '🎮', link: '/products?category=Toys%20%26%20Games' },
      ]
    };
  }
}
</script>

<style scoped>
.category-navigation-panel {
  background-color: #fff;
  padding: 20px; /* Increased padding */
  border-radius: 8px; /* Slightly larger radius */
  box-shadow: 0 3px 8px rgba(0,0,0,0.08); /* Slightly enhanced shadow */
}

.panel-title { /* New class for title styling */
  margin-top: 0;
  margin-bottom: 15px; /* Increased margin */
  font-size: 1.3em; /* Slightly larger title */
  color: #333;
  border-bottom: 1px solid #f0f0f0; /* Lighter border */
  padding-bottom: 12px; /* Increased padding */
  font-weight: 600; /* Bolder title */
}

.category-list { /* New class for ul */
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-item { /* New class for li */
  margin-bottom: 5px; /* Space between items */
}

.category-link { /* New class for a */
  display: flex; /* For icon and text alignment */
  align-items: center;
  padding: 10px 12px; /* Increased padding */
  color: #337ab7; /* Standard link blue */
  text-decoration: none;
  border-radius: 5px; /* Rounded corners for links */
  transition: background-color 0.2s ease, color 0.2s ease;
  font-size: 1em; /* Slightly larger font for items */
}

.category-link:hover {
  background-color: #e9f5ff; /* Light blue hover */
  color: #23527c; /* Darker blue on hover */
}

.category-icon {
  margin-right: 10px; /* Space between icon and text */
  font-size: 1.2em; /* Slightly larger icon */
  width: 20px; /* Fixed width for alignment */
  text-align: center;
}

.category-name {
  flex-grow: 1; /* Name takes remaining space */
}
</style>
