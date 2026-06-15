import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  base: '/api/',
  build: {
    outDir: '../src/main/resources/static',
    emptyOutDir: true,
  },
  server: {
    proxy: {
      // Proxy only actual REST endpoints, not frontend routes
      // /api/site  and /api/site/:id  (NOT /api/sites/... which are frontend routes)
      '^/api/site(/[0-9]+)?$': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '^/api/dashboard': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})