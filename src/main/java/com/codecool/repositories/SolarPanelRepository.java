package com.codecool.repositories;

import com.codecool.models.SolarPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanel, Integer> {
}
