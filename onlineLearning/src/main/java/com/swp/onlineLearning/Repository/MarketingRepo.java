package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.MarketingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketingRepo extends JpaRepository <MarketingImage,Integer>{
    Optional<MarketingImage> findById(Integer id);
}
