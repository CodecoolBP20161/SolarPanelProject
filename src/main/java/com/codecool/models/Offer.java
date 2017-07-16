package com.codecool.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
public class Offer {
    private String name;
    private long id;
    private double taxRate;
    private ArrayList<LineItem> lineItems;
    private boolean isNetworkUpgradeNeeded;
    @Setter(AccessLevel.NONE)
    private BigDecimal nettoTotalPrice;

    public void addLineItem(LineItem lineItem) {
        lineItems.add(lineItem);
        nettoTotalPrice = nettoTotalPrice.add(lineItem.getTotal());
    }
}


