package com.codecool.models;


import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@EntityScan
@Data
@Table(name = "advertising")
public class Advertising implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer type;

    private Integer value;

    public Advertising(Integer type) {
        this.setType(type);
        this.setValue(0);
    }

    public Advertising() {
    }
}
