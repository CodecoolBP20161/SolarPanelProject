package com.codecool.models;

import com.codecool.models.enums.CompanyEnum;
import com.codecool.models.enums.ItemTypeEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Slf4j
@Entity
@EntityScan
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long generatedOfferId;
    private CompanyEnum company;
    @ElementCollection
    @CollectionTable(name = "lineitems", joinColumns = @JoinColumn(name = "fuckId"))
    private volatile List<LineItem> lineItems = new ArrayList<LineItem>();
    private boolean isNetworkUpgradeNeeded;
    private String consumptionId;

    @Setter(AccessLevel.NONE)
    private BigDecimal nettoTotalPrice;

    @Setter(AccessLevel.NONE)
    private BigDecimal nettoServiceTotalPrice;


    public Offer() {
        lineItems = new CopyOnWriteArrayList<>();
        isNetworkUpgradeNeeded = false;
        nettoTotalPrice = new BigDecimal(0);
        nettoServiceTotalPrice = new BigDecimal(0);
    }

    public void addLineItem(LineItem lineItem) {
        lineItems.add(lineItem);
        calculateNettoTotalPrice();
        calculatenettoServiceTotalPrice();
    }

    public void removeLineItem(LineItem lineItem) {
        lineItems.remove(lineItem);
        calculateNettoTotalPrice();
        calculatenettoServiceTotalPrice();

    }

    public void removeLineItem(Integer lineItemId) {
        LineItem itemToRemove = null;
        for (LineItem lineItem : lineItems) {
            if (lineItem.getId().equals(lineItemId)) {
                itemToRemove = lineItem;
                if (lineItem.getType().equals(ItemTypeEnum.Item)) {
                    nettoTotalPrice = nettoTotalPrice.subtract(lineItem.getTotal());
                } else {
                    nettoServiceTotalPrice = nettoServiceTotalPrice.subtract(lineItem.getTotal());
                }
                break;
            }
        }
        if (itemToRemove != null) {
            lineItems.remove(itemToRemove);
        }

    }

    private void calculateNettoTotalPrice() {
        nettoTotalPrice = new BigDecimal(0);
        for (LineItem lineItem : lineItems) {
            if (lineItem.getType() != ItemTypeEnum.Service) {
                nettoTotalPrice = nettoTotalPrice.add(lineItem.getTotal());
            }
        }
    }

    private void calculatenettoServiceTotalPrice() {
        nettoServiceTotalPrice = new BigDecimal(0);
        for (LineItem lineItem : lineItems) {
            if (lineItem.getType() == ItemTypeEnum.Service) {
                nettoServiceTotalPrice = nettoServiceTotalPrice.add(lineItem.getTotal());
            }
        }
    }

    public List<LineItem> getLineItems(){
        return lineItems;
    }

    public LineItem getLineItem(Integer lineItemId) {
        for (LineItem lineItem : lineItems) {
            if (lineItem.getId().equals(lineItemId)) return lineItem;
        }
        return null;
    }

    public void updateLineItem(LineItem lineItem) {
        for (int i = 0; i < lineItems.size(); i++) {
            if (lineItems.get(i).getId().equals(lineItem.getId())) {
                lineItems.set(i, lineItem);
                break;
            }
        }
        calculateNettoTotalPrice();
        calculatenettoServiceTotalPrice();
    }

    public void sortLineItems(){
        Collections.sort(lineItems);
        Collections.reverse(lineItems);
    }
    public void printLineItems(){
        log.info("LineItems: \n");
        for (LineItem item : lineItems) {
            log.info(item.toJson().toString());
        }
    }
}


