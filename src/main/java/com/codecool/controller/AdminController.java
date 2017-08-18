package com.codecool.controller;
    import com.codecool.models.*;
    import com.codecool.models.enums.InverterBrandEnum;
    import com.codecool.models.forms.ConsumptionForm;
    import com.codecool.models.forms.DeviceForm;
    import com.codecool.repositories.InverterRepository;
    import com.codecool.repositories.OtherItemRepository;
    import com.codecool.repositories.SolarPanelRepository;
    import com.codecool.services.AdminService;
    import com.codecool.services.OfferService;
    import com.codecool.services.PdfService;
    import com.codecool.services.ValidationService;
    import com.codecool.services.email.EmailService;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import javax.servlet.http.HttpSession;
    import java.util.HashMap;
    import java.util.HashSet;
    import java.util.List;

@Slf4j
@Controller
public class AdminController {
    private OfferService offerService;
    private AdminService adminService;
    private InverterRepository inverterRepository;
    private SolarPanelRepository solarPanelRepository;
    private OtherItemRepository otherItemRepository;


    private final String CONSUMPTION = "consumption";
    private final String DEVICE = "deviceForm";
    private final String STEP = "step";
    private final String OFFER = "offer";
    private final String METRIC = "metric";
    private final String BRAND = "brand";
    private final String PHASE = "phase";

    @Autowired
    public AdminController(OfferService offerService, SolarPanelRepository solarPanelRepository,
                           InverterRepository inverterRepository, OtherItemRepository otherItemRepository,
                           AdminService adminService) {
        this.solarPanelRepository = solarPanelRepository;
        this.otherItemRepository = otherItemRepository;
        this.inverterRepository = inverterRepository;
        this.offerService = offerService;
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String getAdmin1() {
        return "redirect:/admin/fogyasztas";
    }

    @GetMapping("/admin/")
    public String getAdmin2() {
        return "redirect:/admin/fogyasztas";
    }

    @GetMapping("/admin/fogyasztas")
    public String getConsumptionData(Model model, HttpSession session) {
        ConsumptionForm consumptionForm = session.getAttribute(CONSUMPTION) == null ?
                new ConsumptionForm() : (ConsumptionForm) session.getAttribute(CONSUMPTION);

        session.setAttribute(DEVICE, null);
        session.setAttribute(OFFER, null);

        if (session.getAttribute(CONSUMPTION) != null) {
            model.addAttribute(METRIC, consumptionForm.getMetric());
        } else {
            model.addAttribute(METRIC, "Ft");
        }

        model.addAttribute(CONSUMPTION, consumptionForm);
        model.addAttribute(STEP, "admin1");
        return "admin";
    }

    @PostMapping("/admin/fogyasztas")
    public String postConsumptionData(@ModelAttribute ConsumptionForm consumption, HttpSession session) {
        session.setAttribute(CONSUMPTION, consumption);
        log.info("Consumption: " + consumption.getValue() + consumption.getMetric() + " No.Phase: " + consumption.getPhase());
        return "redirect:/admin/eszkozok";
    }

    @GetMapping("/admin/eszkozok")
    public String getOfferStep2(Model model, HttpSession session) {
        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);
        if (consumption == null) {
            log.info("Step1 is not done, redirecting to fogyasztas.");
            return "redirect:/admin/fogyasztas";
        }
        DeviceForm pAndIForm = session.getAttribute(DEVICE) == null ?
                new DeviceForm() : (DeviceForm) session.getAttribute(DEVICE);

        int calculatedConsumption = offerService.calculateConsumption(consumption);
        List<Inverter> inverterList = offerService.calculateInverterList(calculatedConsumption, consumption.getPhase());
        List<LineItem> solarPanelLineItems = offerService.getSolarPanelListAsLineItems(consumption);

        model.addAttribute(DEVICE, pAndIForm);
        model.addAttribute("solarPanelLineItems", solarPanelLineItems);
        model.addAttribute("inverterList", inverterList);
        model.addAttribute(STEP, "admin2");
        return "admin";
    }

    @PostMapping("/admin/eszkozok")
    public String postOfferStep2(@ModelAttribute DeviceForm device, HttpSession session) {
        session.setAttribute(DEVICE, device);
        log.info("Devices: InvID: " + device.getInverterId() + "  PanelId: " + device.getPanelId());
        return "redirect:/admin/szerkeszto";
    }

