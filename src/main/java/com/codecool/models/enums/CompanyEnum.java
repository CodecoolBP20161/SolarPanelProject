package com.codecool.models.enums;

public enum  CompanyEnum {
//    TODO: change Cég1 to the real company név
    Cég1,
    StabilInvest,
    SolarProvider;

    private double taxRate;

    static {
    Cég1.taxRate = 1.27;
    StabilInvest.taxRate = 1.20;
    SolarProvider.taxRate = 1.27;
    }

    public double getTaxRate(){
        return taxRate;
    }
}
