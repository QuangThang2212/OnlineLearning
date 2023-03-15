package com.swp.onlineLearning.Repository.Imple;

import com.swp.onlineLearning.DTO.CourseFilterObjectDTO;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseType;
import com.swp.onlineLearning.Repository.CourseFilterRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class CourseFilterRepoImple implements CourseFilterRepo {
    @PersistenceContext
    EntityManager em;
    @Value("${course.filter.sort.in_star}")
    private String inStar;
    @Value("${course.filter.sort.de_star}")
    private String deStar;
    @Value("${course.filter.sort.in_date}")
    private String inDate;
    @Value("${course.filter.sort.de_date}")
    private String deDate;

    public CourseFilterObjectDTO findCourseFilter(Integer typeFilter, String sort, Boolean kind, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);

        Root<Course> courseRoot = cq.from(Course.class);

        Order order = null;

        if (typeFilter != null) {
            Join<Course, CourseType> foreignKeyJoin = courseRoot.join("courseType");
            Predicate typeId = cb.equal(foreignKeyJoin.get("courseTypeID"), typeFilter);

            if (kind == null) {
                if (sort.equals(deStar)) {
                    order = cb.desc(courseRoot.get("starRated"));
                } else if (sort.equals(inStar)) {
                    order = cb.asc(courseRoot.get("starRated"));
                } else if (sort.equals(deDate)) {
                    order = cb.desc(courseRoot.get("createDate"));
                } else if (sort.equals(inDate)) {
                    order = cb.asc(courseRoot.get("createDate"));
                }
                cq.where(typeId);
                if (order != null) {
                    cq.orderBy(order);
                }
            } else if (!kind) {
                Predicate price = cb.notEqual(courseRoot.get("price"), 0);

                if (sort.equals(deStar)) {
                    order = cb.desc(courseRoot.get("starRated"));
                } else if (sort.equals(inStar)) {
                    order = cb.asc(courseRoot.get("starRated"));
                } else if (sort.equals(deDate)) {
                    order = cb.desc(courseRoot.get("createDate"));
                } else if (sort.equals(inDate)) {
                    order = cb.asc(courseRoot.get("createDate"));
                }
                cq.where(cb.and(typeId, price));
                if (order != null) {
                    cq.orderBy(order);
                }
            } else {
                Predicate price = cb.equal(courseRoot.get("price"), 0);

                if (sort.equals(deStar)) {
                    order = cb.desc(courseRoot.get("starRated"));
                } else if (sort.equals(inStar)) {
                    order = cb.asc(courseRoot.get("starRated"));
                } else if (sort.equals(deDate)) {
                    order = cb.desc(courseRoot.get("createDate"));
                } else if (sort.equals(inDate)) {
                    order = cb.asc(courseRoot.get("createDate"));
                }
                cq.where(cb.and(typeId, price));
                if (order != null) {
                    cq.orderBy(order);
                }
            }
        } else {
            if (kind == null) {
                if (sort.equals(deStar)) {
                    order = cb.desc(courseRoot.get("starRated"));
                } else if (sort.equals(inStar)) {
                    order = cb.asc(courseRoot.get("starRated"));
                } else if (sort.equals(deDate)) {
                    order = cb.desc(courseRoot.get("createDate"));
                } else if (sort.equals(inDate)) {
                    order = cb.asc(courseRoot.get("createDate"));
                }
                if (order != null) {
                    cq.orderBy(order);
                }
            } else if (!kind) {
                Predicate price = cb.notEqual(courseRoot.get("price"), 0);
                cq.where(price);

                if (sort.equals(deStar)) {
                    order = cb.desc(courseRoot.get("starRated"));
                } else if (sort.equals(inStar)) {
                    order = cb.asc(courseRoot.get("starRated"));
                } else if (sort.equals(deDate)) {
                    order = cb.desc(courseRoot.get("createDate"));
                } else if (sort.equals(inDate)) {
                    order = cb.asc(courseRoot.get("createDate"));
                }
                if (order != null) {
                    cq.orderBy(order);
                }
            } else {
                Predicate price = cb.equal(courseRoot.get("price"), 0);
                cq.where(price);

                if (sort.equals(deStar)) {
                    order = cb.desc(courseRoot.get("starRated"));
                } else if (sort.equals(inStar)) {
                    order = cb.asc(courseRoot.get("starRated"));
                } else if (sort.equals(deDate)) {
                    order = cb.desc(courseRoot.get("createDate"));
                } else if (sort.equals(inDate)) {
                    order = cb.asc(courseRoot.get("createDate"));
                }
                if (order != null) {
                    cq.orderBy(order);
                }
            }
        }
        TypedQuery<Course> query = em.createQuery(cq);
        List<Course> totalCount = query.getResultList();
        int total = totalCount.size();
        for (Course course : totalCount) {
            System.out.println("Course:  name:" + course.getCourseName() + ", date:" + course.getCreateDate() + ", price:" + course.getPrice() + ", type:" + course.getCourseType().getCourseTypeID());
        }

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<Course> courses = query.getResultList();
        Page<Course> coursePage = new PageImpl<>(courses, pageable, courses.size());

        int totalPages = (int) Math.ceil((double) total / pageable.getPageSize());

        CourseFilterObjectDTO courseFilterObjectDTO = new CourseFilterObjectDTO();
        courseFilterObjectDTO.setCourses(coursePage);
        courseFilterObjectDTO.setTotal(totalPages);
        return courseFilterObjectDTO;
    }
}
