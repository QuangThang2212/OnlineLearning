package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.VoucherDTO;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseType;
import com.swp.onlineLearning.Model.Voucher;
import com.swp.onlineLearning.Repository.CourseRepo;
import com.swp.onlineLearning.Repository.CourseTypeRepo;
import com.swp.onlineLearning.Repository.VoucherRepo;
import com.swp.onlineLearning.Service.VoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class VoucherServiceImple implements VoucherService {
    @Autowired
    private VoucherRepo voucherRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private CourseTypeRepo courseTypeRepo;
    @Value("${voucher.type.course}")
    private String voucherCourse;
    @Value("${voucher.type.typeCourse}")
    private String voucherTypeCourse;
    @Override
    public HashMap<String, Object> createVoucher(VoucherDTO voucherDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Voucher voucherCheck;
        Voucher voucher;
        if(voucherDTO.getVoucherID()==null){
            voucherCheck = voucherRepo.findByName(voucherDTO.getName());
            voucher = new Voucher();
            voucher.setStartDate(LocalDateTime.now());
            voucher.setStatus(false);
        }else{
            voucher = voucherRepo.findByVoucherID(voucherDTO.getVoucherID());
            if(voucher==null){
                log.error("Voucher "+voucherDTO.getVoucherID()+" isn't exist in system to update");
                json.put("msg", "Voucher "+voucherDTO.getVoucherID()+" isn't exist in system to update");
                return json;
            }
            if(voucher.isStatus()){
                log.error("Voucher "+voucherDTO.getVoucherID()+" is set status active for user, not allow update");
                json.put("msg", "Voucher "+voucherDTO.getVoucherID()+" is set status active for user, not allow update, please change status to inactive");
                return json;
            }
            voucherCheck = voucherRepo.findByNameAndVoucherID(voucherDTO.getName(), voucherDTO.getVoucherID());
        }
        if(voucherCheck!=null){
            log.error("Name of voucher "+voucherDTO.getName()+" had already exist in system");
            json.put("msg", "Name of voucher "+voucherDTO.getName()+" had already exist in system");
            return json;
        }

        if(voucherDTO.getType().equals(voucherCourse) && voucherDTO.getCourseID()!=null){
            Course course = courseRepo.findByCourseID(voucherDTO.getCourseID());
            if(course==null){
                log.error("course with id "+voucherDTO.getCourseID()+" isn't exist in system");
                json.put("msg", "course with id "+voucherDTO.getCourseID()+" isn't exist in system");
                return json;
            }
            int coursePrice = Math.round((float)course.getPrice()/2);
            if(coursePrice<=voucherDTO.getAmount()){
                log.error("Amount of this voucher isn't allow greater or equal than 50% price "+"("+coursePrice+")"+" of course with id "+ course.getCourseID());
                json.put("msg", "Amount of this voucher isn't allow greater or equal than 50% price "+"("+coursePrice+")"+" of course with id "+ course.getCourseID());
                return json;
            }
            voucher.setAmount(voucherDTO.getAmount());
            voucher.setCourse(course);
            voucher.setCourseType(null);
        }else if(voucherDTO.getType().equals(voucherTypeCourse) && voucherDTO.getCourseTypeID()!=null){
            CourseType courseType = courseTypeRepo.findByCourseTypeID(voucherDTO.getCourseTypeID());
            if(courseType==null){
                log.error("course type with id "+voucherDTO.getCourseTypeID()+" isn't exist in system");
                json.put("msg", "course type with id "+voucherDTO.getCourseTypeID()+" isn't exist in system");
                return json;
            }
            int coursePrice;
            for(Course course : courseType.getCourses()){
                coursePrice = Math.round((float) course.getPrice() / 2);
                if (coursePrice <= voucherDTO.getAmount()) {
                    log.error("Amount of this voucher isn't allow greater or equal than 50% price of course with id " + course.getCourseID());
                    json.put("msg", "Amount of this voucher isn't allow greater or equal than 50% price of course with id " + course.getCourseID());
                    return json;
                }
            }
            voucher.setAmount(voucherDTO.getAmount());
            voucher.setCourseType(courseType);
            voucher.setCourse(null);
        }else{
            log.error("Invalid type "+ voucherDTO.getType());
            json.put("msg","Invalid type "+ voucherDTO.getType());
            return json;
        }
        voucher.setStartApply(voucherDTO.getStartApply());
        voucher.setName(voucherDTO.getName());
        voucher.setDescription(voucherDTO.getDescription());
        voucher.setDuration(voucherDTO.getDuration());

        try{
            voucherRepo.save(voucher);
        }catch (Exception e){
            log.error("Save Voucher fail \n"+e.getMessage());
            json.put("msg","Save Voucher fail");
            return json;
        }
        json.put("msg", "Save Voucher successfully");
        json.put("type",true);
        return json;
    }

    @Override
    public HashMap<String, Object> getAllVoucher(int page, int size) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (page < 1 || size < 1) {
            log.error("Invalid page " + page + " or size " + size);
            json.put("msg", "Invalid page " + page + " or size " + size);
            return json;
        }
        int totalNumber = voucherRepo.findAll(PageRequest.of(page - 1, size)).getTotalPages();
        if (totalNumber == 0) {
            log.error("0 voucher founded");
            json.put("msg", "0 voucher founded for page");
            return json;
        } else if (page > totalNumber) {
            log.error("invalid page " + page);
            json.put("msg", "invalid page " + page);
            return json;
        }

        Page<Voucher> vouchers = voucherRepo.findAll(PageRequest.of(page - 1, size));
        if (vouchers.isEmpty()) {
            log.error("0 voucher founded for page " + page);
            json.put("msg", "0 voucher founded for page " + page);
            return json;
        }

        List<VoucherDTO> voucherList = new ArrayList<>();
        return null;
    }
}
