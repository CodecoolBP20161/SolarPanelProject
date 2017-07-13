package com.codecool.services;


import com.codecool.models.SolarPanel;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.SolarPanelRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class DataLoader {

    private InverterRepository inverterRep;
    private SolarPanelRepository solarPanelRep;

    @Autowired
    public DataLoader (InverterRepository inverterRep, SolarPanelRepository solarPanelRep){
       this.solarPanelRep = solarPanelRep;
       this.inverterRep = inverterRep;
    }
    @PostConstruct
    public void loadData(){
        loadSolarPanels();
        loadInverters();
    }

    private void loadSolarPanels(){
//        SolarPanel panel = new SolarPanel();
//        solarPanelRep.save(panel);
    }

    private void loadInverters(){

    }
}
