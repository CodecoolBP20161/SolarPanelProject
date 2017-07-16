var express = require('express');
var http = require('http');
var path = require ('path');
var fs = require('fs');
var bodyParser = require('body-parser');
var pdf = require('html-pdf');
var nunjucks = require('nunjucks');
var html = fs.readFileSync('./templates/offer_template.html', 'utf8');



var routes = require('./routes');
var app = express();

//Nunjucks is a product from Mozilla and we are using it as a template engine.
exports.env = nunjucks.configure('templates', {
    autoescape: true,
    express: app
});

var port = process.env.port || 1350;
app.use(express.static(__dirname));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use('/static', express.static('static'));



// Add headers for CORS
app.use(function (req, res, next) {

    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', '*');

    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');

    // Request headers you wish to allow
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');

    // Set to true if you need the website to include cookies in the requests sent
    // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);

    // Pass to next layer of middleware
    next();
});

app.post('/api/printpdf1', routes.printpdf1);

app.listen(port);
console.log('node server for html-pdf is running on port ' + port);
