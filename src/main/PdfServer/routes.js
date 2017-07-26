var pdf = require('html-pdf');
var appmodule = require('./server');
var nunjucks = require("nunjucks");
var path = require("path");
var fs = require('fs');
var templateRoute = path.resolve(__dirname, 'templates/');
var cssRoute = path.resolve(__dirname, 'templates/assets/css/');
var offer_template = 'offer_template.html';
var offer_template_v1 = 'offer_template_v1.html';

// Style files loaded
var bootstrap = fs.readFileSync(cssRoute + 'bootstrap.min.css' , 'utf-8');
var flatIcon = fs.readFileSync(cssRoute + 'flaticon.css' , 'utf-8');
var fontAwesome = fs.readFileSync(cssRoute + 'font-awesome.css' , 'utf-8');
var style = fs.readFileSync(cssRoute + 'style.css' , 'utf-8');
var revStyle = fs.readFileSync(cssRoute + 'rev-style.css' , 'utf-8');
var themeColor = fs.readFileSync(cssRoute + 'theme-color/bootstrap.css' , 'utf-8');


var styles = [
        bootstrap,
        flatIcon,
        fontAwesome,
        style,
        themeColor,
        revStyle
    ]
    ;

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}


config = {
    "format": "Letter",        // allowed units: A3, A4, A5, Legal, Letter, Tabloid
    "orientation": "portrait"
    };

exports.printpdf1 = function (req, res) {
    var offer = req.body;


    var renderedHtml =  appmodule.env.render(templateRoute + offer_template, {
        numberWithCommas: numberWithCommas,
        offer: offer,
        styles: styles
    });

    pdf.create(renderedHtml).toBuffer(function(err, buffer){
        if (err) console.log(err);
        else {
            console.log('A PDF buffer has been created');
            res.send(buffer);
        }
    });
};
