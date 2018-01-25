package com.codecool.repositories;

import com.codecool.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {
    Offer findByConsumptionId(String consumptionId);

    @Query(value = "SELECT COUNT(*) FROM Offer")
    Long countById();

}
