package com.codecool.models;


import com.codecool.models.enums.ItemTypeEnum;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Data
@Entity
@EntityScan
public class OtherItem extends Item{

    private int phase;
    private int priority = 1;

    public OtherItem(String name, String description, Integer price, int phase, ItemTypeEnum type) {
        super(name, description, BigDecimal.valueOf(price), type);
        this.setPhase(phase);
        this.setType(type);
    }

    public OtherItem(String name, String description, BigDecimal price, ItemTypeEnum item) {
        super(name, description, price, item);
    }

    public OtherItem() {
        super();
    }
}
