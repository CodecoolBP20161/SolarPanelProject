package com.codecool.models.enums;

public enum  CompanyEnum {
//    TODO: change Cég1 to the real company név
    TraditionalSolutions,
    StabilInvest,
    SolarProvider;

    private double taxRate;

    static {
        TraditionalSolutions.taxRate = 1.27;
        StabilInvest.taxRate = 1.20;
        SolarProvider.taxRate = 1.27;
    }

    public double getTaxRate(){
        return taxRate;
    }
}
