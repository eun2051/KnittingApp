module.exports = {
  content: [
    "./frontend/src/**/*.{js,jsx,ts,tsx}",
    "./src/**/*.{js,jsx,ts,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        'yarn-pink': '#FFC1E3',      // 부드러운 핑크
        'yarn-lavender': '#E0BBE4',  // 연한 라벤더
        'yarn-mint': '#C7FFED',      // 민트
        'yarn-peach': '#FFD4B2',     // 복숭아색
        'yarn-sky': '#C9E4FF',       // 하늘색
        'yarn-cream': '#FFF5E9',     // 크림색
      },
      fontFamily: {
        'cute': ['"Noto Sans KR"', 'sans-serif'],
      },
      boxShadow: {
        'cute': '0 4px 20px rgba(255, 193, 227, 0.3)',
        'cute-hover': '0 6px 30px rgba(255, 193, 227, 0.4)',
      },
    },
  },
  plugins: [],
};
