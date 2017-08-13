package com.codecool.repositories;

import com.codecool.models.Inverter;
import com.codecool.models.enums.InverterBrandEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InverterRepository extends JpaRepository<Inverter, Integer> {

    @Query(value = "SELECT inv FROM Inverter inv " +
            "WHERE inv.phase = :phase  AND inv.capacity >= :capacity AND inv.capacity <= (:capacity + 999) " +
            "ORDER BY inv.price ASC, inv.brand ASC")
    List<Inverter> findByCustomerValue(@Param("capacity") int capacity, @Param("phase") int phase);


    @Query(value =
            "SELECT inv FROM Inverter inv " +
            "WHERE inv.capacity >= 11000 " +
            "ORDER BY inv.capacity ASC")

    List<Inverter> findAllByOrderByCapacity();

    List<Inverter> findByBrandAndPhase(InverterBrandEnum brandEnum, int phase);
    List<Inverter> findByBrand(InverterBrandEnum brandEnum);
}
