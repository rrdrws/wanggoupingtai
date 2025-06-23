<template>
  <div class="about-view view-container-global">
    <div class="about-panel">
      <h1>关于 我的商店</h1>
      <p>欢迎来到“我的商店”！我们致力于为您提供最佳的在线购物体验。</p>
      <p>我们的平台提供种类繁多的商品、有竞争力的价格以及优质的客户服务。</p>
      <p>本项目是一个使用 Vue.js 和 Spring Boot 构建的演示平台，其灵感来源于现代电子商务解决方案，旨在用于学习和开发目的。</p>
      <p>感谢您的光临！</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AboutView' // Can keep as AboutView or change to AboutPageView
}
</script>

<style scoped>
.about-view {
  /* Uses .view-container-global for max-width and horizontal padding */
  padding-top: 30px;
  padding-bottom: 30px;
}

.about-panel {
  background-color: #fff;
  padding: 30px 40px;
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  text-align: left; /* Align text to left for readability */
}

.about-panel h1 {
  text-align: center; /* Center the main heading */
  margin-bottom: 25px;
  font-size: 1.8em;
  color: #333;
}

.about-panel p {
  line-height: 1.8; /* Improved line height for readability */
  margin-bottom: 15px;
  color: #555;
}
</style>
