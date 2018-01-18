package com.codecool.models.forms;

import com.codecool.models.enums.CompanyEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ConsumptionForm {
    private String metric;
    private Double value;
    private Integer advertisement;
    private int phase = 1;
    private CompanyEnum company = CompanyEnum.TraditionalSolutions;
    private static Map<Integer, Integer> allAdvertisement = new HashMap<>();

    static {
        allAdvertisement.put(1,0);
        allAdvertisement.put(2,0);
        allAdvertisement.put(3,0);
    }

    public ConsumptionForm(String metric, Double value) {
        this.metric = metric;
        this.value = value;
    }

    public ConsumptionForm(String metric, Double value, Integer advertisement) {
        this.metric = metric;
        this.value = value;
        this.advertisement = advertisement;
    }

    public ConsumptionForm() {
    }

    public void addNewAdvertisement(Integer advertisement){
        allAdvertisement.put(advertisement, allAdvertisement.get(advertisement) + 1);
        System.out.println(allAdvertisement.toString());
    }
}


