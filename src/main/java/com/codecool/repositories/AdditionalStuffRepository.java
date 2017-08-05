package com.codecool.repositories;

import com.codecool.models.AdditionalStuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Mate on 2017. 07. 29..
 */
@Repository
public interface AdditionalStuff extends JpaRepository<Integer, AdditionalStuff> {

}
