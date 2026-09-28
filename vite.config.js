import path from 'path';
import { defineConfig } from 'vite';

export default defineConfig(() => {
  return {
    resolve: {
      alias: {
        '@': path.resolve(import.meta.dirname || '.', '.'),
      },
    },
    server: {
      port: 3000,
      hmr: process.env.DISABLE_HMR !== 'true',
      watch: process.env.DISABLE_HMR === 'true' ? null : {},
    },
  };
});

