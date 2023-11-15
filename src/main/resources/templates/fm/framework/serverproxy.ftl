'use strict';

require('./project-config');

const path = require('path');
const webpack = require('webpack');
const webpackDevServer = require('webpack-dev-server');
const webpackConfig = require('./webpack.config.js');

const cwd = process.cwd();

const proxy = require('proxy-middleware');
const url = require('url');
const express = require('express');
const bodyParser = require('body-parser');
const fetch = require('isomorphic-fetch');

const expressPort = ${expressPort};
const webpackPort = ${webpackPort}

global.__basedir = __dirname;

const app = express();
app.use(bodyParser.json());
app.listen(expressPort);

const webpackServer = new webpackDevServer(webpack(webpackConfig), {
  contentBase: cwd,
  hot: true,
  quiet: false,
  noInfo: false,
  publicPath: '/src/',
  stats: { colors: true }
});
webpackServer.listen(webpackPort);

app.get('*', proxy(url.parse(`http://localhost:${webpackPort}`)));
