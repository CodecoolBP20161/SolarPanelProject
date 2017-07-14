package com.codecool.models.forms;

import lombok.Data;

@Data
public class ConsumptionForm {
    private String metric;
    private float value;
    private int phase;
}


