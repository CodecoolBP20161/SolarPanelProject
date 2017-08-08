package com.codecool.services;

import com.codecool.models.*;
import com.codecool.models.enums.ItemTypeEnum;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.DeviceForm;
import com.codecool.repositories.OtherItemRepository;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.stereotype.Service;

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
        value = metric.equals(kWh) ? kWhRoundedValue  : FtRoundedValue;
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
            if (value < 12000 ) {
                returnValue = Math.round(value / 1100) * 1000;
            } else if(value >= 12000 && value <= 21999){
                nearestValue = calculateNearestValue(value, metric);
                returnValue = calculateValueToInverterFilter(roundedValue, nearestValue.get(higherValue), nearestValue.get(lowerValue));
            }
        } else {
            if (value < 37500) {
                returnValue = Math.round(((value * 12) / 37.5) / 1100) * 1000;
            } else if(value >= 37500 && value <= 68746){
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
        LineItem inverterLineItem = new LineItem(inverter);
        LineItem solarPanelLineItem = new LineItem(solarPanel);
        solarPanelLineItem.setQuantity(solarPanelService.calculateSolarPanelQuantity(consumptionForm, solarPanel.getCapacity()));

        LineItem additionalStuffLineItem;
        List<OtherItem> otherItems = otherItemRepository.findByPhaseIn(Arrays.asList(0, consumptionForm.getPhase()));

        lineItems.add(solarPanelLineItem);
        lineItems.add(inverterLineItem);

        OtherItem installationFee = new OtherItem("Kivitelezés", "", getInstallationFee(consumptionForm.getValue()),
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
                } else if (additionalStuffLineItem.getName().equals("MC4 Csatlakozó (pár)")){
                    additionalStuffLineItem.setQuantity(4);
                } else if (additionalStuffLineItem.getName().contains("AC vezeték")) {
                    additionalStuffLineItem.setQuantity(10);
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
        } else if (consumption >= 3000 && consumption < 6000){
            installationFee = 120000;
        } else if (consumption >= 6000 && consumption < 12000) {
            installationFee = 140000;
        } else {
            installationFee = 0;
        }

        return installationFee;
    }

    public List<LineItem> getSolarPanelListAsLineItems( ConsumptionForm consumptionForm) {
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


}
