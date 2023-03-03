package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.CourseRateDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseRate;
import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Repository.CourseRateRepo;
import com.swp.onlineLearning.Repository.CourseRepo;
import com.swp.onlineLearning.Service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@Transactional
public class CommentServiceImple implements CommentService {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CourseRateRepo courseRateRepo;
    @Autowired
    private CourseRepo courseRepo;

    @Override
    public HashMap<String, Object> createCourseRate(CourseRateDTO courseRateDTO, String authority) {
        HashMap<String, Object> json = new HashMap<>();

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
}
