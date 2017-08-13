
$(document).on('ready', function () {attachEventListeners()});

var attachEventListeners = function () {

    $('.plus').on('click', function(){
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
        var callback = function(response) {reRenderTable(response);};
        doAJAX(URL, data, callback)
    });

    $('.minus').on('click', function(){
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
        var callback = function (response) {reRenderTable(response);};
        doAJAX(URL, data, callback)
    });

    $('.fa.fa-times.delete').on('click', function(){
        var URL = 'tetel/torles';
        var id = $(this).attr('data');
        var data = JSON.stringify({
            id: id
        });
        var callback = function (response) {
            reRenderTable(response)
        };

        doAJAX(URL, data, callback);
    })

};

var reRenderTable = function(responseOffer){
    var root = $('.root');
    var header = $('.dynamic-header');
    console.log("root: ");
    console.log(root);
    console.log('responseOffer: ');
    console.log(responseOffer);

    root.html("");
    var lineItems = responseOffer.lineItems;
    for (var i = 0; i < lineItems.length; i++){
        root.append(createRow(lineItems[i]));
    }
    header.html("");
    header.append(createTotal(responseOffer));
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
                '<td class="padding_all"><i class="fa fa-times delete" data="' + item.id + '"></i></td>' +
            '</tr>'
};

var createTotal = function (offer) {
    var nettoTotal = accounting.formatNumber(offer.nettoTotalPrice, {precision : 0, thousand : " "});
    return '<tr>' +
        '<th colspan="3"><span></span></th>' +
        '<th class="text-right"><span >Nettó összeg:</span></th>' +
        '<th>' +
        '<span style="white-space: nowrap">' + nettoTotal +' Ft' + '</span>'+
        '</th><th><span></span></th></tr>'+
        '<tr>'+
        '<th colspan="3"><span></span></th>'+
        '<th class="text-right"><span>Bruttó összeg:</span></th>'+
        '<th>'+
            //TODO:Change this to grossTotal
        '<span style="white-space: nowrap">' + nettoTotal +' Ft' + '</span>'+
        '</th> <th><span></span></th> </tr>'
};