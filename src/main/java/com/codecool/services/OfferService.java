package com.codecool.services;


import com.codecool.models.*;
import com.codecool.models.enums.ItemTypeEnum;
import com.codecool.repositories.AdditionalStuffRepository;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OfferService {

    private InverterRepository inverterRepository;
    private SolarPanelRepository solarPanelRepository;
    private SolarPanelService solarPanelService;
    private AdditionalStuffRepository  additionalStuffRepository;


    private final String kWh = "kWh";
    private final String lowerValue = "lowerValue";
    private final String higherValue = "higherValue";

    @Autowired
    public OfferService(InverterRepository inverterRepository, SolarPanelRepository solarPanelRepository, SolarPanelService solarPanelService,
                        AdditionalStuffRepository additionalStuffRepository) {
        this.inverterRepository = inverterRepository;
        this.solarPanelRepository = solarPanelRepository;
        this.solarPanelService = solarPanelService;
        this.additionalStuffRepository = additionalStuffRepository;
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


    public int calculateConsumption(float value, String metric) {
        long returnValue = 0;
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

    public List<LineItem> getOffer(int consumption, String metric, int phase, int panelId, int inverterId) {

        SolarPanel solarPanel = solarPanelRepository.findOne(panelId);
        Inverter inverter = inverterRepository.findOne(inverterId);
        LineItem inverterLineItem = new LineItem(inverter);
        LineItem solarPanelLineItem = new LineItem(solarPanel);
        solarPanelLineItem.setQuantity(solarPanelService.callculateSolarPanelPiece(consumption, metric, solarPanel.getCapacity()));

        LineItem additionalStuffLineItem;
        List<AdditionalStuff> additionalStuffs = additionalStuffRepository.findByPhaseIn(Arrays.asList(0, phase));

        Offer offer = new Offer();
        offer.addLineItem(solarPanelLineItem);
        offer.addLineItem(inverterLineItem);

        AdditionalStuff installationFee = new AdditionalStuff("Kivitelezés", "", getInstallationFee(consumption),
                0, ItemTypeEnum.Service);
        additionalStuffs.add(installationFee);

        for (AdditionalStuff item : additionalStuffs) {
            additionalStuffLineItem = new LineItem(item);
            if (consumption < 12000){
                if (additionalStuffLineItem.getName().equals("16mm2-es MKH vezeték")) additionalStuffLineItem.setQuantity(15);
            } else if (additionalStuffLineItem.getName().equals("Szolár kábel /méter/")) additionalStuffLineItem.setQuantity(50);
            offer.addLineItem(additionalStuffLineItem);
        }
        return offer.getLineItems();
    }


    private int getInstallationFee(int consumption) {
        int installationFee = 0;

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

    public List<SolarPanel> getSolarPanelList() {
        return solarPanelRepository.findAllByOrderByCapacityAscPriceAsc();
    }


}
