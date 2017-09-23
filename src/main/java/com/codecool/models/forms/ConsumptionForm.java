package com.codecool.models.forms;

import com.codecool.models.enums.CompanyEnum;
import lombok.Data;

@Data
public class ConsumptionForm {
    private String metric;
    private Double value;
    private int phase = 1;
    private CompanyEnum company = CompanyEnum.TraditionalSolutions;

    public ConsumptionForm(String metric, Double value) {
        this.metric = metric;
        this.value = value;
    }

    public ConsumptionForm() {
    }
}


