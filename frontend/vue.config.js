const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8081, // Ensures frontend runs on a different port than backend
    proxy: {
      '/api': { // Proxy API requests to Spring Boot backend
        target: 'http://localhost:8080', // Your Spring Boot backend address
        changeOrigin: true,
        // pathRewrite: { '^/api': '' }, // Uncomment if your Spring Boot API doesn't have /api prefix
      }
    }
  }
})
