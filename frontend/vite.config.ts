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
  },
  build: {
    chunkSizeWarningLimit: 1000, // 경고 임계값을 1000KB로 상향 (기본값: 500KB)
    rollupOptions: {
      output: {
        manualChunks: {
          // React 관련 라이브러리를 별도 청크로 분리하여 최적화
          'react-vendor': ['react', 'react-dom', 'react-router-dom'],
          'query-vendor': ['@tanstack/react-query'],
        }
      }
    }
  }
})