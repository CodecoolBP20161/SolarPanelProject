package com.codecool.services;


import com.codecool.models.Inverter;
import com.codecool.models.SolarPanel;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OfferService {

    private InverterRepository inverterRepository;
    private SolarPanelRepository solarPanelRepository;

    @Autowired
    public OfferService(InverterRepository inverterRepository, SolarPanelRepository solarPanelRepository) {
        this.inverterRepository = inverterRepository;
        this.solarPanelRepository = solarPanelRepository;
    }

    public List<Inverter> callculateInverterList(Integer value) {
        List<Inverter> inverterList = (value >= 11 &
                inverterRepository.findByCustomerValue(((int) value) *1000).size() == 0) ?
                inverterRepository.findAllOvertTenThousand():
                inverterRepository.findByCustomerValue(((int) value) *1000);
        return inverterList;
    }

    public List<SolarPanel> getSolarPanelList() {
        return solarPanelRepository.findAllByOrderByCapacityAscPriceAsc();
    }
}
