package com.codecool.models;

import com.codecool.models.enums.ItemTypeEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Embeddable
public class LineItem extends Item implements Comparable<LineItem> {

    private static Integer idCount = 1;

    public Integer id;

    private  double quantity;

    private Integer itemId;

    private int priority;

    @NotNull
    ItemTypeEnum type;


    @Setter(AccessLevel.NONE)
    private BigDecimal total;

    public LineItem(Item item){
        super(item.getName(), item.getDescription(), item.getPrice(), item.getType());
        setId(idCount++);
        setItemId(item.getId());
        setQuantity(1);
        total = item.getPrice();
        setPriority(item.getPriority());
    }
    public LineItem(String name, String description, BigDecimal price, ItemTypeEnum type,
                    int priority){
        super(name, description, price, type);
        setId(idCount++);
        setItemId(null);
        setQuantity(1);
        total = price;
        setPriority(priority);
    }

    public LineItem() {
    }

    public LineItem(String name, String description, BigDecimal price, ItemTypeEnum type) {
        super(name, description, price, type);
    }

    public void setQuantity(double quantity){
        this.quantity = quantity;
        this.total = price.multiply(BigDecimal.valueOf(quantity));
    }

    public void setPrice(BigDecimal price){
        this.price = price;
        this.total = price.multiply(BigDecimal.valueOf(quantity));
    }

    public int compareTo(LineItem lineItem){
        if (priority == lineItem.getPriority()){
            if(type == ItemTypeEnum.Item && lineItem.getType() == ItemTypeEnum.Service) return 1;
            else if(type == ItemTypeEnum.Service && lineItem.getType() == ItemTypeEnum.Item) return -1;
            else return 0;
        }
        return priority - lineItem.getPriority();
    }

    public JSONObject toJson(){
        JSONObject convertedItem = new JSONObject();
        BigDecimal quantityBigDecimal = new BigDecimal(this.getQuantity());

        convertedItem.put("name", this.getName());
        convertedItem.put("price", this.getPrice());
        convertedItem.put("itemId:", this.getItemId());
        convertedItem.put("priority:", this.getPriority());
        convertedItem.put("quantity", this.getQuantity());
        convertedItem.put("subtotal", this.getPrice().multiply(quantityBigDecimal));
        convertedItem.put("description", this.getDescription());
        convertedItem.put("type", this.getType());

        return  convertedItem;
    }

}
