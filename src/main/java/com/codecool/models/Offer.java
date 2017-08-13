package com.codecool.models;

import com.codecool.models.enums.CompanyEnum;
import com.codecool.models.enums.ItemTypeEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Offer {


    private static long idCount = 1;
    private long id;
    private CompanyEnum company;
    private List<LineItem> lineItems;
    private boolean isNetworkUpgradeNeeded;

    @Setter(AccessLevel.NONE)
    private BigDecimal nettoTotalPrice;


    public Offer(){
        id = idCount++;
        lineItems = new ArrayList<>();
        isNetworkUpgradeNeeded = false;
        nettoTotalPrice = new BigDecimal(0);
    }

    public void addLineItem(LineItem lineItem) {
        lineItems.add(lineItem);
        nettoTotalPrice = nettoTotalPrice.add(lineItem.getTotal());
    }
    public void removeLineItem(LineItem lineItem){
        lineItems.remove(lineItem);
        nettoTotalPrice = nettoTotalPrice.subtract(lineItem.getTotal());
    }
    public void removeLineItem(Integer lineItemId){
        LineItem itemToRemove = null;
        for (LineItem lineItem : lineItems){
            if(lineItem.getId().equals(lineItemId)) {
                itemToRemove = lineItem;
                nettoTotalPrice = nettoTotalPrice.subtract(lineItem.getTotal());
                break;
            }
        }
        if(itemToRemove != null){
            lineItems.remove(itemToRemove);
        }

    }

    private void recalculateNettoTotalPrice(){
        nettoTotalPrice = new BigDecimal(0);
        for (LineItem lineitem : lineItems){
            nettoTotalPrice = nettoTotalPrice.add(lineitem.getTotal());
        }
    }

    public LineItem getLineItem(Integer lineItemId){
        for (LineItem lineItem : lineItems){
            if(lineItem.getId().equals(lineItemId)) return lineItem;
        }
        return null;
    }

    public void updateLineItem(LineItem lineItem){
        for (int i = 0; i < lineItems.size(); i++){
            if(lineItems.get(i).getId().equals(lineItem.getId())) {
                lineItems.set(i, lineItem);
                break;
                }
            }
        recalculateNettoTotalPrice();
        }

    public JSONObject toJson(){

        JSONObject offerJson = new JSONObject();
        JSONArray items = new JSONArray();
        JSONArray services = new JSONArray();

        offerJson.put("id", this.getId());
        offerJson.put("isNetworkUpgradeNeeded", this.isNetworkUpgradeNeeded);
        offerJson.put("taxRate", this.company.getTaxRate());
        offerJson.put("netTotal", this.getNettoTotalPrice());
        offerJson.put("grossTotal", this.getNettoTotalPrice().multiply(new BigDecimal(this.company.getTaxRate())));

        for(LineItem item : lineItems){
            if(item.getType() == ItemTypeEnum.Item) items.put(item.toJson());
            else services.put(item.toJson());
        }
        return offerJson;
    }
}


