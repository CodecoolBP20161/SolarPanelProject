package com.codecool.services;

import com.codecool.models.Consumption;
import com.codecool.repositories.ConsumptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumptionService {

    private ConsumptionRepository consumptionRepository;


    public ConsumptionService(ConsumptionRepository consumptionRepository) {
        this.consumptionRepository = consumptionRepository;
    }

    public Consumption saveConsuption(Consumption consumption){
        return consumptionRepository.save(consumption);
    }

    public Consumption getConsumptionByconsumptionID(String consumptionID){
        return consumptionRepository.findByConsumptionID(consumptionID);
    };

    public Long rowCount(){return consumptionRepository.count();};

}
