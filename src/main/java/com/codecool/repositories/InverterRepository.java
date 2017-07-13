package com.codecool.repositories;

import com.codecool.models.Inverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InverterRepository extends JpaRepository<Inverter, Integer> {
}
