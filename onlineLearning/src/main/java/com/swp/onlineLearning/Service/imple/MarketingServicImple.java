package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.DTO.MarketingDTO;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.MarketingImage;
import com.swp.onlineLearning.Repository.BlogRepo;
import com.swp.onlineLearning.Repository.MarketingRepo;
import com.swp.onlineLearning.Service.MarketingService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        marketingDTO.setLink(marketingDTO.getLink());

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

}

