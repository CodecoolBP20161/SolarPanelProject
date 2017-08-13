
$(document).on('ready', function () {


    $('.plus').on('click', function(event){
        var lineItemId = $(this).attr('data');
        var thisInput = $('#quantity' + lineItemId);
        var newQuantity = Number(thisInput.val()) + 1;
        console.log('thisInput: ');
        console.log(thisInput);
        var URL = 'tetel/mennyisegvaltoztatas';

        console.log('thisInputVal: ' + newQuantity);
        var data = JSON.stringify({
            "id": lineItemId,
            "quantity" : newQuantity
        });
        console.log('Data: ' + data);
        var callback = function (response) {changeQuantity(event, response);};
        doAJAX(URL, data, callback)
    });

    $('.minus').on('click', function(event){
        var lineItemId = $(this).attr('data');
        var thisInput = $('#quantity' + lineItemId);
        var newQuantity = Number(thisInput.val()) - 1;
        console.log('thisInput: ');
        console.log(thisInput);
        var URL = 'tetel/mennyisegvaltoztatas';

        console.log('thisInputVal: ' + newQuantity);
        var data = JSON.stringify({
            "id": lineItemId,
            "quantity" : newQuantity
        });
        console.log('Data: ' + data);
        var callback = function (response) {changeQuantity(event, response);};
        doAJAX(URL, data, callback)
    });



});
var reRenderTable = function(responseOffer){
    var root = $('.root');
    console.log("root: ");
    console.log(root);
    console.log('responseOffer: ');
    console.log(responseOffer);

    root.html("");
    var lineItems = responseOffer.lineItems;
    for (var i = 0; i < lineItems.length; i++){
        root.append(createRow(lineItems[i]));

    }






};

var changeQuantity = function(event, response){
    reRenderTable(response);
    attachEventListeners();
};
var createRow =function (item) {

    // Don't mind this, just formatting shit

    var price = accounting.formatNumber(item.price, {precision : 0, thousand : " "});
    var quantity = accounting.formatNumber(item.quantity, {precision : 0, thousand : " "});
    var total = accounting.formatNumber(item.total, {precision : 0, thousand : " "});
    //---------------------------------------------------------------------------------------------

    return '<tr>' +
                '<td class="padding_all"><p>' + item.name + '</p></td>' +
                '<td class="padding_all"><p>' + item.description + '</p></td>' +
                '<td class="padding_all"><p style="white-space: nowrap">' + price + ' Ft</p></td >' +
                '<td class="padding_all quantity">' +
                    '<div class="cart_amount_wrap">' +
                        '<div class="product-regulator">'+
                            '<div class ="input-group" style="white-space: nowrap">' +
                                '<button class="minus" type="button" data="' + item.id + '"><i class="fa fa-minus" ></i></button>' +
                                '<input class="output" id="quantity' + item.id + '" type = "text" value="' + quantity + '"' +
                                    'onkeypress="return  event.charCode == 46 || (event.charCode >= 48 && event.charCode <= 57)">' +
                                '<button class ="plus" data="' + item.id + '" type="button"><i class="fa fa-plus"></i></button>' +
                '</div></div></div></td>' +
                '<td class="padding_all"><p style="white-space: nowrap" class="total">' + total + ' Ft</p></td>' +
                '<td class="padding_all"><a href = "#"><i class="fa fa-times"></i></a></td>' +
            '</tr>'
};

var attachEventListeners = function () {

    $('.plus').on('click', function(event){
        var lineItemId = $(this).attr('data');
        var thisInput = $('#quantity' + lineItemId);
        var newQuantity = Number(thisInput.val()) + 1;
        console.log('thisInput: ');
        console.log(thisInput);
        var URL = 'tetel/mennyisegvaltoztatas';

        console.log('thisInputVal: ' + newQuantity);
        var data = JSON.stringify({
            "id": lineItemId,
            "quantity" : newQuantity
        });
        console.log('Data: ' + data);
        var callback = function (response) {changeQuantity(event, response);};
        doAJAX(URL, data, callback)
    });

    $('.minus').on('click', function(event){
        var lineItemId = $(this).attr('data');
        var thisInput = $('#quantity' + lineItemId);
        var newQuantity = Number(thisInput.val()) - 1;
        console.log('thisInput: ');
        console.log(thisInput);
        var URL = 'tetel/mennyisegvaltoztatas';

        console.log('thisInputVal: ' + newQuantity);
        var data = JSON.stringify({
            "id": lineItemId,
            "quantity" : newQuantity
        });
        console.log('Data: ' + data);
        var callback = function (response) {changeQuantity(event, response);};
        doAJAX(URL, data, callback)
    });
    
};

