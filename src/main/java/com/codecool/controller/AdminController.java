
package com.codecool.controller;

    import com.codecool.models.*;
import com.codecool.models.enums.InverterBrandEnum;
import com.codecool.models.enums.ItemTypeEnum;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.DeviceForm;
import com.codecool.repositories.AdvertisingRepository;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.OtherItemRepository;
import com.codecool.repositories.SolarPanelRepository;
import com.codecool.services.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
public class AdminController {
    private OfferService offerService;
    private AdminService adminService;
    private PdfService pdfService;
    private ValidationService validationService;
    private InverterRepository inverterRepository;
    private SolarPanelRepository solarPanelRepository;
    private OtherItemRepository otherItemRepository;
    private AdvertisingRepository advertisingRepository;
    private ConsumptionService consumptionService;


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
                           AdminService adminService, PdfService pdfService, ValidationService validationService,
                            AdvertisingRepository advertisingRepository, ConsumptionService consumptionService) {
        this.solarPanelRepository = solarPanelRepository;
        this.otherItemRepository = otherItemRepository;
        this.inverterRepository = inverterRepository;
        this.offerService = offerService;
        this.adminService = adminService;
        this.pdfService = pdfService;
        this.validationService = validationService;
        this.advertisingRepository = advertisingRepository;
        this.consumptionService = consumptionService;
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
    public String getConsumptionData(@RequestParam(value = "key", required = false) String consumptionIDD, Model model) {
        Consumption consumption = new Consumption();

        model.addAttribute(METRIC, "Ft");
        if (null != consumptionIDD){
            model.addAttribute("consumptionId", consumptionIDD);
        }

        model.addAttribute(CONSUMPTION, consumption);
        model.addAttribute(STEP, "admin1");
        return "admin";
    }

    @PostMapping("/admin/fogyasztas")
    public String postConsumptionData(@RequestParam(value = "key", required = false) String consumptionIDD, @ModelAttribute ConsumptionForm consumptionForm) {
        log.info("Consumption: " + consumptionForm.getValue() + consumptionForm.getMetric() + " No.Phase: " + consumptionForm.getPhase());

        Consumption consumption = new Consumption();

        String consumptionID = UUID.randomUUID().toString().replace("-","");

        consumption.setConsumptionID(consumptionID);
        consumption.setPhase(consumptionForm.getPhase());
        consumption.setValue(consumptionForm.getValue());
        consumption.setAdvertisement(consumptionForm.getAdvertisement());
        consumption.setMetric(consumptionForm.getMetric());

        consumptionService.saveConsuption(consumption);
        return "redirect:/admin/eszkozok?key="+consumption.getConsumptionID();
    }

    @GetMapping("/admin/eszkozok")
    public String getOfferStep2(@RequestParam(value = "key") String consumptionID, Model model) {
        Consumption consumption = consumptionService.getConsumptionByconsumptionID(consumptionID);

        if (consumptionService.getConsumptionByconsumptionID(consumptionID) == null) {
            log.info("Step1 is not done, redirecting to /ajanlat/1.");
            return "redirect:/ajanlat/1";
        }

        double calculatedConsumption = offerService.calculateConsumption(consumption);
        DeviceForm pAndIForm = new DeviceForm();

        List<Inverter> inverterList = offerService.calculateInverterList(consumption);
        List<LineItem> solarPanelLineItems = offerService.getSolarPanelListAsLineItems(consumption);

        model.addAttribute("consumptionId", consumptionID);

        model.addAttribute(DEVICE, pAndIForm);
        model.addAttribute("solarPanelLineItems", solarPanelLineItems);
        model.addAttribute("inverterList", inverterList);
        model.addAttribute(STEP, "admin2");
        return "admin";
    }

    @PostMapping("/admin/eszkozok")
    public String postOfferStep2(@RequestParam(value = "key") String consumptionID, @ModelAttribute DeviceForm deviceForm, HttpSession session) {
        Consumption consumption = consumptionService.getConsumptionByconsumptionID(consumptionID);

        consumption.setInverterId(deviceForm.getInverterId());
        consumption.setPanelId(deviceForm.getPanelId());
        consumptionService.saveConsuption(consumption);
        log.info("Devices: Inverter: " + consumption.getInverterId().toString() + "  Panel: " + consumption.getPanelId().toString());

        return "redirect:/admin/szerkeszto?key=" + consumptionID;
    }

    @GetMapping("admin/szerkeszto")
    public String getOfferStep3(@RequestParam(value="key") String consumptionID, Model model) {
        Consumption consumption = consumptionService.getConsumptionByconsumptionID(consumptionID);
        DeviceForm deviceForm = new DeviceForm(consumption.getInverterId(), consumption.getPanelId());


        if (consumptionService.getConsumptionByconsumptionID(consumptionID) == null || !deviceForm.isValid()) {
            log.info("Step1 is not done, redirecting to /ajanlat/1.");
            return "redirect:/admin/eszkozok?key=" + consumptionID;
        }

        consumptionService.saveConsuption(consumption);
        Offer offer = offerService.createFromFormData(consumption, deviceForm);
        offer.setGeneratedOfferId(1001 + offerService.getOfferIdNumber());
        offer.setConsumptionId(consumptionID);

        offerService.saveOffer(offer);


        model.addAttribute("consumptionId", consumptionID);
        model.addAttribute(OFFER, offer);
        model.addAttribute("serviceGrossTotal",offer.getNettoServiceTotalPrice().multiply(BigDecimal.valueOf(1.27)));
        model.addAttribute("itemGrossTotal", offer.getNettoTotalPrice().multiply(BigDecimal.valueOf(offer.getCompany().getTaxRate())));
        model.addAttribute(CONSUMPTION, consumption);
        model.addAttribute(STEP, "admin3");
        return "admin";
    }

    @PostMapping("admin/tetel/inverterek")
    @ResponseBody
    public ResponseEntity<List<Inverter>> filterItemType(@RequestParam(value="key") String consumptionID, @RequestBody HashMap<String, String> data) {
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

    @PostMapping("admin/tetel/mennyisegvaltoztatas")
    @ResponseBody
    public ResponseEntity<Offer> updateQuantity(@RequestParam(value="key") String consumptionID, @RequestBody HashMap<String, String> data, HttpSession session) {

        Integer lineItemId = Integer.valueOf(data.get("id"));
        double quantity = Double.valueOf(data.get("quantity"));
        Offer offer = offerService.getOfferByConsumptionId(consumptionID);

        log.info(String.format("lineItemId: %s quantity: %s", lineItemId, quantity));

        LineItem lineItem = offer.getLineItem(lineItemId);

        lineItem.setQuantity(quantity);

        offer.updateLineItem(lineItem);
        offerService.saveOffer(offer);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PostMapping("admin/tetel/egysegarvaltoztatas")
    @ResponseBody
    public ResponseEntity<Offer> updatePrice(@RequestParam(value="key") String consumptionID, @RequestBody HashMap<String, String> data, HttpSession session) {

        Integer lineItemId = Integer.valueOf(data.get("id"));
        BigDecimal price = new BigDecimal(data.get("price"));

        Offer offer = offerService.getOfferByConsumptionId(consumptionID);

        log.info(String.format("lineItemId: %s price: %s", lineItemId, price));

        LineItem lineItem = offer.getLineItem(lineItemId);
        lineItem.setPrice(price);

        offer.updateLineItem(lineItem);
        offerService.saveOffer(offer);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }


    @PostMapping("admin/tetel/torles")
    @ResponseBody
    public ResponseEntity<Offer> deleteLineItem(@RequestParam(value="key") String consumptionID, @RequestBody HashMap<String, String> data, HttpSession session) {

        Integer lineItemId = Integer.valueOf(data.get("id"));
        log.info(String.format("lineItemId: %s", lineItemId));

        Offer offer = offerService.getOfferByConsumptionId(consumptionID);
        offer.removeLineItem(lineItemId);
        offerService.saveOffer(offer);


        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PostMapping("admin/tetel/listazas")
    @ResponseBody
    public ResponseEntity<List> getList(@RequestParam(value="key") String consumptionID, @RequestBody HashMap<String, String> data) {
        String type = data.get("type");
        String brand = null;
        if(type.equals("inverter")){
            brand = data.get("brand");
        }
        return new ResponseEntity<>(adminService.getListOfItems(type, brand), HttpStatus.OK);
    }

    @PostMapping("admin/tetel/uj")
    @ResponseBody
    public ResponseEntity<Offer> addNewItem(@RequestParam(value="key") String consumptionID, @RequestBody HashMap<String, String> data, HttpSession session) {

        Integer itemId = Integer.valueOf(data.get("itemId"));
        String type = data.get("type");
        log.info(String.format("New Item's itemId: %s, type: %s", itemId, type));

        Offer offer = offerService.getOfferByConsumptionId(consumptionID);

        if (!offerService.containsItem(offer, itemId, type)) {
            LineItem newItem = offerService.getLineItemFromItemIdAndType(itemId, type);
            log.info("New Item: ", newItem.toString());
            offer.addLineItem(newItem);
            offer.sortLineItems();
        }
        offer.printLineItems();
        offerService.saveOffer(offer);

        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PostMapping("admin/tetel/egyeni")
    @ResponseBody
    public ResponseEntity<Offer> addCustomItem(@RequestParam(value="key") String consumptionID, @RequestBody HashMap<String, String> data, HttpSession session) {

        String name = data.get("name");
        String description = data.get("description");
        BigDecimal price = BigDecimal.valueOf(Integer.valueOf(data.get("price")));
        ItemTypeEnum type = (ItemTypeEnum.valueOf(data.get("type")));
        int  priority = Integer.valueOf(data.get("priority"));

        Consumption consumption = consumptionService.getConsumptionByconsumptionID(consumptionID);
        DeviceForm deviceForm = new DeviceForm(consumption.getInverterId(), consumption.getPanelId());
        consumptionService.saveConsuption(consumption);
        Offer offer = offerService.getOfferByConsumptionId(consumptionID);

        LineItem newItem = new LineItem(name, description, price, type, priority);
        offer.addLineItem(newItem);
        offer.sortLineItems();
        offer.printLineItems();
        offerService.saveOffer(offer);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PostMapping("admin/pdf")
    @ResponseBody
    public ResponseEntity<Resource> getPDF(@RequestParam(value="key") String consumptionID, @ModelAttribute ConsumptionForm consumptionForm) {
        Offer offer = offerService.getOfferByConsumptionId(consumptionID);

        log.info(consumptionForm.getCompany().toString());
        offer.setCompany(consumptionForm.getCompany());
        offerService.saveOffer(offer);
        File pdf = null;

        try {
            pdf = pdfService.getPdf(offer);
        } catch (UnirestException e) {
            log.warn("Pdf Server is unavailable. UnirestException is thrown.");
            e.printStackTrace();
        } catch (IOException e) {
            log.warn("Failed to convert PDF server's response to File.");
            e.printStackTrace();
        }
        Path path = null;
        if (pdf != null) {
            path = Paths.get(pdf.getAbsolutePath());
        }
        ByteArrayResource resource = null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentLength(pdf.length())
                .header("Content-Disposition", "attachment; filename=" + convertPdfName(pdf.getName()))
                .contentType(MediaType.parseMediaType("application/download"))
                .body(resource);
    }

    @PostMapping("/admin/network-upgrade")
    @ResponseBody
    public String isNetworkUpgradeNeededCheck (@RequestBody HashMap < String, String > payload){
        log.info("Request arrived to validate, payload: " + payload.toString());
        return String.valueOf(validationService.validateNetworkUpgrade(payload));
    }

    @RequestMapping(value = "admin/statistics", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Advertising>> listAdvertisements(){
        List<Advertising> advertisingList = advertisingRepository.findAll();
        return new ResponseEntity<List<Advertising>>(advertisingList, HttpStatus.OK);
    }

    private String convertPdfName(String name){
        return name.substring(0, name.indexOf('@')) + ".pdf";
    }
}

