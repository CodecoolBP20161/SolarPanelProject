package com.codecool.services;

import com.codecool.models.*;
import com.codecool.models.enums.*;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.DeviceForm;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.OtherItemRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.Line;
import java.util.*;

@Slf4j
@Service
public class OfferService {

    private InverterRepository inverterRepository;
    private SolarPanelRepository solarPanelRepository;
    private SolarPanelService solarPanelService;
    private OtherItemRepository otherItemRepository;


    private final String kWh = "kWh";
    private final String lowerValue = "lowerValue";
    private final String higherValue = "higherValue";


    @Autowired
    public OfferService(InverterRepository inverterRepository, SolarPanelRepository solarPanelRepository,
                        SolarPanelService solarPanelService, OtherItemRepository otherItemRepository) {
        this.inverterRepository = inverterRepository;
        this.solarPanelRepository = solarPanelRepository;
        this.solarPanelService = solarPanelService;
        this.otherItemRepository = otherItemRepository;
    }

    private Map<String, Integer> calculateNearestValue(float value, String metric) {
        float kWhRoundedValue = (value / 1100) * 1000;
        float FtRoundedValue = (float) ((((value * 12) / 37.5) / 1100) * 1000);
        value = metric.equals(kWh) ? kWhRoundedValue : FtRoundedValue;
        Map<String, Integer> returnValue = new HashMap<>();

        for (int i = 0; i < inverterRepository.findAllByOrderByCapacity().size(); i++) {
            if (inverterRepository.findAllByOrderByCapacity().get(i).getCapacity() > value) {
                returnValue.put(higherValue, inverterRepository.findAllByOrderByCapacity().get(i).getCapacity());
                returnValue.put(lowerValue, inverterRepository.findAllByOrderByCapacity().get(i - 1).getCapacity());
                break;
            }
        }
        return returnValue;
    }


    public int calculateConsumption(ConsumptionForm consumption) {
        long returnValue = 0;
        float value = consumption.getValue();
        String metric = consumption.getMetric();
        float kWhRoundedValue = (value / 1100) * 1000;
        float FtRoundedValue = (float) ((((value * 12) / 37.5) / 1100) * 1000);
        float roundedValue = metric.equals(kWh) ? kWhRoundedValue : FtRoundedValue;
        Map<String, Integer> nearestValue;

        // TODO: 12100 az ultimate szám ahol kiakad az inverterre, 12101 re 12 re kerekít, 12099 lefele

        if (metric.equals(kWh)) {
            if (value < 12000) {
                returnValue = Math.round(value / 1100) * 1000;
            } else if (value >= 12000 && value <= 21999) {
                nearestValue = calculateNearestValue(value, metric);
                returnValue = calculateValueToInverterFilter(roundedValue, nearestValue.get(higherValue), nearestValue.get(lowerValue));
            }
        } else {
            if (value < 37500) {
                returnValue = Math.round(((value * 12) / 37.5) / 1100) * 1000;
            } else if (value >= 37500 && value <= 68746) {
                nearestValue = calculateNearestValue(value, metric);
                returnValue = calculateValueToInverterFilter(roundedValue, nearestValue.get(higherValue), nearestValue.get(lowerValue));
            }
        }
        log.info("calculated value for inverter type: " + returnValue);
        return (int) returnValue;
    }

    private int calculateValueToInverterFilter(float value, float higherValue, float lowerValue) {
        float returnValue = 0;
        if (value - lowerValue < higherValue - value) {
            returnValue = lowerValue;
        } else if (value - lowerValue > higherValue - value) {
            returnValue = higherValue;
        }
        return (int) returnValue;
    }

    public List<Inverter> calculateInverterList(int value, int phase) {

        return inverterRepository.findByCustomerValue(value, phase);
    }

