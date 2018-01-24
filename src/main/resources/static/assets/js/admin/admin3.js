
$(document).on('ready', function () {
    var fullURL = window.location.href;
    var paramName = 'key';
    var consumtionID = getParameterByName(paramName, fullURL);
    console.log(consumtionID);
    var addURL = 'tetel/uj?key='.concat(consumtionID);
    var getListURL = 'tetel/listazas?key='.concat(consumtionID);
    var isCSRFNeeded = true;
    var addCustomURL = 'tetel/egyeni?key='.concat(consumtionID);

    attachEventListeners();



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
            doAJAX(getListURL, data, callback, isCSRFNeeded);
            $('#brandSelect').attr('disabled', true).val('warning');

        } else if (value == 'other'){
            doAJAX(getListURL, data, callback, isCSRFNeeded);
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
        doAJAX(getListURL, data, callback, isCSRFNeeded)
    });

    $('#itemSelect').on('change', function () {
        if($(this).val() != 'inital'){
            $('#submitAddItem').attr('disabled', false);
        }
    });

    $('#submitAddItem').on('click', function () {
        var callback = function (response) {
            resetAddForm();
            reRenderTable(response);
        };
        var data = JSON.stringify({
            itemId: $('#itemSelect').val(),
            type: $('#categorySelect').val()
        });
        doAJAX(addURL, data, callback, isCSRFNeeded);

    });



    $('#customPrice').on('change keyup', function () {
        var formattedInput = formatPrice($(this).val());
        var futureInput = isNumberInputFieldValueValid(formattedInput) ? formattedInput : '' ;
        $(this).val((futureInput));
    });

    $('#submitCustomItem').on('click', function () {
        var name = $('#customName').val();
        var price = unFormatPrice($('#customPrice').val());
        var description = $('#customDescription').val();
        var priority = $('#prioritySelect').val();
        var type = $('#typeSelect').val();

        var data = JSON.stringify({
            name: name,
            price: price,
            description: description,
            priority: priority,
            type: type
        });
        var callback = function (response) {
            resetCustomForm();
            reRenderTable(response);
        };

        doAJAX(addCustomURL, data, callback, isCSRFNeeded);
    });

    $('#customPrice').add($('#customName')).on('change, keyup', function () {
        if( $('#customName').val() !== '' && $('#customPrice').val() !== ''){
            console.log("valid");
            $('#submitCustomItem').attr('disabled', false);
        }
        else {
            console.log("invalid");
            $('#submitCustomItem').attr('disabled', true);
        }
    })

});

