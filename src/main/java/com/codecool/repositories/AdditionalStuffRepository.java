package com.codecool.repositories;

import com.codecool.models.AdditionalStuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Mate on 2017. 07. 29..
 */
@Repository
public interface AdditionalStuffRepository extends JpaRepository<AdditionalStuff, Integer> {

    public List<AdditionalStuff> findByPhaseIn(List<Integer> valami);
}