    public List<LineItem> getLineItems(ConsumptionForm consumptionForm, DeviceForm deviceForm) {
        int consumptionValue = calculateConsumption(consumptionForm);

        int inverterId = Integer.parseInt(deviceForm.getInverterId());
        int panelID = Integer.parseInt(deviceForm.getPanelId());

        List<LineItem> lineItems = new ArrayList<>();

        SolarPanel solarPanel = solarPanelRepository.findOne(panelID);
        Inverter inverter = inverterRepository.findOne(inverterId);
        List<OtherItem> otherItems = otherItemRepository.findByPhaseIn(Arrays.asList(0, consumptionForm.getPhase()));
        LineItem inverterLineItem = new LineItem(inverter);
        log.info(inverterLineItem.toJson().toString());

        inverterLineItem.setName(inverter.getBrand() + " " + inverter.getName());
        LineItem solarPanelLineItem = new LineItem(solarPanel);
        int neededSolarpanelQuantity = solarPanelService.calculateSolarPanelQuantity(consumptionForm, solarPanel.getCapacity());

        solarPanelLineItem.setQuantity(neededSolarpanelQuantity);
        LineItem additionalStuffLineItem;

        lineItems.add(solarPanelLineItem);
        lineItems.add(inverterLineItem);


        if (inverter.getBrand().equals(InverterBrandEnum.SOLAREDGE)) {
            int quantityOfOptimlizer = (inverter.getOptimalizerName().contains("300")) ? neededSolarpanelQuantity : neededSolarpanelQuantity / 2;
            OtherItem optimalizerItsNeeded = new OtherItem(inverter.getOptimalizerName(), "", inverter.getOptimalierPrice(), 0, ItemTypeEnum.Item);
            LineItem optimalizerLineItem = new LineItem(optimalizerItsNeeded);
            optimalizerLineItem.setQuantity(quantityOfOptimlizer);
            lineItems.add(optimalizerLineItem);
        }

        OtherItem wifiModule = new OtherItem("Wifi modul", "", inverter.getWifiModule(), 0, ItemTypeEnum.Item);
        LineItem wifiLineItem = new LineItem(wifiModule);
        lineItems.add(wifiLineItem);


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
                    additionalStuffLineItem.setQuantity(neededSolarpanelQuantity);
                } else if (additionalStuffLineItem.getName().contains("Solaredge")) {
                    additionalStuffLineItem.setQuantity(neededSolarpanelQuantity);
                } else if (additionalStuffLineItem.getName().equals("Tartószerkezet szett (4panel/szett)")) {
                    additionalStuffLineItem.setQuantity(solarPanelService.callculateSolarPanelSupportStructure(neededSolarpanelQuantity));
                }
                lineItems.add(additionalStuffLineItem);
            }
        }
        return lineItems;
    }


    private int getInstallationFee(int consumption) {
        int installationFee;

        if (consumption < 3000) {
            installationFee = 100000;
        } else if (consumption >= 3000 && consumption < 6000) {
            installationFee = 120000;
        } else if (consumption >= 6000 && consumption < 12000) {
            installationFee = 140000;
        } else {
            installationFee = 0;
        }

        return installationFee;
    }

    public List<LineItem> getSolarPanelListAsLineItems(ConsumptionForm consumptionForm) {
        LineItem solarPanelItem;
        List<LineItem> solarPanelLineItems = new ArrayList<>();

        for (SolarPanel solarPanel : solarPanelRepository.findAllByOrderByCapacityAscPriceAsc()) {
            int quantity = solarPanelService.calculateSolarPanelQuantity(consumptionForm, solarPanel.getCapacity());

            solarPanelItem = new LineItem(solarPanel);
            solarPanelItem.setQuantity(quantity);
            solarPanelLineItems.add(solarPanelItem);
        }
        return solarPanelLineItems;
    }
    public Offer createFromFormData(ConsumptionForm consumption, DeviceForm deviceForm){
        Offer offer = new Offer();

        offer.setCompany(consumption.getCompany());
        List<LineItem> offerItem =  getLineItems(consumption, deviceForm);

        offerItem.forEach(offer::addLineItem);
        return offer;
    }

    public boolean containsItem(Offer offer, Integer itemId, String type){
        for (LineItem lineItem : offer.getLineItems()){
            if(lineItem.getItemId().equals(itemId)){
                String inputName = lineItem.getName();
                LineItem newItem = getLineItemFromItemIdAndType(itemId, type);
                return inputName.equals(newItem.getName());
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
