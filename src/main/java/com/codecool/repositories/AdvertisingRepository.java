package com.codecool.repositories;


import com.codecool.models.Advertising;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisingRepository extends JpaRepository<Advertising, Integer>{
    Advertising findByType(Integer type);
}
