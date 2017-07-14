package com.codecool.services;


import com.codecool.models.SolarPanel;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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
        List<SolarPanel> solarPanelList = new ArrayList<>();
        solarPanelList.add(new SolarPanel("270W-os Amerisolar polikristályos napelem", "van",39110,1.27, 270,"12év teljeskörű garancia, 12 évre 91%-os telj.gar., 30 évre 80%-os telj.gar."));
        solarPanelList.add(new SolarPanel("260W-os Amerisolar polikristályos napelem", "van",39110,1.27,260,"12év teljeskörű garancia, 12 évre 91%-os telj.gar., 30 évre 80%-os telj.gar."));
        solarPanelList.add(new SolarPanel("265W-os Amerisolar polikristályos napelem", "nincs",37000,1.27,265,""));
        solarPanelList.add(new SolarPanel("270W-os Heckert Solar polikristályos napelem", "nincs",50000,1.20,270,""));
        solarPanelList.add(new SolarPanel("260W-os polikristályos napelem", "nincs",40000,1.20,260,""));
        solarPanelList.add(new SolarPanel("270W-os Heckert Solar polikristályos napelem", "nincs",50000,1.20,270,""));
        solarPanelList.add(new SolarPanel("ALEO Solar HE Tech 305W napelem", "nincs",60000,1.20,305,""));
        solarPanelList.add(new SolarPanel("270W-os polikristályos napelem", "van",40000,1.20,270,""));
        solarPanelList.add(new SolarPanel("265W-os polikristályos napelem", "nincs",40000,1.20,265,""));
        solarPanelList.add(new SolarPanel("AXIworldplus SE 270W", "nincs",55000,1.20,270,""));
        solarPanelList.add(new SolarPanel("265W-os polikristályos napelem", "nincs",40000,1.20,265,""));


        for(SolarPanel item : solarPanelList) {
            solarPanelRep.save(item);
        }
    }

    private void loadInverters(){
    }
}
