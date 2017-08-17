package com.codecool.models;

import com.codecool.models.enums.ItemTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@MappedSuperclass
@Data
public abstract class Item implements Comparable<Item> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public Integer priority;

    @NotEmpty
    String name;

    String description;

    @NotNull
    BigDecimal price;

    @NotNull
    ItemTypeEnum type;

    public Item(String name, String description, BigDecimal price, ItemTypeEnum type){
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setType(type);
    }

    public Item(){}

    public int compareTo(Item item){
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if (this == item) return EQUAL;
        if (item.priority == null) return AFTER;
        if(priority > item.priority) return AFTER;
        else if(priority < item.priority) return BEFORE;
        else return EQUAL;
    }
}