var attachEventListeners = function () {
    var fullURL = window.location.href;
    var paramName = 'key';
    var consumtionID = getParameterByName(paramName, fullURL);

    console.log(consumtionID);
    var quantityURL = 'tetel/mennyisegvaltoztatas?key='.concat(consumtionID);
    var priceURL = 'tetel/egysegarvaltoztatas?key='.concat(consumtionID);
    var previousQuantity;
    var previousPrice;
    var deleteURL = 'tetel/torles?key='.concat(consumtionID);
    var isCSRFNeeded = true;

    $('.plus').on('click', function(){
        var lineItemId = $(this).attr('data');
        var thisInput = $('#quantity' + lineItemId);
        var newQuantity = unFormatPrice(Number(thisInput.val())) + 1;
        var data = JSON.stringify({
            "id": lineItemId,
            "quantity" : newQuantity
        });
        var callback = function(response) {reRenderTable(response);};

        doAJAX(quantityURL, data, callback, isCSRFNeeded)
    });

    $('.minus').on('click', function(){
        var lineItemId = $(this).attr('data');
        var thisInput = $('#quantity' + lineItemId);
        var newQuantity =unFormatPrice(Number(thisInput.val())) - 1;
        var data = JSON.stringify({
            "id": lineItemId,
            "quantity" : newQuantity
        });
        var callback = function (response) {reRenderTable(response);};

        doAJAX(quantityURL, data, callback, isCSRFNeeded)
    });

    $('.fa.fa-times.delete').on('click', function(){
        var id = $(this).attr('data');
        var data = JSON.stringify({
            id: id
        });
        var callback = function (response) {
            reRenderTable(response)
        };

        doAJAX(deleteURL, data, callback, isCSRFNeeded);
    });

    $('.output')
        .on('focusout', function () {
            var thisInput = $(this);
            if (thisInput.val() == '') thisInput.val(previousQuantity);
            else{
                var idString = thisInput.attr('id');
                var lineItemId = idString.replace('quantity', '');
                var newQuantity = thisInput.val();
                var data = JSON.stringify({
                    "id": lineItemId,
                    "quantity" : newQuantity
                });
                var callback = function (response) {reRenderTable(response);};

                doAJAX(quantityURL, data, callback, isCSRFNeeded)
            }
    })  .on('focusin', function () {
            previousQuantity = $(this).val();
    });

    $('.priceInput')
        .on('focusout', function () {
            var thisInput = $(this);
            if (thisInput.val() == '') thisInput.val(previousPrice);
            else {
                var lineItemId = thisInput.attr('id');
                var newPrice = unFormatPrice(thisInput.val());
                var data = JSON.stringify({
                    "id": lineItemId,
                    "price": newPrice
                });
                var callback = function (response) {
                    reRenderTable(response);
                };

                doAJAX(priceURL, data, callback, isCSRFNeeded)
            }
        })
        .on('focusin', function () {
                previousPrice = $(this).val();
        })
        .on('ready change keyup', function () {
            $(this).val(formatPrice( $(this).val()));
    });
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

    var price = formatPrice(item.price);
    var quantity = formatPrice(item.quantity);
    var total = formatPrice(item.total);

    return '<tr>' +
        '<td class="padding_all"><p>' + item.name + '</p></td>' +
        '<td class="padding_all"><p>' + item.description + '</p></td>' +
        '<td class="padding_all">' +
        '<div class="romana_check_out_form">' +
        '<div class="row"><div class="check_form_left common_input"><div class="select_option_one">' +
        '<input class="input-lg form-full priceInput" style="white-space: nowrap" id="'+ item.id +'" value="' + price + '">' +
        '</div></div></div></div>' +
        '</td >' +
        '<td class="padding_all quantity">' +
        '<div class="cart_amount_wrap">' +
        '<div class="product-regulator">'+
        '<div class ="input-group" style="white-space: nowrap">' +
        '<button class="minus" type="button" data="' + item.id + '"><i class="fa fa-minus" ></i></button>' +
        '<input class="output" id="quantity' + item.id + '" type = "text" value="' + quantity + '"' +
        ' onkeypress="return  event.charCode == 46 || (event.charCode >= 48 && event.charCode <= 57)">' +
        '<button class ="plus" data="' + item.id + '" type="button"><i class="fa fa-plus"></i></button>' +
        '</div></div></div></td>' +
        '<td class="padding_all"><p style="white-space: nowrap" class="total">' + total + ' Ft</p></td>' +
        '<td class="padding_all"><i class="fa fa-times delete" data="' + item.id + '"></i></td>' +
        '</tr>'
};

var createTotal = function (offer) {
    var nettoTotalPrice = formatPrice(offer.nettoTotalPrice);
    var nettoServiceTotalPrice = formatPrice(offer.nettoServiceTotalPrice);
    var nettoTotal = formatPrice(offer.nettoTotalPrice + offer.nettoServiceTotalPrice);

    return '<tr>' +
        '<th class="text-left"><span style="white-space: nowrap"> Nettó áru:</span></th>' +
        ' <th>' +
        '<span style="white-space: nowrap">' + nettoTotalPrice +' Ft' + '</span>' +
        '</th>' +

        '<th class="text-left"><span style="white-space: nowrap"> Nettó szolgáltatás:</span></th>' +
        ' <th>' +
        '<span style="white-space: nowrap">' + nettoServiceTotalPrice +' Ft' + '</span>' +
        '</th>' +

        '<th class="text-left"><span style="white-space: nowrap"> Nettó összesen:</span></th>' +
        ' <th>' +
        '<span style="white-space: nowrap">' + nettoTotal +' Ft' + '</span>' +
        '</th>' +
        '</tr>' +
        '<tr>'
};

var resetAddForm = function () {
    $('#categorySelect').val('initial');
    $('#brandSelect').val('initial').attr('disabled', true);
    $('#itemSelect').val('initial').attr('disabled', true);
};

var resetCustomForm = function () {
    $('#customName, #customPrice, #customDescription').val('');
    $('#typeSelect').val('Service');
    $('#prioritySelect').val(1);
};

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
