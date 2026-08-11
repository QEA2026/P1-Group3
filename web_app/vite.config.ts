import { defineConfig } from 'vite'
import react, { reactCompilerPreset } from '@vitejs/plugin-react'
import babel from '@rolldown/plugin-babel'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  server: {
    host: '0.0.0.0',
    port: 5173,
    proxy: {
      '/api/employee': {
        target: 'http://employee-backend:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/employee/, ''),
      },
      '/api/manager': {
        target: 'http://manager-backend:9090',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/manager/, ''),
      },
    },
  },
  plugins: [
    react(),
    babel({ presets: [reactCompilerPreset()] }),
    tailwindcss(),
  ],
})