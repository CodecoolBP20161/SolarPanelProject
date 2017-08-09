package com.codecool.services;

import com.codecool.models.forms.ConsumptionForm;
import org.springframework.stereotype.Service;

@Service
public class SolarPanelService {

    public int calculateSolarPanelQuantity(ConsumptionForm consumptionForm, int solarPanelCapacity) {
        float value = consumptionForm.getValue();
        String metric = consumptionForm.getMetric();

        int consumption = metric.equals("kWh") ? (int) value  :  (int) (((value * 12) / 37.5));
        int piece = consumption / solarPanelCapacity;
        while ((piece % 4) != 0) {
            piece++;
        }
//:TODO beszéljük meg a napelem és tartók kalkulálását csak 4 napelem lehet egyszerre vagy lehet 2 esével is növelni?
/*
        int quantity = 0;

        float value = consumptionForm.getValue();
        String metric = consumptionForm.getMetric();
        int consumption = metric.equals("kWh") ? (int) value  :  (int) (((value * 12) / 37.5));


        return quantity;*/
        return piece;
    }

    public double callculateSolarPanelSupportStructure(double solarPanelQuantity) {
        double quantity = 0;

        if (solarPanelQuantity % 4 == 0) {
            quantity = solarPanelQuantity / 4;
        } else {
            quantity = (solarPanelQuantity / 4) + 0.5;
        }
        return quantity;
    }
}
