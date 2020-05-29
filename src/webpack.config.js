const path = require('path');
const TerserPlugin = require('terser-webpack-plugin');

module.exports = {
    mode: 'development',
    optimization: {
        minimize: false,
        minimizer: [new TerserPlugin()],
    },
    entry: './frontend/app.js',
    output: {
        path: path.resolve(__dirname, 'main/resources/static'),
        filename: 'app.js',
        publicPath: '/'
    },
    module: {
        rules: [
            {
                test: /.(jsx|js)$/,
                loader: 'babel-loader'
            },
            {
                test: /.s[ac]ss$/,
                use: [ 'style-loader', 'css-loader', 'sass-loader' ]
            }
        ]
    },
    devtool: 'source-map',
    devServer: {
        historyApiFallback: true,
        contentBase: path.resolve(__dirname, 'main/resources/static'),
        port: 3000,
        host: '0.0.0.0',
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true
            }
        }
    }
}