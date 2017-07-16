package com.codecool.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
public class Offer {
    private static long idCount = 1;
    private long id;
    private double taxRate;
    private ArrayList<LineItem> lineItems;
    private boolean isNetworkUpgradeNeeded;

    public Offer( double taxRate){
        id = idCount++;
        lineItems = new ArrayList<>();
        isNetworkUpgradeNeeded = false;
    }

    @Setter(AccessLevel.NONE)
    private BigDecimal nettoTotalPrice;

    public void addLineItem(LineItem lineItem) {
        lineItems.add(lineItem);
        nettoTotalPrice = nettoTotalPrice.add(lineItem.getTotal());
    }
    public void removeLineItem(LineItem lineItem){
        lineItems.remove(lineItem);
        nettoTotalPrice = nettoTotalPrice.subtract(lineItem.getTotal());
    }
}


