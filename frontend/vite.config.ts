import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  root: '.',
  publicDir: 'public',
  server: {
    port: 5174,
    host: 'localhost',
    open: true,
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
})