package com.codecool.services;

import org.springframework.stereotype.Service;

@Service
public class SolarPanelService {

    public int calculateSolarPanelQuantity(float value, String metric, int solarPanelCapacity) {
        int consumtion = metric.equals("kWh") ? (int) value  :  (int) (((value * 12) / 37.5));
        int piece = consumtion / solarPanelCapacity;

        while ((piece % 4) != 0) {
            piece++;
        }
        return piece;
    }
}
