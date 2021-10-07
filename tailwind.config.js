module.exports = {
    purge: {
        content: [
            './src/main/resources/templates/*.html',
            './src/main/resources/templates/**/*.html',
        ]
    },
    darkMode: false, // or 'media' or 'class'
    theme: {
        extend: {},
    },
    variants: {
        extend: {},
    },
    plugins: [
        require('@tailwindcss/forms')
    ],
}
