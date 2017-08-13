$(document).on('ready', function(){
    // FORM PAGE 2

    var inverterInput = $('#inverterInput');
    var panelInput = $('#panelInput');
    var formSubmitbutton = $('#submitPanelAndInverter');

    if (inverterInput.val() == "" || panelInput.val() == "") {
        var selectedInverter = null;
        var selectedPanel = null;
    } else {
        var inverterID = inverterInput.val();
        var panelID = panelInput.val();

        selectedInverter = $('div[class^="testimonial-block cyan-background inverter"][data="' + inverterID + '"]');
        selectedPanel =  $('div[class^="testimonial-block cyan-background panel"][data="' + panelID + '"]');

        selectedInverter.toggleClass('active');
        selectedPanel.toggleClass('active');
    }

    // Toggles class on clicked inverter and set hidden input's value to clicked inverters id
    $('.inverter').add($('div[class^="testimonial-block cyan-background inverter"]')).on('click', function (event) {
        if (selectedInverter != null) {
            selectedInverter.toggleClass('active');
        }
        if ($(event.target).attr('class') == "testimonial-block cyan-background inverter") selectedInverter = $(event.target);
        else selectedInverter = $(event.target.closest('div[class^="testimonial-block cyan-background inverter"]'));

        selectedInverter.toggleClass('active');
        inverterInput.val(selectedInverter.attr('data')).change();
    });

    // Toggles class on clicked panel and set hidden input's value to clicked panel id
    $('.panel').add($('div[class^="testimonial-block cyan-background panel"]')).on('click', function (event) {
        if (selectedPanel != null) {
            selectedPanel.toggleClass('active');
        }
        if ($(event.target).attr('class') == "testimonial-block cyan-background panel") selectedPanel = $(event.target);
        else selectedPanel = $(event.target.closest('div[class^="testimonial-block cyan-background panel"]'));

        selectedPanel.toggleClass('active');
        panelInput.val(selectedPanel.attr('data')).change()
    });

    // When an input is changed(panel or inverter), it checks whether there are still
    // input with initial value (if the other one is chosen as well),
    // and sets the buttons disabled property accordingly
    inverterInput.add(panelInput).on('ready change', function () {
        if (inverterInput.val() !== 'initial' && panelInput.val() !== 'initial')
            formSubmitbutton.prop('disabled', false);
    });
});
