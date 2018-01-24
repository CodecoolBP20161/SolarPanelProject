package com.codecool.repositories;

import com.codecool.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OfferRepository extends JpaRepository<Offer, Integer> {
    public Offer findByConsumptionId(String consumptionId);
}
