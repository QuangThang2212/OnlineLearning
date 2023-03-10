package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseType;
import com.swp.onlineLearning.Model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepo extends JpaRepository<Voucher, Integer> {
    Voucher findByVoucherID(int id);
    Voucher findByName(String name);
    @Query(nativeQuery = true, value = "SELECT * FROM voucher a where name=?1 and voucherid != ?2")
    Voucher findByNameAndVoucherID(String name, int id);
    Page<Voucher> findAll(Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT * FROM voucher a where courseid=?1 and status=true")
    List<Voucher> findByCourse(int id);
    @Query(nativeQuery = true, value = "SELECT * FROM voucher a where course_typeid=?1 and status=true")
    List<Voucher> findByCourseType(int id);
}
