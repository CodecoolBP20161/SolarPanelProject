package com.codecool.repositories;

import com.codecool.models.OtherItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtherItemRepository extends JpaRepository<OtherItem, Integer> {

    public List<OtherItem> findByPhaseIn(List<Integer> valami);
}
