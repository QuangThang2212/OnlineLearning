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
}
