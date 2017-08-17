
$(document).on('ready', function () {var quantityURL = 'tetel/mennyisegvaltoztatas';
    var addURL = 'tetel/uj';
    var getListURL = 'tetel/listazas';
    var deleteURL = 'tetel/torles';

    $('.plus').on('click', function(){
        var lineItemId = $(this).attr('data');
        var thisInput = $('#quantity' + lineItemId);
        var newQuantity = Number(thisInput.val()) + 1;
        console.log('thisInput: ');
        console.log(thisInput);


        console.log('thisInputVal: ' + newQuantity);
        var data = JSON.stringify({
            "id": lineItemId,
            "quantity" : newQuantity
        });
        console.log('Data: ' + data);
        var callback = function(response) {reRenderTable(response);};
        doAJAX(quantityURL, data, callback)
    });

    $('.minus').on('click', function(){
        var lineItemId = $(this).attr('data');
        var thisInput = $('#quantity' + lineItemId);
        var newQuantity = Number(thisInput.val()) - 1;
        console.log('thisInput: ');
        console.log(thisInput);

        console.log('thisInputVal: ' + newQuantity);
        var data = JSON.stringify({
            "id": lineItemId,
            "quantity" : newQuantity
        });
        console.log('Data: ' + data);
        var callback = function (response) {reRenderTable(response);};
        doAJAX(quantityURL, data, callback)
    });

    $('.fa.fa-times.delete').on('click', function(){
        var id = $(this).attr('data');
        var data = JSON.stringify({
            id: id
        });
        var callback = function (response) {
            reRenderTable(response)
        };

        doAJAX(deleteURL, data, callback);
    });

    $('#categorySelect').on('change', function () {
        var value = $(this).val();
        var data = JSON.stringify({
            type: value
        });
        var callback = function (response) {
            fillItemSelect(response);
        };

        if(value == 'inverter'){
            $('#brandSelect').attr('disabled', false).val('initial');
            $('#itemSelect').attr('disabled', true);
        } else if(value == 'panel'){
            doAJAX(getListURL, data, callback);
            $('#brandSelect').attr('disabled', true).val('warning');

        } else if (value == 'other'){
            doAJAX(getListURL, data, callback);
            $('#brandSelect').attr('disabled', true).val('warning');

        }
    });

    $('#brandSelect').on('change', function () {
        var type = $('#categorySelect').val();
        var brand = $(this).val();
        var data = JSON.stringify({
            type: type,
            brand: brand
        });
        var callback = function (response) {
            fillItemSelect(response);
        };
        doAJAX(getListURL, data, callback)
    });

    $('#itemSelect').on('change', function () {
        if($(this).val() != 'inital'){
            $('#submitAddItem').attr('disabled', false);
        }
    });

    $('#submitAddItem').on('click', function (event) {
        var callback = function (response) {
            resetAddForm();
            reRenderTable(response);
        };
        var data = JSON.stringify({
            itemId: $('#itemSelect').val(),
            type: $('#categorySelect').val()
        });
        doAJAX(addURL, data, callback);

    })});

var attachEventListeners = function () {
    var quantityURL = 'tetel/mennyisegvaltoztatas';
    var addURL = 'tetel/uj';
    var getListURL = 'tetel/listazas';
    var deleteURL = 'tetel/torles';

    $('.plus').on('click', function(){
        var lineItemId = $(this).attr('data');
        var thisInput = $('#quantity' + lineItemId);
        var newQuantity = Number(thisInput.val()) + 1;
        console.log('thisInput: ');
        console.log(thisInput);


        console.log('thisInputVal: ' + newQuantity);
        var data = JSON.stringify({
            "id": lineItemId,
            "quantity" : newQuantity
        });
        console.log('Data: ' + data);
        var callback = function(response) {reRenderTable(response);};
        doAJAX(quantityURL, data, callback)
    });

    $('.minus').on('click', function(){
        var lineItemId = $(this).attr('data');
        var thisInput = $('#quantity' + lineItemId);
        var newQuantity = Number(thisInput.val()) - 1;
        console.log('thisInput: ');
        console.log(thisInput);

        console.log('thisInputVal: ' + newQuantity);
        var data = JSON.stringify({
            "id": lineItemId,
            "quantity" : newQuantity
        });
        console.log('Data: ' + data);
        var callback = function (response) {reRenderTable(response);};
        doAJAX(quantityURL, data, callback)
    });

    $('.fa.fa-times.delete').on('click', function(){
        var id = $(this).attr('data');
        var data = JSON.stringify({
            id: id
        });
        var callback = function (response) {
            reRenderTable(response)
        };

        doAJAX(deleteURL, data, callback);
    });

    // $('#categorySelect').on('change', function () {
    //     var value = $(this).val();
    //     var data = JSON.stringify({
    //         type: value
    //     });
    //     var callback = function (response) {
    //         fillItemSelect(response);
    //     };
    //
    //     if(value == 'inverter'){
    //         $('#brandSelect').attr('disabled', false).val('initial');
    //         $('#itemSelect').attr('disabled', true);
    //     } else if(value == 'panel'){
    //         doAJAX(getListURL, data, callback);
    //         $('#brandSelect').attr('disabled', true).val('warning');
    //
    //     } else if (value == 'other'){
    //         doAJAX(getListURL, data, callback);
    //         $('#brandSelect').attr('disabled', true).val('warning');
    //
    //     }
    // });
    //
    // $('#brandSelect').on('change', function () {
    //     var type = $('#categorySelect').val();
    //     var brand = $(this).val();
    //     var data = JSON.stringify({
    //         type: type,
    //         brand: brand
    //     });
    //     var callback = function (response) {
    //         fillItemSelect(response);
    //     };
    //     doAJAX(getListURL, data, callback)
    // });
    //
    // $('#itemSelect').on('change', function () {
    //     if($(this).val() != 'inital'){
    //         $('#submitAddItem').attr('disabled', false);
    //     }
    // });
    //
    // $('#submitAddItem').on('click', function (event) {
    //     var callback = function (response) {
    //         resetAddForm();
    //         reRenderTable(response);
    //     };
    //     var data = JSON.stringify({
    //         itemId: $('#itemSelect').val(),
    //         type: $('#categorySelect').val()
    //     });
    //     doAJAX(addURL, data, callback);
    //
    // })

};

var reRenderTable = function(responseOffer){
    var root = $('.root');
    var header = $('.dynamic-header');

    root.html("");
    var lineItems = responseOffer.lineItems;
    for (var i = 0; i < lineItems.length; i++){
        root.append(createRow(lineItems[i]));
    }
    header.html("");
    header.append(createTotal(responseOffer));
    attachEventListeners();
};

var fillItemSelect = function (itemArray) {
    var select =  $('#itemSelect');
    select.html('');
    select.append(
        '<option selected="selected" hidden="hidden" value="initial"><strong>Nincs kiválasztva</strong></option>'
    );
    for(var i = 0; i< itemArray.length; i++){
        var newOption;
        if($('#categorySelect').val() == 'inverter') {
            var name = itemArray[i].name + (' (fázis: ' + itemArray[i].phase + ')');
            newOption = new Option(name, itemArray[i].id);
        } else {
            newOption = new Option(itemArray[i].name, itemArray[i].id);
        }
        select.append(newOption);
    }

    select.attr('disabled', false);
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

var resetAddForm = function () {
    $('#categorySelect').val('initial');
    $('#brandSelect').val('initial').attr('disabled', true);
    $('#itemSelect').val('initial').attr('disabled', true);
};
