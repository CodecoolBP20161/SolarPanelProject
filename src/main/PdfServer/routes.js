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
    "orientation": "portrait",
    // Rendering options
    "base": "file://localhost:1350/static" // Base path that's used to load files (images, css, js) when they aren't referenced using a host
    };

exports.printpdf1 = function (req, res) {
    var offer = req.body;


    var renderedHtml =  appmodule.env.render(templateRoute, {
        numberWithCommas: numberWithCommas,
        offer: offer
    });
    // This one automatically downloads the pdf
    // pdf.create(renderedHtml).toBuffer(function(err, buffer){
    //     console.log('This is a buffer:', Buffer.isBuffer(buffer));
    //     res.download(buffer);
    //
    // });

    // This one returns the pdf in your browser
    // pdf.create(renderedHtml).toStream(function(err, stream){
    //     console.log(stream);
    //     stream.pipe(res);
    // });

    // This one saves it into a file
    pdf.create(renderedHtml, config).toFile('./offer_' + offer.id + '.pdf', function(err, res) {
        if (err) return console.log(err);
        else console.log("Your PDF has been generated!");
    });
    res.json("ok");
};
