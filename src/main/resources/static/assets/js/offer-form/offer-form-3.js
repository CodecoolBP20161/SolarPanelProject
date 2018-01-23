
/*$(function () {
    $("button[name='submitEmail']").click(function () {
        if ($("#check_email").val() !="") {
            $("#check_email").val("");
            $("button[name='submitEmail']").prop("disabled", false);

        }
    });
});*/

/*
$('submitEmail').click(function(){
    $('input[name="check_email"]').val('');
});
*/




function validateForm() {
    var x = document.forms["submitEmailForm"]["emailInput"].value;
    if (x != "") {

        document.getElementById("submitEmail").disabled = true;
    }
}
