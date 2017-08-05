package com.codecool.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigDecimal;

@Data
public class LineItem extends Item {

    private  int quantity;

    @Setter(AccessLevel.NONE)
    private BigDecimal total;

    public LineItem(Item item){
        this.setName(item.getName());
        this.setDescription(item.description);
        this.setPrice(item.getPrice());
        this.setQuantity(1);
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
        this.total = price.multiply(BigDecimal.valueOf(quantity));
    }

    public JSONObject toJson(){
        JSONObject convertedItem = new JSONObject();
        BigDecimal quantityBigDecimal = new BigDecimal(this.getQuantity());

        convertedItem.put("name", this.getName());
        convertedItem.put("price", this.getPrice());
        convertedItem.put("quantity", this.getQuantity());
        convertedItem.put("subtotal", this.getPrice().multiply(quantityBigDecimal));
        convertedItem.put("description", this.getDescription());

        return  convertedItem;
    }

}
