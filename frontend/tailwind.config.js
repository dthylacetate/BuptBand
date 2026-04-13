/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'metal-black': '#0a0a0a',
        'metal-dark': '#1a1a1a',
        'metal-gray': '#2d2d2d',
        'metal-silver': '#8c8c8c',
        'metal-light': '#d4d4d4',
        'flame-orange': '#ff4500',
        'flame-red': '#dc143c',
        'flame-yellow': '#ffa500',
        'blood-red': '#8b0000',
        'evil-purple': '#4b0082',
      },
      fontFamily: {
        'metal': ['Impact', 'Arial Black', 'sans-serif'],
        'aggressive': ['Arial Black', 'Impact', 'sans-serif'],
      },
      backgroundImage: {
        'metal-gradient': 'linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #0a0a0a 100%)',
        'flame-gradient': 'linear-gradient(45deg, #ff4500 0%, #dc143c 50%, #ffa500 100%)',
        'dark-gradient': 'linear-gradient(180deg, #0a0a0a 0%, #1a1a1a 100%)',
        'metallic': 'linear-gradient(135deg, #2d2d2d 0%, #4a4a4a 50%, #2d2d2d 100%)',
      },
      boxShadow: {
        'metal': '0 4px 6px rgba(0, 0, 0, 0.5), 0 1px 3px rgba(0, 0, 0, 0.3)',
        'flame': '0 0 20px rgba(255, 69, 0, 0.5), 0 0 40px rgba(220, 20, 60, 0.3)',
        'aggressive': '4px 4px 0px #0a0a0a, 8px 8px 0px rgba(255, 69, 0, 0.3)',
        'inset-metal': 'inset 0 2px 4px rgba(0, 0, 0, 0.5)',
      },
      animation: {
        'pulse-flame': 'pulse-flame 2s ease-in-out infinite',
        'shake': 'shake 0.5s ease-in-out',
        'flicker': 'flicker 3s ease-in-out infinite',
      },
      keyframes: {
        'pulse-flame': {
          '0%, 100%': { opacity: '1', transform: 'scale(1)' },
          '50%': { opacity: '0.8', transform: 'scale(1.05)' },
        },
        'shake': {
          '0%, 100%': { transform: 'translateX(0)' },
          '25%': { transform: 'translateX(-5px) rotate(-1deg)' },
          '75%': { transform: 'translateX(5px) rotate(1deg)' },
        },
        'flicker': {
          '0%, 100%': { opacity: '1' },
          '50%': { opacity: '0.95' },
        },
      },
    },
  },
  plugins: [],
}