    @GetMapping("admin/szerkeszto")
    public String getOfferStep3(Model model, HttpSession session) {
        DeviceForm deviceForm = (DeviceForm) session.getAttribute(DEVICE);

        if (deviceForm == null || !deviceForm.isValid()) {
            log.info("Step2 is not done, redirecting to /admin/eszkozok.");
            return "redirect:/admin/eszkozok";
        }
        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);
        Offer offer = session.getAttribute(OFFER) != null ?
                (Offer) session.getAttribute(OFFER) : offerService.createFromFormData(consumption, deviceForm);

        session.setAttribute(OFFER, offer);

        model.addAttribute(OFFER, offer);
        model.addAttribute(STEP, "admin3");
        return "admin";
    }

    /*   {
        brand: String, (Uppercase letters)
        phase: String, (1 or 3)
    } */
    @PostMapping("admin/tetel/inverterek")
    @ResponseBody
    public ResponseEntity<List<Inverter>> filterItemType(@RequestBody HashMap<String, String> data) {
        InverterBrandEnum brand = InverterBrandEnum.valueOf(data.get(BRAND));
        int phase = Integer.valueOf(data.get(PHASE));

        List<Inverter> filteredInverters = inverterRepository.findByBrandAndPhase(brand, phase);

        return new ResponseEntity<>(filteredInverters, HttpStatus.OK);
    }

    @PostMapping("admin/tetel/panelek")
    @ResponseBody
    public ResponseEntity<List<SolarPanel>> getPanels() {
        List<SolarPanel> solarPanels = solarPanelRepository.findAll();
        return new ResponseEntity<>(solarPanels, HttpStatus.OK);
    }

    /*{
        quantity: String, (the updated quantity)
        id: String,
    } */
    @PostMapping("admin/tetel/mennyisegvaltoztatas")
    @ResponseBody
    public ResponseEntity<Offer> updateQuantity(@RequestBody HashMap<String, String> data, HttpSession session) {

        Integer lineItemId = Integer.valueOf(data.get("id"));
        double quantity = Double.valueOf(data.get("quantity"));
        Offer offer = (Offer) session.getAttribute(OFFER);

        log.info(String.format("lineItemId: %s quantity: %s", lineItemId, quantity));

        LineItem lineItem = offer.getLineItem(lineItemId);
        lineItem.setQuantity(quantity);

        offer.updateLineItem(lineItem);

        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PostMapping("admin/tetel/torles")
    @ResponseBody
    public ResponseEntity<Offer> deleteLineItem(@RequestBody HashMap<String, String> data, HttpSession session) {

        Integer lineItemId = Integer.valueOf(data.get("id"));
        log.info(String.format("lineItemId: %s", lineItemId));

        Offer offer = (Offer) session.getAttribute(OFFER);
        offer.removeLineItem(lineItemId);

        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    /*{
        type: String, one from: ["panel", "inverter", "other"]
        brand: String, UPPERCASE // Only in case of type inverter, must be provided
    }*/
    @PostMapping("admin/tetel/listazas")
    @ResponseBody
    public ResponseEntity<List> getList(@RequestBody HashMap<String, String> data) {
        String type = data.get("type");
        String brand = null;
        if(type.equals("inverter")){
            brand = data.get("brand");
        }
        return new ResponseEntity<>(adminService.getListOfItems(type, brand), HttpStatus.OK);
    }

    /*{
        type: String, one from: ["panel", "inverter", "other"],
        itemId: String
    }*/
    @PostMapping("admin/tetel/uj")
    @ResponseBody
    public ResponseEntity<Offer> addNewItem(@RequestBody HashMap<String, String> data, HttpSession session) {

        Integer itemId = Integer.valueOf(data.get("itemId"));
        String type = data.get("type");
        log.info(String.format("New Item's itemId: %s, type: %s", itemId, type));

        Offer offer = (Offer) session.getAttribute(OFFER);
        if (!offerService.containsItem(offer, itemId, type)) {
            LineItem newItem = offerService.getLineItemFromItemIdAndType(itemId, type);
            offer.addLineItem(newItem);
            offer.sortLineItems();
        }

        offer.printLineItems();
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }
}