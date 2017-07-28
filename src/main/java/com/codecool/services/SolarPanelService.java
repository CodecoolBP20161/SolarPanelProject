package com.codecool.services;

import org.springframework.stereotype.Service;

/**
 * Created by Mate on 2017. 07. 28..
 */
@Service
public class SolarPanelService {

    public int callculateSolarPanelPiece(float value, String metric, int solarPanelCapacity) {
        int consumtion = metric.equals("kWh") ? (int) value  :  (int) (((value * 12) / 37.5));
        int piece = consumtion / solarPanelCapacity;

        while ((piece % 4) != 0) {
            piece++;
        }
        return piece;
    }
}
