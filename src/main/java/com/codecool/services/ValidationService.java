package com.codecool.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Slf4j
@Service
public class ValidationService {
    public boolean validateNetworkUpgrade(HashMap consumption){
        log.info("Payload: " + consumption);
        Integer value;
        try{
            value =  Integer.valueOf((String) consumption.get("value"));
        } catch (NumberFormatException e){
            return false;
        }

        String metric = (String) consumption.get("metric");

        boolean HUFisHigh = value > 17184 && metric.equals("Ft");
        boolean consumptionIsHigh = (value/1100 > 5.499) && metric.equals("kWh");

        return HUFisHigh || consumptionIsHigh;
    }
}
