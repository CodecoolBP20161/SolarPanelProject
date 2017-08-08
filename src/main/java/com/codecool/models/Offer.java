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


