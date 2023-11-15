'strict';

var path = require('path');
var cwd = process.cwd();

const exportsMain = {
  entry: path.join(cwd, '${publicPath}/${entry}'),
  output: {
    path: path.join(cwd, '${publicPath}'),
    filename: '[name].js',
    publicPath: './${publicPath}'
  },
  module: {
    loaders: [{
      test: /\.js$/,
      loader: 'babel-loader',
      exclude: /node_modules/,
      include: cwd,
      query: {
        plugins: ['transform-decorators-legacy'],
        presets: ['env', 'stage-0', 'react']
      }
    }, {
        test: /\.css$/,
		use: [{
            loader: "style-loader"
        }, {
            loader: "css-loader"
        }],
        include: /node_modules/
     }]
  }
};

module.exports = exportsMain;
