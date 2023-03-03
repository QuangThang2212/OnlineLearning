package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.CourseRateDTO;
import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseRate;
import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Repository.CourseRateRepo;
import com.swp.onlineLearning.Repository.CourseRepo;
import com.swp.onlineLearning.Service.CourseRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class CourseRateServiceImple implements CourseRateService {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CourseRateRepo courseRateRepo;
    @Autowired
    private CourseRepo courseRepo;

    @Override
    public HashMap<String, Object> createCourseRate(CourseRateDTO courseRateDTO, String authority) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Account account = accountRepo.findByGmail(authority);
        Course course = courseRepo.findByCourseID(courseRateDTO.getCourseID());
        if (course == null) {
            log.error("Invalid course");
            json.put("msg", "Invalid course ID");
            return json;
        }
        CourseRate courseRate = courseRateRepo.findByCourseAndAccount(course, account);
        if (courseRate == null) {
            log.error("Account with ID: " + account.getAccountID() + " doesn't enroll course with ID " + course.getCourseID());
            json.put("msg", "You doesn't enroll this course before, please enroll to comment and rate");
            return json;
        }
        courseRate.setContent(courseRateDTO.getContent());
        courseRate.setStarRate(courseRateDTO.getStarRate());

        try {
            courseRateRepo.save(courseRate);
        } catch (Exception e) {
            log.error("Create rate for course with ID " + course.getCourseID() + " by account " + account.getAccountID() + " fail");
            json.put("msg", "Create rate for course fail");
            return json;
        }

        List<CourseRate> courseRates = courseRateRepo.findByCourse(course);
        float starRate;
        int totalRate = 0;
        for (CourseRate rate : courseRates) {
            totalRate += rate.getStarRate();
        }
        starRate = (float) totalRate / courseRates.size();
        course.setStarRated(starRate);

        try {
            courseRepo.save(course);
        } catch (Exception e) {
            log.error("Update rate for course with ID " + course.getCourseID() + " fail");
            json.put("msg", "Update rate for course fail");
            return json;
        }

        json.put("msg", "Thank you for your review");
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> getCourseRate(int courseID, int page, int limit) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (page < 1 || limit < 1) {
            log.error("Invalid page " + page + " or size " + limit);
            json.put("msg", "Invalid page " + page + " or size " + limit);
            return json;
        }
        Course course = courseRepo.findByCourseID(courseID);
        if (course == null) {
            log.error("Invalid course");
            json.put("msg", "Invalid course ID");
            return json;
        }
        int totalNumber = courseRateRepo.findByCourse(course.getCourseID(),PageRequest.of(page - 1, limit)).getTotalPages();
        Page<CourseRate> courseRates = courseRateRepo.findByCourse(course.getCourseID(),PageRequest.of(page - 1, limit));
        if(!courseRates.isEmpty()){
            CourseRateDTO courseRateDTO;
            List<CourseRateDTO> courseRateDTOS = new ArrayList<>();
            Account account;
            for(CourseRate rate : courseRates.stream().toList()){
                courseRateDTO = new CourseRateDTO();
                courseRateDTO.setCourseRateID(rate.getCourseRateID());
                courseRateDTO.setStarRate(rate.getStarRate());
                courseRateDTO.setContent(rate.getContent());

                account = rate.getAccount();
                courseRateDTO.setUserID(account.getAccountID());
                courseRateDTO.setUserName(account.getName());
                courseRateDTO.setUserImage(account.getImage());

                courseRateDTOS.add(courseRateDTO);
            }
            json.put("rating", courseRateDTOS);
        }
        json.put("numPage", totalNumber);
        json.put("type", true);
        return json;
    }
}
