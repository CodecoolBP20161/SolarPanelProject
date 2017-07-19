package com.codecool.repositories;

import com.codecool.models.SolarPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanel, Integer> {
    List<SolarPanel> findAllByOrderByCapacityAscPriceAsc();
}
