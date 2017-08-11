package com.codecool.models.enums;

public enum  CompanyEnum {

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
