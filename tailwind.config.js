module.exports = {
    content: ['./layouts/**/*.{html,js}', './content/**/*.{html,md}'], // Pastikan mencakup file Hugo Anda
    theme: {
      extend: {
        screens: {
          'xs': '480px', // Tambahkan breakpoint baru
        },
      },
    },
    plugins: [],
  };
