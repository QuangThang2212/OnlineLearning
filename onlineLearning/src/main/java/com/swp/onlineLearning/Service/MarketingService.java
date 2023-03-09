package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.MarketingDTO;


import java.util.HashMap;

public interface MarketingService {
    HashMap<String, Object> save(MarketingDTO marketingDTO);
    HashMap<String, Object> findAll();
}
