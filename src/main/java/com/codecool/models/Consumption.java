package com.codecool.models;

import com.codecool.models.enums.CompanyEnum;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;


@Entity
@EntityScan
@Data
@Table
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String consumptionID;
    private String metric;
    private Double value;
    private Integer advertisement;
    private String inverterId;
    private String panelId;
    private Long offerId;
    private Boolean alreadyGetOffer;

    private int phase = 1;
    private CompanyEnum company = CompanyEnum.TraditionalSolutions;
    private static Map<Integer, Integer> allAdvertisement = new HashMap<>();

    static {
        allAdvertisement.put(1,0);
        allAdvertisement.put(2,0);
        allAdvertisement.put(3,0);
    }

    public Consumption() {
    }

    public Consumption(String metric, Double value, Integer advertisement, Integer phase) {
        this.metric = metric;
        this.value = value;
        this.advertisement = advertisement;
        this.phase = phase;
        this.alreadyGetOffer = false;
    }

    public void addNewAdvertisement(Integer advertisement){
        allAdvertisement.put(advertisement, allAdvertisement.get(advertisement) + 1);
        System.out.println(allAdvertisement.toString());
    }

    public boolean isValid(){return this.getInverterId() != null || this.getPanelId() != null; }

}
