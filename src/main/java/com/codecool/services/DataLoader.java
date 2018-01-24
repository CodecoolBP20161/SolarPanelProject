package com.codecool.services;


import com.codecool.models.*;
import com.codecool.models.enums.InverterBrandEnum;
import com.codecool.models.enums.ItemTypeEnum;
import com.codecool.repositories.AdvertisingRepository;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.OtherItemRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DataLoader {

    private InverterRepository inverterRep;
    private SolarPanelRepository solarPanelRep;
    private OtherItemRepository otherItemRep;
    private AdvertisingRepository advertisingRep;
    private OfferService offerService;
    // private OfferRepository offerRep;

    private final InverterBrandEnum SOLAREDGE = InverterBrandEnum.SOLAREDGE;
    private final InverterBrandEnum GROWATT = InverterBrandEnum.GROWATT;
    private final InverterBrandEnum FRONIUS = InverterBrandEnum.FRONIUS;
    private final String Amerisolar = "275 W-os Amerisolar polikristályos napelem";


    @Autowired
    public DataLoader(InverterRepository inverterRep, SolarPanelRepository solarPanelRep, OtherItemRepository otherItemRep, AdvertisingRepository advertisingRep, OfferService offerService) {
        this.inverterRep = inverterRep;
        this.solarPanelRep = solarPanelRep;
        this.otherItemRep = otherItemRep;
        this.advertisingRep = advertisingRep;
        this.offerService = offerService;
    }



    @PostConstruct
    public void loadData() {
        loadSolarPanels();
        loadInverters();
        loadOtherItems();
        loadDefaultadvertising();
    }

    private void loadDefaultadvertising(){
        List<Advertising> advertisementList = new ArrayList<>();

        advertisementList.add(new Advertising(1));
        advertisementList.add(new Advertising(2));
        advertisementList.add(new Advertising(3));

        for (Advertising item : advertisementList) {
            advertisingRep.save(item);
        }

    }

    private void loadSolarPanels() {
        List<SolarPanel> solarPanelList = new ArrayList<>();

        //solarPanelList.add(new SolarPanel("270 W-os Amerisolar polikristályos napelem", "van", 39110, 1.27, 270, "12év teljeskörű garancia")); //, 12 évre 91%-os telj.gar., 30 évre 80%-os telj.gar.
        //solarPanelList.add(new SolarPanel("270 W-os Heckert Solar polikristályos napelem", "nincs", 50000, 1.20, 270, ""));
        solarPanelList.add(new SolarPanel("305 W-os ALEO Solar HE Tech napelem", "nincs", 60000, 1.20, 305, ""));
        solarPanelList.add(new SolarPanel("275 W-os Polikristályos napelem", "van", 40000, 1.20, 270, ""));
        //solarPanelList.add(new SolarPanel("270 W-os AXIworldplus SE napelem", "nincs", 55000, 1.20, 270, ""));

        for (SolarPanel item : solarPanelList) {
            solarPanelRep.save(item);
        }
    }

    private List<Inverter> loadSolaredgeInverters() {
        List<Inverter> solaredgeInverterList = new ArrayList<>();

        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE2200-ER-01", 230000, 1, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 2200, "Optimalizáló (P300-5R M4M RS)", 13000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE3000-ER-01", 255000, 1, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 3000, "Optimalizáló (P300-5R M4M RS)", 13000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE4000-ER-01", 300000, 1, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 4000, "Optimalizáló (P300-5R M4M RS)", 13000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE5000-ER-01", 320000, 1, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 5000, "Optimalizáló (P300-5R M4M RS)", 13000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE5K-ER-01", 360000, 3, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 5000, "Optimalizáló (P300-5R M4M RS)", 13000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE7K-ER-01", 400000, 3, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 7000, "Optimalizáló (P300-5R M4M RS)", 13000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE8K-ER-01", 440000, 3, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 8000, "Optimalizáló (P300-5R M4M RS)", 13000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE9K-ER-01", 460000, 3, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 9000, "Optimalizáló (P300-5R M4M RS)", 13000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE10K-ER-01", 480000, 3, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 10000, "Optimalizáló (P300-5R M4M RS)", 13000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE12.5K-ER-01", 500000, 3, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 12500, "Optimalizáló (P300-5R M4M RS)", 13000));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE15K-ER-01", 440000, 3, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 15000, "Optimalizáló (P600-5R M4M RL)", 16500));
        solaredgeInverterList.add(new Inverter(SOLAREDGE, "SE17K-ER-01", 450000, 3, 1.27, "Beépített wifi modullal, 12 év garancia", 15000, 17000, "Optimalizáló (P600-5R M4M RL)", 16500));
        return solaredgeInverterList;
    }


    private List<Inverter> loadGrowattInverters() {
        List<Inverter> growattInverterList = new ArrayList<>();

        growattInverterList.add(new Inverter(GROWATT, "2000S", 150000, 1, 1.27, "", 15000, 2000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "3000S", 185000, 1, 1.27, "", 15000, 3000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "4200MTL-S", 245000, 1, 1.27, "", 15000, 4200, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "5000MTL-S", 255000, 1, 1.27, "", 15000, 5000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "4000UE", 300000, 3, 1.27, "", 15000, 4000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "5000UE", 320000, 3, 1.27, "", 15000, 5000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "6000UE", 340000, 3, 1.27, "", 15000, 6000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "7000UE", 390000, 3, 1.27, "", 15000, 7000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "8000UE", 420000, 3, 1.27, "", 15000, 8000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "9000UE", 440000, 3, 1.27, "", 15000, 9000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "10000UE", 450000, 3, 1.27, "", 15000, 10000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "12000UE", 470000, 3, 1.27, "", 15000, 12000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "18000UE", 620000, 3, 1.27, "", 15000, 18000, "", 0));
        growattInverterList.add(new Inverter(GROWATT, "20000UE", 650000, 3, 1.27, "", 15000, 20000, "", 0));

        return growattInverterList;
    }

    private List<Inverter> loadFroniusInverters() {
        List<Inverter> froniusInverterList = new ArrayList<>();
        froniusInverterList.add(new Inverter(FRONIUS, "GALVO-2.0-1", 253000, 1, 1.2, "", 15000, 2000, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "GALVO-3.0-1", 265000, 1, 1.2, "", 15000, 3000, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "PRIMO-4.0-1", 291000, 1, 1.2, "", 15000, 4000, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "PRIMO-4.6-1", 300000, 1, 1.2, "", 15000, 4600, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "Symo 4.5-3M WEB", 340000, 3, 1.2, "", 15000, 4500, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-5.0-3-M", 350000, 3, 1.2, "", 15000, 5000, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-6.0-3-M", 370000, 3, 1.2, "", 15000, 6000, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-7.0-3-M", 450000, 3, 1.2, "", 15000, 7000, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-8.2-3-M", 484000, 3, 1.2, "", 15000, 8200, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-10.0-3-M", 540000, 3, 1.2, "", 15000, 10000, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-12.5-3-M", 600000, 3, 1.2, "", 15000, 12500, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-15.0-3-M", 680000, 3, 1.2, "", 15000, 15000, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-17.5-3-M", 750000, 3, 1.2, "", 15000, 17500, "", 0));
        froniusInverterList.add(new Inverter(FRONIUS, "SYMO-20.0-3-M", 750000, 3, 1.2, "", 15000, 20000, "", 0));

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

    private void loadOtherItems() {
        List<OtherItem> stuffs = new ArrayList<>();
        stuffs.add(new OtherItem("Tartószerkezet szett (4panel/szett)", "", 30000, 0, ItemTypeEnum.Item));
        stuffs.add(new OtherItem("AC/DC túlfesz és túláram védelem", "", 65000, 1, ItemTypeEnum.Item));
        stuffs.add(new OtherItem("AC/DC túlfesz és túláram védelem", "", 85000, 3, ItemTypeEnum.Item));
        stuffs.add(new OtherItem("Szolár kábel /méter/", "Tervezett mennyiség", 220, 0, ItemTypeEnum.Item));
        stuffs.add(new OtherItem("MC4 Csatlakozó (pár)", "", 600, 0, ItemTypeEnum.Item));
        stuffs.add(new OtherItem("AC vezeték 3x4mm2", "Tervezett mennyiség", 600, 1, ItemTypeEnum.Item));
        stuffs.add(new OtherItem("AC vezeték 5x4mm2", "Tervezett mennyiség", 900, 3, ItemTypeEnum.Item));
        stuffs.add(new OtherItem("16mm2-es MKH vezeték", "Tervezett mennyiség", 440, 0, ItemTypeEnum.Item));
        stuffs.add(new OtherItem("Termék díj", "", 2110, 0, ItemTypeEnum.Service));
        stuffs.add(new OtherItem("Tervezés, engedélyeztetés", "", 45000, 0, ItemTypeEnum.Service));

        for (OtherItem item : stuffs) {
            otherItemRep.save(item);
        }
    }

    public List<ReadyProduct> loadPresetProduct(String brand) {
        ArrayList<ReadyProduct> presetOffers = new ArrayList<>();

        List<String> smallerThan12000Description = Arrays.asList("Engedélyezés", "Termékdíj", "Tervezés, Kivitelezés");
        List<String> moreThan120000Description = Arrays.asList("Engedélyezés", "Termékdíj", "A kivitelezési díjat nem tartalmazza, további egyeztetés szükséges");
        if (brand.equals("growatt")) {
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 2000S").toString(), Amerisolar, 10, offerService.getReadyProductPrice(13,2), smallerThan12000Description, 1, 2));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 3000S").toString(), Amerisolar, 10, offerService.getReadyProductPrice(14,2), smallerThan12000Description, 1, 3));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 4200MTL-S").toString(), Amerisolar, 10, offerService.getReadyProductPrice(15,2), smallerThan12000Description, 1, 4.2));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 5000MTL-S").toString(), Amerisolar, 10, offerService.getReadyProductPrice(16,2), smallerThan12000Description, 1, 5));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 4000UE").toString(), Amerisolar, 10, offerService.getReadyProductPrice(17,2), smallerThan12000Description, 3, 4));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 5000UE").toString(), Amerisolar, 10, offerService.getReadyProductPrice(18,2), smallerThan12000Description, 3, 5));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 6000UE").toString(), Amerisolar, 10, offerService.getReadyProductPrice(19,2), smallerThan12000Description, 3, 6));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 7000UE").toString(), Amerisolar, 10, offerService.getReadyProductPrice(20,2), smallerThan12000Description, 3, 7));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 8000UE").toString(), Amerisolar, 10, offerService.getReadyProductPrice(21,2), smallerThan12000Description, 3, 8));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 9000UE").toString(), Amerisolar, 10, offerService.getReadyProductPrice(22,2), smallerThan12000Description, 3, 9));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 10000UE").toString(), Amerisolar, 10, offerService.getReadyProductPrice(23,2), smallerThan12000Description, 3, 10));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 12000UE").toString(), Amerisolar, 10, 1234567, smallerThan12000Description, 3, 12));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 18000UE").toString(), Amerisolar, 10, 1234567, moreThan120000Description, 3, 18));
            presetOffers.add(new ReadyProduct(new StringBuilder(GROWATT + " 20000UE").toString(), Amerisolar, 10, 1234567, moreThan120000Description, 3, 20));

        } else {
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE2200-ER-01").toString(), Amerisolar, 10, offerService.getReadyProductPrice(1,2), smallerThan12000Description, 1, 2.2));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE3000-ER-01").toString(), Amerisolar, 10, offerService.getReadyProductPrice(2,2), smallerThan12000Description, 1, 3));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE4000-ER-01").toString(), Amerisolar, 10, offerService.getReadyProductPrice(3,2), smallerThan12000Description, 1, 4));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE5000-ER-01").toString(), Amerisolar, 10, offerService.getReadyProductPrice(4,2), smallerThan12000Description, 1, 5));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE5K-ER-01").toString(), Amerisolar, 10, offerService.getReadyProductPrice(5,2), smallerThan12000Description, 3, 5));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE7K-ER-01").toString(), Amerisolar, 10, offerService.getReadyProductPrice(6,2), smallerThan12000Description, 3, 7));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE8K-ER-01").toString(), Amerisolar, 10, offerService.getReadyProductPrice(7,2), smallerThan12000Description, 3, 8));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE9K-ER-01").toString(), Amerisolar, 10, offerService.getReadyProductPrice(8,2), smallerThan12000Description, 3, 9));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE10K-ER-01").toString(), Amerisolar, 10, offerService.getReadyProductPrice(9,2), smallerThan12000Description, 3, 10));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE12.5K-ER-01").toString(), Amerisolar, 10, 1234567, moreThan120000Description, 3, 12.5));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE15K-ER-01").toString(), Amerisolar, 10, 1234567, moreThan120000Description, 3, 15));
            presetOffers.add(new ReadyProduct(new StringBuilder(SOLAREDGE + " SE17K-ER-01").toString(), Amerisolar, 10, 1234567, moreThan120000Description, 3, 17));
        }
        return presetOffers;
    }


        /*if (brand.equals("solaredge")) {
            presetOffers.add(new ReadyProduct(inverterRep.getOne(1).getName(), Amerisolar, 10,
                    (offerService.getFinalPriceForReadyProduct(inverterRep.getOne(1), solarPanelRep.getOne(4), new ConsumptionForm("kWh",2222.0, 1))),
                    smallerThan12000Description, 1, inverterRep.getOne(1).getCapacity()/1000));*/


}
