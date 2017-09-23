package com.codecool.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SolarPanelService {

    public int calculateSolarPanelQuantity(double consumption, int solarPanelCapacity) {
        return (consumption % solarPanelCapacity == 0) ? (int) (consumption/solarPanelCapacity) :
                (int) (consumption / solarPanelCapacity) + 1;
    }

    public double calculateSolarPanelSupportStructure(int solarPanelQuantity) {
        return (solarPanelQuantity % 4 == 0) ? solarPanelQuantity/4 :
                (solarPanelQuantity/4 + ((solarPanelQuantity % 4) * 0.25));
    }
}
