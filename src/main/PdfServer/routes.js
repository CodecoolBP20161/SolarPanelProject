var pdf = require('html-pdf');
var appmodule = require('./server');
var nunjucks = require("nunjucks");
var path = require("path");
var fs = require('fs');
var accounting = require('accounting');
var templateRoute = path.resolve(__dirname, 'templates/');
var templateNaposOldal = '/napos-oldal_template.html';
var templateSolarProvider = '/solar-provider_template.html';
var templateStabilInvest = '/stabil-invest_template.html';

config = {
    "format": "A4",        // allowed units: A3, A4, A5, Legal, Letter, Tabloid
    "orientation": "portrait",
    "border": "0",
    // Rendering options
    "base": "file://http://52.15.84.238:1350/templates/assets/" // Base path that's used to load files (images, css, js) when they aren't referenced using a host
};

exports.printpdf1 = function (req, res) {
    var offer = req.body;
    var htmlFileName;

    console.log(offer.company);

    if(offer.company == 'StabilInvest') htmlFileName = templateStabilInvest;
    else if(offer.company == 'SolarProvider') htmlFileName = templateSolarProvider;
    else htmlFileName = templateNaposOldal;

    var renderedHtml = appmodule.env.render(templateRoute + htmlFileName, {formatNumber: accounting.formatNumber, offer: offer});

    pdf.create(renderedHtml, config).toBuffer(function (err, buffer) {
        if (err) console.log(err);
        else {
            console.log('A PDF buffer has been created');
            res.send(buffer);
        }
    });
};
