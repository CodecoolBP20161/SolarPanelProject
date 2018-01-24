package com.codecool.repositories;

import com.codecool.models.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Integer>{
    Consumption findByConsumptionID(String consumptionID);

    Long countByOfferIdNotNull();

}
