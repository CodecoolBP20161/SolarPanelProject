package com.codecool.services;

import com.codecool.models.*;
import com.codecool.models.enums.InverterBrandEnum;
import com.codecool.models.enums.ItemTypeEnum;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.DeviceForm;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.OtherItemRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class OfferService {

    private InverterRepository inverterRepository;
    private SolarPanelRepository solarPanelRepository;
    private SolarPanelService solarPanelService;
    private OtherItemRepository otherItemRepository;

    private final String kWh = "kWh";

    @Autowired
    public OfferService(InverterRepository inverterRepository, SolarPanelRepository solarPanelRepository,
                        SolarPanelService solarPanelService, OtherItemRepository otherItemRepository) {
        this.inverterRepository = inverterRepository;
        this.solarPanelRepository = solarPanelRepository;
        this.solarPanelService = solarPanelService;
        this.otherItemRepository = otherItemRepository;
    }

    public List<Inverter> calculateInverterList(double value) {
        return inverterRepository.findByCustomerValue((int) ((value < 1650) ? 2000  : value),
                                                            (value < 5000) ? 1 : 3);
    }

    public double calculateConsumption(ConsumptionForm consumption) {
        double consumptionValue = (consumption.getMetric().equals(kWh)) ? consumption.getValue() :
                ((consumption.getValue() * 12)/37.5);

        log.info("Given metric: " + consumption.getMetric() + " Rounded value " + Math.round(consumptionValue / 1100) +
                " consumptionValue " + consumptionValue + " Given value " + consumption.getValue());
        return (Math.round(consumptionValue / 1100)) * 1000;
    }

    public List<LineItem> getLineItems(ConsumptionForm consumptionForm, DeviceForm deviceForm) {
        double consumptionValue = (consumptionForm.getMetric().equals(kWh)) ? consumptionForm.getValue() :
                ((consumptionForm.getValue() * 12)/37.5);

        List<LineItem> lineItems = new ArrayList<>();

        int panelID = Integer.parseInt(deviceForm.getPanelId());

        SolarPanel solarPanel = solarPanelRepository.findOne(panelID);

        List<OtherItem> otherItems = otherItemRepository.findByPhaseIn(Arrays.asList(0, (calculateConsumption(consumptionForm) < 5000) ? 1 : 3));

        LineItem solarPanelLineItem = new LineItem(solarPanel);
        int neededSolarPanelQuantity = solarPanelService.calculateSolarPanelQuantity(consumptionValue, solarPanel.getCapacity());

        solarPanelLineItem.setQuantity(neededSolarPanelQuantity);
        LineItem additionalStuffLineItem;

        lineItems.add(solarPanelLineItem);

        if (!deviceForm.getInverterId().equals("")) {
            int inverterId = Integer.parseInt(deviceForm.getInverterId());
            Inverter inverter = inverterRepository.findOne(inverterId);
            LineItem inverterLineItem = new LineItem(inverter);
            log.info(inverterLineItem.toJson().toString());
            lineItems.add(inverterLineItem);

            if (inverter.getBrand().equals(InverterBrandEnum.SOLAREDGE)) {
                int quantityOfOptimizer = (inverter.getOptimizerName().contains("300")) ? neededSolarPanelQuantity : neededSolarPanelQuantity / 2;
                OtherItem optimizerIsNeeded = new OtherItem(inverter.getOptimizerName(), "", inverter.getOptimizerPrice(), 0, ItemTypeEnum.Item);
                LineItem optimizerLineItem = new LineItem(optimizerIsNeeded);
                optimizerLineItem.setQuantity(quantityOfOptimizer);
                lineItems.add(optimizerLineItem);
            }

            OtherItem wifiModule = new OtherItem("Wifi modul", "", inverter.getWifiModule(), 0, ItemTypeEnum.Item);
            LineItem wifiLineItem = new LineItem(wifiModule);
            lineItems.add(wifiLineItem);
        }




        OtherItem installationFee = new OtherItem("Kivitelezés", "", getInstallationFee(consumptionValue),
                0, ItemTypeEnum.Service);
        installationFee.setId(11);
        otherItems.add(installationFee);

        for (OtherItem item : otherItems) {
            additionalStuffLineItem = new LineItem(item);
            if (consumptionValue < 12000) {
                if (additionalStuffLineItem.getName().equals("16mm2-es MKH vezeték")) {
                    additionalStuffLineItem.setQuantity(15);
                } else if (additionalStuffLineItem.getName().equals("Szolár kábel /méter/")) {
                    additionalStuffLineItem.setQuantity(50);
                } else if (additionalStuffLineItem.getName().equals("MC4 Csatlakozó (pár)")) {
                    additionalStuffLineItem.setQuantity(4);
                } else if (additionalStuffLineItem.getName().contains("AC vezeték")) {
                    additionalStuffLineItem.setQuantity(10);
                } else if (additionalStuffLineItem.getName().equals("Termék díj")) {
                    additionalStuffLineItem.setQuantity(neededSolarPanelQuantity);
                } else if (additionalStuffLineItem.getName().contains("Solaredge")) {
                    additionalStuffLineItem.setQuantity(neededSolarPanelQuantity);
                } else if (additionalStuffLineItem.getName().equals("Tartószerkezet szett (4panel/szett)")) {
                    additionalStuffLineItem.setQuantity(solarPanelService.calculateSolarPanelSupportStructure(neededSolarPanelQuantity));
                }
                lineItems.add(additionalStuffLineItem);
            }
        }
        return lineItems;
    }

    private int getInstallationFee(double consumption) {
        if (consumption < 3000) {
            return 100000;
        } else if (consumption >= 3000 && consumption < 6000) {
            return  120000;
        } else if (consumption >= 6000 && consumption < 12000) {
            return  140000;
        }
        return 0;
    }

    public List<LineItem> getSolarPanelListAsLineItems(ConsumptionForm consumptionForm) {
        LineItem solarPanelItem;
        List<LineItem> solarPanelLineItems = new ArrayList<>();
        double consumptionValue = (consumptionForm.getMetric().equals(kWh)) ? consumptionForm.getValue() :
                ((consumptionForm.getValue() * 12)/37.5);
        System.out.println("Ajanlat 2 controller, getSolarPanelListAsLineItems before for loop ");
        System.out.println("consumption " + consumptionValue);
       // if (consumptionValue < 12000) {
            for (SolarPanel solarPanel : solarPanelRepository.findAllByOrderByCapacityAscPriceAsc()) {
                System.out.println(solarPanel.toString());
                int quantity = solarPanelService.calculateSolarPanelQuantity(consumptionValue, solarPanel.getCapacity());

                solarPanelItem = new LineItem(solarPanel);
                solarPanelItem.setQuantity(quantity);
                solarPanelLineItems.add(solarPanelItem);
            }
        //}
        System.out.println("Ajanlat 2 controller, getSolarPanelListAsLineItems after for loop ");
        return solarPanelLineItems;
    }

    public Offer createFromFormData(ConsumptionForm consumption, DeviceForm deviceForm){
        Offer offer = new Offer();

        offer.setCompany(consumption.getCompany());
        List<LineItem> offerItem =  getLineItems(consumption, deviceForm);

        offerItem.forEach(offer::addLineItem);
        offer.sortLineItems();
        return offer;
    }

    public boolean containsItem(Offer offer, Integer itemId, String type){
        for (LineItem lineItem : offer.getLineItems()){
            if(lineItem.getItemId() != null){
                if(lineItem.getItemId().equals(itemId)) {
                    String inputName = lineItem.getName();
                    LineItem newItem = getLineItemFromItemIdAndType(itemId, type);
                    return inputName.equals(newItem.getName());
                }
            }
        }
        return false;
    }

    public LineItem getLineItemFromItemIdAndType(Integer itemId, String type){
        Item newItem = null;

        switch (type){
            case "other":
                newItem = otherItemRepository.findOne(itemId);
                break;
            case "inverter":
                newItem = inverterRepository.findOne(itemId);
                break;
            case "panel":
                newItem = solarPanelRepository.findOne(itemId);
                break;
        }
        return new LineItem(newItem);
    }

}
