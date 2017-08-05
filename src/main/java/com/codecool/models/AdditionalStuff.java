package com.codecool.models;


import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Data
@Entity
@EntityScan
public class AdditionalStuff extends Item{

/*    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;*/

    private int phase;
    private String type;

    public AdditionalStuff(String name, String description, Integer price, int phase, String type) {
        super(name, description, BigDecimal.valueOf(price));
        this.setPhase(phase);
        this.setType(type);
    }

    public AdditionalStuff(String name, String description, BigDecimal price) {
        super(name, description, price);
    }

    public AdditionalStuff() {
    }
}
