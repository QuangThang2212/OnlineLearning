package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.MarketingDTO;
import com.swp.onlineLearning.Model.MarketingImage;
import com.swp.onlineLearning.Repository.MarketingRepo;
import com.swp.onlineLearning.Service.MarketingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MarketingServicImple implements MarketingService {
    @Autowired
    private MarketingRepo marketingRepo;
    @Override
    public HashMap<String, Object> save(MarketingDTO marketingDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (marketingDTO == null) {
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }
        MarketingImage marketingImage = new MarketingImage();
        marketingImage.setLink(marketingDTO.getLink());

        try {
            marketingRepo.save(marketingImage);
        } catch (Exception e) {
            log.error("Save marketing " + marketingImage.getLink() + " fail\n" + e.getMessage());
            json.put("msg", "Save marketing " + marketingImage.getLink() + " fail");
            return json;
        }
        log.info("Saving marketing:" + marketingImage.getLink() + " successfully");
        json.put("msg", "Saving marketing:" + marketingImage.getLink() + " successfully");
        json.replace("type", true);

        return json;
    }

    @Override
    public HashMap<String, Object> findAll() {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        MarketingDTO dto;

        List<MarketingImage> marketingImages = marketingRepo.findAll();
        if(marketingImages.isEmpty()){
            log.error("No image founded");
            json.put("msg", "No image founded");
            return json;
        }
        List<MarketingDTO> marketingDTOS = new ArrayList<>();
        for(MarketingImage marketingImage : marketingImages){
            dto = new MarketingDTO();
            dto.setId(marketingImage.getId());
            dto.setLink(marketingImage.getLink());

            marketingDTOS.add(dto);
        }
        log.info("Get list of image successfully");
        json.put("msg","Get list of image successfully");
        json.put("marketingImage", marketingDTOS);
        json.replace("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> delete(Integer id) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        if(id==null){
            log.error("Invalid image id");
            json.put("msg", "Invalid image id");
            return json;
        }
        Optional<MarketingImage> marketingImage = marketingRepo.findById(id);
        if(marketingImage.isEmpty()){
            log.error("Invalid image id");
            json.put("msg", "Invalid image id");
            return json;
        }
        try{
            marketingRepo.delete(marketingImage.get());
        }catch (Exception e){
            log.error("Delete image with id "+id+"\n"+e.getMessage());
            json.put("msg", "Delete image with id "+id);
            return json;
        }
        log.info("Delete successfully");
        json.put("msg","Delete successfully");
        json.replace("type", true);
        return json;
    }

}

