module.exports = {
  content: [
    './src/main/resources/templates/**/*.html', // Path untuk file Thymeleaf
    './src/main/resources/static/**/*.js',      // Path untuk file JS (opsional)
  ],
  theme: {
    extend: {
      screens: {
        'xs': '420px', // Tambahkan breakpoint baru
        'xxs': '360px', // Tambahkan breakpoint baru
      },
    },
  },
  plugins: [],
};
