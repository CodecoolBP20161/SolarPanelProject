package com.codecool.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "solarpanel")
public class SolarPanel extends Item{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String optimalizer;

    @NotNull
    private Double vat;

    @NotNull
    private Integer capacity;

    public SolarPanel(String name, String optimalizer, Integer price, Double vat, Integer capacity, String description) {
        super(name, description, BigDecimal.valueOf(price));
        this.setOptimalizer(optimalizer);
        this.setVat(vat);
        this.setCapacity(capacity);
    }
}
