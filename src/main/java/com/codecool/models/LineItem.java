package com.codecool.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
class LineItem extends Item {

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
}
