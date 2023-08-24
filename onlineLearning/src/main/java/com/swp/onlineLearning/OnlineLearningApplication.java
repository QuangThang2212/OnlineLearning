package com.swp.onlineLearning;

import com.swp.onlineLearning.Model.LessonType;
import com.swp.onlineLearning.Model.RoleUser;
import com.swp.onlineLearning.Repository.LessonTypeRepo;
import com.swp.onlineLearning.Repository.RoleRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class OnlineLearningApplication {
	@Value("${role.user}")
	private String roleUserName;
	@Value("${role.courseExpert}")
	private String roleCourseExpert;
	@Value("${role.sale}")
	private String roleSale;
	@Value("${role.admin}")
	private String roleAdmin;
	@Value("${lessonType.listening}")
	private String typeListening;
	@Value("${lessonType.reading}")
	private String typeReading;
	@Value("${lessonType.quiz}")
	private String typeQuiz;
	public static void main(String[] args) {
		SpringApplication.run(OnlineLearningApplication.class, args);
	}
	@Bean
	CommandLineRunner run(RoleRepo roleRepo, LessonTypeRepo lessonTypeRepo) {
		return args -> {
			RoleUser roleUser = roleRepo.findByName(roleUserName);
			if(roleUser==null) {
				roleUser = new RoleUser();
				roleUser.setName(roleUserName);
				roleRepo.save(roleUser);
			}

			RoleUser courseExpert = roleRepo.findByName(roleCourseExpert);
			if(courseExpert==null) {
				courseExpert = new RoleUser();
				courseExpert.setName(roleCourseExpert);
				roleRepo.save(courseExpert);
			}

			RoleUser sale = roleRepo.findByName(roleSale);
			if(sale==null) {
				sale = new RoleUser();
				sale.setName(roleSale);
				roleRepo.save(sale);
			}

			RoleUser admin = roleRepo.findByName(roleAdmin);
			if(admin==null) {
				admin = new RoleUser();
				admin.setName(roleAdmin);
				roleRepo.save(admin);
			}
			LessonType reading = lessonTypeRepo.findByName(typeReading);
			if(reading==null) {
				reading = new LessonType();
				reading.setName(typeReading);
				lessonTypeRepo.save(reading);
			}
			LessonType listening = lessonTypeRepo.findByName(typeListening);
			if(listening==null) {
				listening = new LessonType();
				listening.setName(typeListening);
				lessonTypeRepo.save(listening);
			}
			LessonType quiz = lessonTypeRepo.findByName(typeQuiz);
			if(quiz==null) {
				quiz = new LessonType();
				quiz.setName(typeQuiz);
				lessonTypeRepo.save(quiz);
			}
		};
	}
}
