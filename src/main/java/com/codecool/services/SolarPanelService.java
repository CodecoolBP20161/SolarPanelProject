package com.codecool.services;

import com.codecool.models.forms.ConsumptionForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
        return piece;
    }
}
