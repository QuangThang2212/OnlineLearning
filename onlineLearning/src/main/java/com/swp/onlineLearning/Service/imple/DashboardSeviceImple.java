package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.Model.RoleUser;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class DashboardSeviceImple implements DashboardService {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private BlogRepo blogRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Value("${role.sale}")
    private String roleSale;
    @Value("${role.courseExpert}")
    private String roleCourseExpert;
    @Override
    public HashMap<String, Object> returnDashboard() {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        int totalAccount = accountRepo.countTotalAccount();
        double totalAmount = paymentRepo.totalAmount();
        int totalCourse = courseRepo.totalCourse();
        int totalBlog = blogRepo.totalBlog();

        RoleUser sale = roleRepo.findByName(roleSale);
        int totalSale = accountRepo.totalByRole(sale.getRoleID());

        RoleUser expert = roleRepo.findByName(roleCourseExpert);
        int totalExpert = accountRepo.totalByRole(expert.getRoleID());

        return null;
    }
}
