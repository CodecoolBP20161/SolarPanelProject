var pdf = require('html-pdf');
var appmodule = require('./server');
var nunjucks = require("nunjucks");
var path = require("path");
var templateRoute = path.resolve(__dirname, 'templates/offer_template.html');

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}


config = {
    "format": "Letter",        // allowed units: A3, A4, A5, Legal, Letter, Tabloid
    "orientation": "portrait"
    };

exports.printpdf1 = function (req, res) {
    var offer = req.body;


    var renderedHtml =  appmodule.env.render(templateRoute, {
        numberWithCommas: numberWithCommas,
        offer: offer
    });

    pdf.create(renderedHtml).toBuffer(function(err, buffer){
        if (err) console.log(err);
        else {
            console.log('A PDF buffer has been created');
            res.send(buffer);
        }
    });
};
