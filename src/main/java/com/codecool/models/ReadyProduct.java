package com.codecool.models;

import lombok.Data;

import java.util.List;

@Data
public class ReadyProduct {

    private String inverterName;
    private String solarPanelName;
    private int nettPrice;
    private int grossPrice;
    private List<String> description;
    private int phase;
    private double capacity;

    public ReadyProduct(String inverterName, String solarPanelName, int nettPrice, int grossPrice, List<String> description, int phase, double capacity) {
        this.inverterName = inverterName;
        this.solarPanelName = solarPanelName;
        this.nettPrice = nettPrice;
        this.grossPrice = grossPrice;
        this.description = description;
        this.phase = phase;
        this.capacity = capacity;
    }
}
