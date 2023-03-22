package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
@Service
@Slf4j
public class DashboardSeviceImple implements DashboardService {
    @Autowired
    private AccountRepo accountRepo;
    @Override
    public HashMap<String, Object> returnDashboard() {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        int totalAccount = accountRepo.countTotalAccount();

        return null;
    }
}
