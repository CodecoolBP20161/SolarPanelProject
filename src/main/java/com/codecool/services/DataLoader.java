package com.codecool.services;


import com.codecool.models.Inverter;
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

    private final String SOLAREDGE = "Solaredge";
    private final String GROWATT = "Growatt";
    private final String FRONIUS = "Fronius";


    @Autowired
    public DataLoader(InverterRepository inverterRep, SolarPanelRepository solarPanelRep) {
        this.solarPanelRep = solarPanelRep;
        this.inverterRep = inverterRep;
    }

    @PostConstruct
    public void loadData() {
        loadSolarPanels();
        loadInverters();
    }

    private void loadSolarPanels() {
        List<SolarPanel> solarPanelList = new ArrayList<>();

        solarPanelList.add(new SolarPanel("270 W-os Amerisolar polikristályos napelem", "van", 39110, 1.27, 270, "12év teljeskörű garancia, 12 évre 91%-os telj.gar., 30 évre 80%-os telj.gar."));
        solarPanelList.add(new SolarPanel("270 W-os Heckert Solar polikristályos napelem", "nincs", 50000, 1.20, 270, ""));
        solarPanelList.add(new SolarPanel("305 W-os ALEO Solar HE Tech napelem", "nincs", 60000, 1.20, 305, ""));
        solarPanelList.add(new SolarPanel("270 W-os Polikristályos napelem", "van", 40000, 1.20, 270, ""));
        solarPanelList.add(new SolarPanel("270 W-os AXIworldplus SE napelem", "nincs", 55000, 1.20, 270, ""));

        for (SolarPanel item : solarPanelList) {
            solarPanelRep.save(item);
        }
    }

    private List<Inverter> loadSolaredgeInverters() {
        List<Inverter> solaredgeInverterList = new ArrayList<>();

        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE2200-ER-01+wifi", 230000, 1, 1.27, "12 év garancia", 0, 2200));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE3000-ER-01", 255000, 1, 1.27, "12 év garancia", 0, 3000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE4000-ER-01", 300000, 1, 1.27, "12 év garancia", 0, 4000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE5000-ER-01", 320000, 1, 1.27, "12 év garancia", 0, 5000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE5K-ER-01", 360000, 3, 1.27, "12 év garanacia", 0, 5000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE7K-ER-01", 400000, 3, 1.27, "12 év garancia", 0, 7000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE8K-ER-01", 440000, 3, 1.27, "12 év garancia", 0, 8000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE9K-ER-01", 460000, 3, 1.27, "12 év garancia", 0, 9000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE10K-ER-01", 480000, 3, 1.27, "12 év garancia", 0, 10000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE12.5K-ER-01", 500000, 3, 1.27, "12 év garancia", 0, 12500));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE15K-ER-01", 440000, 3, 1.27, "12 év garancia", 0, 15000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE17K-ER-01", 450000, 3, 1.27, "12 év garancia", 0, 17000));

        return solaredgeInverterList;
    }


    private List<Inverter> loadGrowattInverters() {
        List<Inverter> growattInverterList = new ArrayList<>();

        growattInverterList.add(new Inverter(GROWATT, "2000S", 150000, 1, 1.27, "a", 15000, 2000));
        growattInverterList.add(new Inverter(GROWATT, "3000S", 185000, 1, 1.27, "a", 15000, 3000));
        growattInverterList.add(new Inverter(GROWATT, "4200MTL-S", 245000, 1, 1.27, "a", 15000, 4200));
        growattInverterList.add(new Inverter(GROWATT, "5000MTL-S", 255000, 1, 1.27, "a", 15000, 5000));
        growattInverterList.add(new Inverter(GROWATT, "4000UE", 300000, 3, 1.27, "a", 15000, 4000));
        growattInverterList.add(new Inverter(GROWATT, "5000UE", 320000, 3, 1.27, "a", 15000, 5000));
        growattInverterList.add(new Inverter(GROWATT, "6000UE", 340000, 3, 1.27, "a", 15000, 6000));
        growattInverterList.add(new Inverter(GROWATT, "7000UE", 390000, 3, 1.27, "a", 15000, 7000));
        growattInverterList.add(new Inverter(GROWATT, "8000UE", 420000, 3, 1.27, "a", 15000, 8000));
        growattInverterList.add(new Inverter(GROWATT, "9000UE", 440000, 3, 1.27, "a", 15000, 9000));
        growattInverterList.add(new Inverter(GROWATT, "10000UE", 450000, 3, 1.27, "a", 15000, 10000));
        growattInverterList.add(new Inverter(GROWATT, "12000UE", 470000, 3, 1.27, "a", 15000, 12000));
        growattInverterList.add(new Inverter(GROWATT, "18000UE", 620000, 3, 1.27, "a", 15000, 18000));
        growattInverterList.add(new Inverter(GROWATT, "20000UE", 650000, 3, 1.27, "a", 15000, 20000));

        return growattInverterList;
    }

    private List<Inverter> loadFroniusInverters() {
        List<Inverter> froniusInverterList = new ArrayList<>();
        froniusInverterList.add(new Inverter(FRONIUS, "GALVO-2.0-1",253000, 1, 1.2, "a", 0, 2000));
        froniusInverterList.add(new Inverter(FRONIUS, "GALVO-3.0-1", 265000, 1, 1.2, "a", 0, 3000));
        froniusInverterList.add(new Inverter(FRONIUS, "PRIMO-4.0-1", 291000, 1, 1.2, "a", 0, 4000));
        froniusInverterList.add(new Inverter(FRONIUS, "PRIMO-4.6-1", 300000, 1, 1.2, "a", 0, 4600));
        froniusInverterList.add(new Inverter(FRONIUS, "Symo 4.5-3M WEB", 340000, 3, 1.2, "a", 0, 4500));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-5.0-3-M", 350000, 3, 1.2, "a", 0, 5000));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-6.0-3-M", 370000, 3, 1.2, "a", 0, 6000));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-7.0-3-M", 450000, 3, 1.2, "a",0, 7000));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-8.2-3-M", 484000, 3, 1.2, "a", 0, 8200));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-10.0-3-M", 540000, 3, 1.2, "a", 0, 10000));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-12.5-3-M", 600000, 3, 1.2, "a", 0, 12500));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-15.0-3-M", 680000, 3, 1.2, "a", 0, 15000));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-17.5-3-M", 750000, 3, 1.2, "a", 0, 17500));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-20.0-3-M", 750000, 3, 1.2, "a", 0, 20000));

        return froniusInverterList;
    }

    private void loadInverters() {
        for (Inverter item : loadSolaredgeInverters()) {
            inverterRep.save(item);
        }

        for (Inverter item : loadGrowattInverters()) {
            inverterRep.save(item);
        }

        for (Inverter item : loadFroniusInverters()) {
            inverterRep.save(item);
        }
    }
}
