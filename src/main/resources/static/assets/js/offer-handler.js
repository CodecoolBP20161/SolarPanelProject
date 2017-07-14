$(document).ready(function() {

    // Event Listeners for form one( annual electric consumption or monthly electric bill selection)

    var metricSelect = $('#stepOneSelect');
    var metricInput = $('#sub');
    if (metricInput.val() == "0.0") metricInput.val("");

    metricSelect.on('change', function () {
        $('#metricHeader').html(metricSelect.find('option:selected').val());
    });

    // Event Listeners for form two (Solar panel and inverter selection)

    var inverterInput = $('#inverterInput');
    var panelInput = $('#panelInput');
    if(inverterInput.val() == undefined){
        inverterInput.val('initial');
        panelInput.val('initial');
    }

    var formSubmitbutton = $('#submitPanelAndInverter');
    var selectedInverter = null;
    var selectedPanel = null;

    // Toggles class on clicked inverter and set hidden input's value to clicked inverters id
    $('.inverter').on('click', function (event) {
        if (selectedInverter){
            selectedInverter.toggleClass('active');
        }
        selectedInverter = $(event.target.closest('div[class^="testimonial-block cyan-background inverter"]'));
        selectedInverter.toggleClass('active');
        inverterInput.val(selectedInverter.attr('data')).change();
    });

    // Toggles class on clicked panel and set hidden input's value to clicked panel id
    $('.panel').on('click', function (event) {

        if (selectedPanel){
            selectedPanel.toggleClass('active');
        }
        selectedPanel = $(event.target.closest('div[class^="testimonial-block cyan-background panel"]'));
        selectedPanel.toggleClass('active');
        panelInput.val(selectedPanel.attr('data')).change()
    });

    // When an input is changed, it checks whether there are still input with initial value,
    // and sets the buttons disabled property accordingly
    inverterInput.add(panelInput).on('change', function () {
        if (inverterInput.val() !== 'initial' && panelInput.val() !== 'initial')
            formSubmitbutton.prop('disabled', false);
        })

}
);

