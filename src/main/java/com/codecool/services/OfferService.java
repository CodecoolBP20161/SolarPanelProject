package com.codecool.services;


import com.codecool.models.Inverter;
import com.codecool.models.SolarPanel;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OfferService {

    private InverterRepository inverterRepository;
    private SolarPanelRepository solarPanelRepository;

    @Autowired
    public OfferService(InverterRepository inverterRepository, SolarPanelRepository solarPanelRepository) {
        this.inverterRepository = inverterRepository;
        this.solarPanelRepository = solarPanelRepository;
    }

/*    public List<Inverter> callculateInverterList(Integer value) {
        List<Inverter> inverterList = (value >= 11 &
                inverterRepository.findByCustomerValue(((int) value) *1000).size() == 0) ?
                inverterRepository.findAllOvertTenThousand():
                inverterRepository.findByCustomerValue(((int) value) *1000);
        return inverterList;
    }*/

    public int callculateConsumption(float value, String metric) {

        long returnValue = 0;
        int higherValue = 0;
        int lowerValue = 0;
        float kWhRoundedValue = (value / 1100) * 1000;
        float FtRoundedValue = (float) ((((value * 12) / 37.5) / 1100) * 1000);

        if (metric.equals("kWh")) {
            for (int i = 0; i < inverterRepository.findAllByOrderByCapacity().size(); i++) {
                if (inverterRepository.findAllByOrderByCapacity().get(i).getCapacity() > Math.round(value / 1100) * 1000) {
                    higherValue = inverterRepository.findAllByOrderByCapacity().get(i).getCapacity();
                    lowerValue = inverterRepository.findAllByOrderByCapacity().get(i - 1).getCapacity();
                    break;
                }
            }
        } else {
            for (int i = 0; i < inverterRepository.findAllByOrderByCapacity().size(); i++) {
                if (inverterRepository.findAllByOrderByCapacity().get(i).getCapacity() > Math.round(((value * 12) / 37.5) / 1100) * 1000) {
                    higherValue = inverterRepository.findAllByOrderByCapacity().get(i).getCapacity();
                    lowerValue = inverterRepository.findAllByOrderByCapacity().get(i - 1).getCapacity();
                    break;
                }
            }
        }


        if (metric.equals("kWh")) {
            if (value < 12000) {
                returnValue = Math.round(value / 1100) * 1000;
            } else {
                if (kWhRoundedValue - lowerValue < higherValue - kWhRoundedValue) {
                    returnValue = lowerValue;
                } else if(kWhRoundedValue - lowerValue > higherValue - kWhRoundedValue){
                    returnValue = higherValue;
                }
            }
        } else {
            if (value < 37500) {
                returnValue = Math.round(((value * 12) / 37.5) / 1100) * 1000;
            } else {
                if (FtRoundedValue - lowerValue < higherValue - FtRoundedValue) {
                    returnValue = lowerValue;
                } else if(FtRoundedValue - lowerValue > higherValue - FtRoundedValue){
                    returnValue = higherValue;
                }
            }
        }


        log.info("callculated value for inverter type: " + returnValue);
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
