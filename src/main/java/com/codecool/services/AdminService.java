package com.codecool.services;

import com.codecool.models.*;
import com.codecool.models.enums.InverterBrandEnum;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.OtherItemRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class AdminService {


    private InverterRepository inverterRepository;
    private SolarPanelRepository solarPanelRepository;
    private OtherItemRepository otherItemRepository;

    @Autowired
    public AdminService(InverterRepository inverterRepository, SolarPanelRepository solarPanelRepository,
                        OtherItemRepository otherItemRepository) {
        this.inverterRepository = inverterRepository;
        this.solarPanelRepository = solarPanelRepository;
        this.otherItemRepository = otherItemRepository;
    }

    public InverterBrandEnum[] getInverterBrands(){
        return InverterBrandEnum.values();
    }

    public List getListOfItems(String type, String brand){

        switch (type){
            case "other":
                return otherItemRepository.findAll();
            case "inverter":
                return inverterRepository.findByBrand(InverterBrandEnum.valueOf(brand));
            case "panel":
                return solarPanelRepository.findAllByOrderByCapacityAscPriceAsc();
        }
        return null;
    }

}
