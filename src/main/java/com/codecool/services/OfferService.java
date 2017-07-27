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
    private final String lowerValue = "lowerValue";
    private final String higherValue = "higherValue";



    @Autowired
    public OfferService(InverterRepository inverterRepository, SolarPanelRepository solarPanelRepository) {
        this.inverterRepository = inverterRepository;
        this.solarPanelRepository = solarPanelRepository;
    }

    private Map<String, Integer> callculateNearestValue(float value, String metric) {
        float kWhRoundedValue = (value / 1100) * 1000;
        float FtRoundedValue = (float) ((((value * 12) / 37.5) / 1100) * 1000);
        Map<String, Integer> returnValue = new HashMap<>();

        value = metric.equals(kWh) ? kWhRoundedValue  : FtRoundedValue;

        for (int i = 0; i < inverterRepository.findAllByOrderByCapacity().size(); i++) {
            if (inverterRepository.findAllByOrderByCapacity().get(i).getCapacity() > value) {
                returnValue.put(higherValue, inverterRepository.findAllByOrderByCapacity().get(i).getCapacity());
                returnValue.put(lowerValue, inverterRepository.findAllByOrderByCapacity().get(i - 1).getCapacity());
                break;
            }
        }
        return returnValue;
    }


    public int callculateConsumption(float value, String metric) {

        long returnValue = 0;
        float kWhRoundedValue = (value / 1100) * 1000;
        float FtRoundedValue = (float) ((((value * 12) / 37.5) / 1100) * 1000);
        Map<String, Integer> nearestValue = callculateNearestValue(value, metric);


        if (metric.equals(kWh)) {
            if (value < 12000) {
                returnValue = Math.round(value / 1100) * 1000;
            } else {
                if (kWhRoundedValue - nearestValue.get(lowerValue) < nearestValue.get(higherValue) - kWhRoundedValue) {
                    returnValue = nearestValue.get(lowerValue);
                } else if (kWhRoundedValue - nearestValue.get(lowerValue) > nearestValue.get(higherValue) - kWhRoundedValue) {
                    returnValue = nearestValue.get(higherValue);
                }
            }
        } else {
            if (value < 37500) {
                returnValue = Math.round(((value * 12) / 37.5) / 1100) * 1000;
            } else {
                if (FtRoundedValue - nearestValue.get(lowerValue) < nearestValue.get(higherValue) - FtRoundedValue) {
                    returnValue = nearestValue.get(lowerValue);
                } else if (FtRoundedValue - nearestValue.get(lowerValue) > nearestValue.get(higherValue) - FtRoundedValue) {
                    returnValue = nearestValue.get(higherValue);
                }
            }

            log.info("callculated value for inverter type: " + returnValue);
        }
        return (int) returnValue;

    }

    public List<Inverter> callculateInverterList(int value, int phase) {
/*
        List<Inverter> inverterList = inverterRepository.findByCustomerValue(value, phase);
*/

        return inverterRepository.findByCustomerValue(value, phase);
    }

    public List<SolarPanel> getSolarPanelList() {
        return solarPanelRepository.findAllByOrderByCapacityAscPriceAsc();
    }
}
