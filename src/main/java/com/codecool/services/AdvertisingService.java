package com.codecool.services;

import com.codecool.models.Advertising;
import com.codecool.repositories.AdvertisingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AdvertisingService {

    private AdvertisingRepository advertisingRepository;

    public AdvertisingService(AdvertisingRepository advertisingRepository) {
        this.advertisingRepository = advertisingRepository;
    }

    public void increaseAdvertisement(Integer type){
        System.out.println(type);
        Advertising advertisement = advertisingRepository.findByType(type);
        advertisement.setValue(advertisement.getValue()+1);
        advertisingRepository.save(advertisement);
   }

   public List<Advertising> getAllAdvertisement(){
        return advertisingRepository.findAll();
   }
}
