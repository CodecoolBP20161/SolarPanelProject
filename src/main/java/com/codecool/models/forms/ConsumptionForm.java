package com.codecool.models.forms;

import com.codecool.models.CompanyEnum;
import lombok.Data;

@Data
public class ConsumptionForm {
    private String metric;
    private Integer value;
    private int phase = 1;
    private CompanyEnum company = CompanyEnum.Cég1;
}


