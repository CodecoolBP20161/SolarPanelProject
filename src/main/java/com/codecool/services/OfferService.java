package com.codecool.services;


import com.codecool.models.Inverter;
import com.codecool.models.SolarPanel;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OfferService {

    private InverterRepository inverterRepository;
    private SolarPanelRepository solarPanelRepository;

    private final String kWh = "kWh";
    private final String Ft = "Ft";
    private final String lowerValue = "lowerValue";
    private final String higherValue = "higherValue";

    @Autowired
    public OfferService(InverterRepository inverterRepository, SolarPanelRepository solarPanelRepository) {
        this.inverterRepository = inverterRepository;
        this.solarPanelRepository = solarPanelRepository;
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
        Map<String, Integer> nearestValue = calculateNearestValue(value, metric);

        if (!metric.equals(Ft)) {
            if (value < 12000 ) {
                returnValue = Math.round(value / 1100) * 1000;
            } else if(value >= 12000 && value <= 21999){
                returnValue = calculateValueToInverterFilter(roundedValue, nearestValue.get(higherValue), nearestValue.get(lowerValue));
            }
        } else {
            if (value < 37500) {
                returnValue = Math.round(((value * 12) / 37.5) / 1100) * 1000;
            } else if(value >= 37500 && value <= 68746){
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
/*
        List<Inverter> inverterList = inverterRepository.findByCustomerValue(value, phase);
*/

        return inverterRepository.findByCustomerValue(value, phase);
    }

    public List<SolarPanel> getSolarPanelList() {
        return solarPanelRepository.findAllByOrderByCapacityAscPriceAsc();
    }
}
