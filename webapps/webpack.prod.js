const path = require('path');
const ESLintPlugin = require('eslint-webpack-plugin');
const { VueLoaderPlugin } = require('vue-loader');

const config = {
  context: path.resolve(__dirname, '.'),
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: [
          'babel-loader',
        ]
      },
      {
        test: /\.vue$/,
        use: [
          'vue-loader',
        ]
      }
    ]
  },
  plugins: [
    new ESLintPlugin({
      files: [
        './src/main/webapp/vue-app/*.js',
        './src/main/webapp/vue-app/*.vue',
        './src/main/webapp/vue-app/**/*.js',
        './src/main/webapp/vue-app/**/*.vue',
      ],
    }),
    new VueLoaderPlugin()
  ],
  entry: {
    ixbusTopBar: './src/main/webapp/vue-apps/ixbus-top-bar/main.js',
    ixbusNotification: './src/main/webapp/vue-apps/ixbus-notifications/main.js',

  },
  output: {
    path: path.join(__dirname, 'target/ixbus/'),
    filename: 'js/[name].bundle.js',
    libraryTarget: 'amd'
  },
  externals: {
    vue: 'Vue',
    vuetify: 'Vuetify',
    jquery: '$',
  },
};

module.exports = config;
