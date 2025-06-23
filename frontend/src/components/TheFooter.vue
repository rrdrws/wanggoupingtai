<template>
  <footer class="site-footer">
    <div class="container">
      <div class="footer-links">
        <router-link to="/about">关于我们</router-link>
        <a href="#">联系我们 (占位符)</a>
        <a href="#">服务条款 (占位符)</a>
        <a href="#">隐私政策 (占位符)</a>
      </div>
      <div class="footer-social">
        <!-- Placeholder for social media icons -->
        <a href="#" aria-label="Facebook"><i class="fab fa-facebook-f"></i></a>
        <a href="#" aria-label="Twitter"><i class="fab fa-twitter"></i></a>
        <a href="#" aria-label="Instagram"><i class="fab fa-instagram"></i></a>
      </div>
      <div class="copyright">
        &copy; {{ new Date().getFullYear() }} 我的商店. 版权所有.
        <p>本网站设计灵感来源于淘宝等电商平台，仅用于学习目的。</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'TheFooter'
}
</script>

<style scoped>
.site-footer {
  background-color: #f8f9fa; /* Light grey background */
  color: #333;
  padding: 30px 0;
  border-top: 1px solid #e0e0e0;
  font-size: 0.9em;
  margin-top: 40px; /* Space above the footer */
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 15px;
  text-align: center;
}

.footer-links {
  margin-bottom: 15px;
}

.footer-links a {
  color: #007bff;
  text-decoration: none;
  margin: 0 10px;
}

.footer-links a:hover {
  text-decoration: underline;
}

.footer-social {
  margin-bottom: 15px;
}

.footer-social a {
  color: #555;
  text-decoration: none;
  margin: 0 8px;
  font-size: 1.2em; /* Placeholder for icon size */
}

.footer-social a:hover {
  color: #007bff;
}

/* Font Awesome icons (if you decide to use them) */
/* @import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css'); */

.copyright {
  font-size: 0.85em;
  color: #6c757d;
}
.copyright p {
    font-size: 0.9em;
    color: #888;
    margin-top: 5px;
}
</style>
