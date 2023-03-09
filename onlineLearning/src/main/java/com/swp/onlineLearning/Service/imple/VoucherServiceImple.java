package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.ChangeStatusVoucherDTO;
import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.DTO.CourseTypeDTO;
import com.swp.onlineLearning.DTO.VoucherDTO;
import com.swp.onlineLearning.Model.*;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.VoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
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
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CourseRateRepo courseRateRepo;
    @Value("${voucher.type.course}")
    private String voucherCourse;
    @Value("${voucher.type.typeCourse}")
    private String voucherTypeCourse;
    @Value("${voucher.price.apply.limit}")
    private double priceLimit;

    @Override
    @Transactional
    public HashMap<String, Object> createVoucher(VoucherDTO voucherDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Voucher voucherCheck;
        Voucher voucher;
        if (voucherDTO.getVoucherID() == null) {
            voucherCheck = voucherRepo.findByName(voucherDTO.getName());
            voucher = new Voucher();
            voucher.setStartDate(LocalDateTime.now());
            voucher.setStatus(false);
        } else {
            voucher = voucherRepo.findByVoucherID(voucherDTO.getVoucherID());
            if (voucher == null) {
                log.error("Voucher " + voucherDTO.getVoucherID() + " isn't exist in system to update");
                json.put("msg", "Voucher " + voucherDTO.getVoucherID() + " isn't exist in system to update");
                return json;
            }
            if (voucher.isStatus()) {
                log.error("Voucher " + voucherDTO.getVoucherID() + " is set status active for user, not allow update");
                json.put("msg", "Voucher " + voucherDTO.getVoucherID() + " is set status active for user, not allow update, please change status to inactive");
                return json;
            }
            voucherCheck = voucherRepo.findByNameAndVoucherID(voucherDTO.getName(), voucherDTO.getVoucherID());
        }
        voucherDTO.setAmount(Math.round(voucherDTO.getAmount()*10)/10);
        if (voucherCheck != null) {
            log.error("Name of voucher " + voucherDTO.getName() + " had already exist in system");
            json.put("msg", "Name of voucher " + voucherDTO.getName() + " had already exist in system");
            return json;
        }

        if (voucherDTO.getType().equals(voucherCourse) && voucherDTO.getCourseID() != null) {
            Course course = courseRepo.findByCourseID(voucherDTO.getCourseID());
            if (course == null) {
                log.error("course with id " + voucherDTO.getCourseID() + " isn't exist in system");
                json.put("msg", "course with id " + voucherDTO.getCourseID() + " isn't exist in system");
                return json;
            }
            if (course.getPrice() < priceLimit - 0.1) {
                log.error("course with id " + voucherDTO.getCourseID() + " have price under 10$");
                json.put("msg", "course with id " + voucherDTO.getCourseID() + " have price under 10$");
                return json;
            }
            int coursePrice = Math.round((float) course.getPrice() / 2);
            if (coursePrice <= voucherDTO.getAmount()) {
                log.error("Amount of this voucher isn't allow greater or equal than 50% price " + "(" + coursePrice + ")" + " of course with id " + course.getCourseID());
                json.put("msg", "Amount of this voucher isn't allow greater or equal than 50% price " + "(" + coursePrice + ")" + " of course with id " + course.getCourseID());
                return json;
            }
            voucher.setAmount(voucherDTO.getAmount());
            voucher.setCourse(course);
            voucher.setCourseType(null);
        } else if (voucherDTO.getType().equals(voucherTypeCourse) && voucherDTO.getCourseTypeID() != null) {
            CourseType courseType = courseTypeRepo.findByCourseTypeID(voucherDTO.getCourseTypeID());
            if (courseType == null) {
                log.error("course type with id " + voucherDTO.getCourseTypeID() + " isn't exist in system");
                json.put("msg", "course type with id " + voucherDTO.getCourseTypeID() + " isn't exist in system");
                return json;
            }
            double coursePrice = 0;
            for (Course course : courseType.getCourses()) {
                if (coursePrice == 0) {
                    coursePrice = course.getPrice();
                }
                if (coursePrice > course.getPrice() && course.getPrice() >= priceLimit) {
                    coursePrice = course.getPrice();
                }
            }
            double coursePriceUpperLimit = Math.round((float) coursePrice / 2);
            if (coursePriceUpperLimit <= voucherDTO.getAmount()) {
                log.error("Amount of this voucher isn't allow greater or equal than 50% price (" + coursePriceUpperLimit + ")");
                json.put("msg", "Amount of this voucher isn't allow greater or equal than 50% price (" + coursePriceUpperLimit + ")");
                return json;
            }
            voucher.setAmount(voucherDTO.getAmount());
            voucher.setCourseType(courseType);
            voucher.setCourse(null);
        } else {
            log.error("Invalid type " + voucherDTO.getType());
            json.put("msg", "Invalid type " + voucherDTO.getType());
            return json;
        }
        voucher.setStartApply(voucherDTO.getStartApply());
        voucher.setName(voucherDTO.getName());
        voucher.setDescription(voucherDTO.getDescription());
        voucher.setDuration(voucherDTO.getDuration());

        try {
            voucherRepo.save(voucher);
        } catch (Exception e) {
            log.error("Save Voucher fail \n" + e.getMessage());
            json.put("msg", "Save Voucher fail");
            return json;
        }
        json.put("msg", "Save Voucher successfully");
        json.put("type", true);
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

        List<Voucher> voucherList = vouchers.stream().toList();
        List<VoucherDTO> voucherDTOS = new ArrayList<>();
        VoucherDTO voucherDTO;
        for (Voucher voucher : voucherList) {
            voucherDTO = new VoucherDTO();
            voucherDTO.setVoucherID(voucher.getVoucherID());
            voucherDTO.setAmount(voucher.getAmount());
            voucherDTO.setDuration(voucher.getDuration());
            voucherDTO.setName(voucher.getName());
            voucherDTO.setStartApply(voucher.getStartApply());
            voucherDTO.setStatus(voucher.isStatus());

            voucherDTOS.add(voucherDTO);
        }
        if (voucherDTOS.isEmpty()) {
            json.put("msg", "0 voucher found on system");
        } else {
            json.put("msg", "Get voucher for page " + page);
        }
        json.put("vouchers", voucherDTOS);
        json.put("numPage", totalNumber);
        json.put("type", true);
        log.info("get voucher successfully");
        return json;
    }

    @Override
    @Transactional
    public HashMap<String, Object> changeVoucherStatus(ChangeStatusVoucherDTO changeStatusVoucherDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        if (changeStatusVoucherDTO == null || changeStatusVoucherDTO.getVoucherID() == null) {
            log.error("Invalid voucher value");
            json.put("msg", "Invalid voucher value");
            return json;
        }
        Voucher voucher = voucherRepo.findByVoucherID(changeStatusVoucherDTO.getVoucherID());
        if (voucher == null) {
            log.error("voucher with id " + changeStatusVoucherDTO.getVoucherID() + " isn't exist in system");
            json.put("msg", "voucher with id " + changeStatusVoucherDTO.getVoucherID() + " isn't exist in system");
            return json;
        }
        voucher.setStatus(changeStatusVoucherDTO.getStatus());

        try {
            voucherRepo.save(voucher);
        } catch (Exception e) {
            log.error("voucher with id " + changeStatusVoucherDTO.getVoucherID() + " save fail \n" + e.getMessage());
            json.put("msg", "voucher with id " + changeStatusVoucherDTO.getVoucherID() + " save fail ");
            return json;
        }
        json.put("msg", "Change Status successfully");
        json.put("type", true);
        log.info("Change Status successfully");
        return json;
    }

    @Override
    public HashMap<String, Object> getVoucherForUpdate(Integer voucherID) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (voucherID == null) {
            log.error("Invalid voucher value");
            json.put("msg", "Invalid voucher value");
            return json;
        }
        Voucher voucher = voucherRepo.findByVoucherID(voucherID);
        if (voucher == null) {
            log.error("voucher with id " + voucherID + " isn't exist in system");
            json.put("msg", "voucher with id " + voucherID + " isn't exist in system");
            return json;
        }
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucherID(voucher.getVoucherID());
        voucherDTO.setAmount(voucher.getAmount());
        voucherDTO.setDuration(voucher.getDuration());
        voucherDTO.setName(voucher.getName());
        voucherDTO.setStartApply(voucher.getStartApply());
        voucherDTO.setDescription(voucher.getDescription());

        CourseType courseType = voucher.getCourseType();
        Course course = voucher.getCourse();
        if (courseType != null) {
            voucherDTO.setType(voucherTypeCourse);
            CourseTypeDTO courseTypeDTO = new CourseTypeDTO();
            courseTypeDTO.setCourseTypeName(courseType.getCourseTypeName());
            courseTypeDTO.setCourseTypeID(courseType.getCourseTypeID());
            voucherDTO.setCourseTypeDTO(courseTypeDTO);
        } else if (course != null) {
            voucherDTO.setType(voucherCourse);
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setImage(course.getImage());
            courseDTO.setCourseName(course.getCourseName());
            courseDTO.setCourseID(course.getCourseID());
            courseDTO.setPrice(course.getPrice());
            voucherDTO.setCourseDTO(courseDTO);
        } else {
            log.error("Invalid voucher type");
            json.put("msg", "Invalid voucher type");
            return json;
        }

        json.put("msg", "Get voucher successfully");
        json.put("voucher", voucherDTO);
        json.put("type", true);
        log.info("Get voucher successfully");
        return json;
    }

    @Override
    public HashMap<String, Object> getVoucherForUser(Integer courseID, String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (gmail == null) {
            log.error("Invalid gmail value");
            json.put("msg", "Invalid gmail value");
            return json;
        }
        if (courseID == null) {
            log.error("Invalid course id value");
            json.put("msg", "Invalid course id value");
            return json;
        }
        Course course = courseRepo.findByCourseID(courseID);
        if (course == null) {
            log.error("course with id " + courseID + " isn't exist in system");
            json.put("msg", "course with id " + courseID + " isn't exist in system");
            return json;
        }
        if (course.getPrice() < priceLimit - 0.1) {
            log.info("No voucher for free course or course under 10$");
            json.put("type", true);
            return json;
        }
        Account account = accountRepo.findByGmail(gmail);

        CourseRate courseRate = courseRateRepo.findByCourseAndAccount(course, account);
        if(courseRate!=null){
            log.info("Account with id "+account.getAccountID()+" already enrolled course");
            json.put("type", true);
            return json;
        }
        LocalDateTime startAt = account.getCreateAt();
        long presentDate = Duration.between(startAt.toLocalDate().atStartOfDay(), LocalDateTime.now().toLocalDate().atStartOfDay()).toDays();

        List<Voucher> voucherCourse = voucherRepo.findByCourse(course.getCourseID());
        List<Voucher> voucherCourseType = voucherRepo.findByCourseType(course.getCourseType().getCourseTypeID());

        List<Voucher> vouchers = new ArrayList<>();
        vouchers.addAll(voucherCourseType);
        vouchers.addAll(voucherCourse);
        if (vouchers.isEmpty()) {
            log.info("0 voucher for user founded");
            json.put("type", true);
            return json;
        }

        List<VoucherDTO> voucherDTOS = new ArrayList<>();
        VoucherDTO voucherDTO;

        for (Voucher voucher : vouchers) {
            if (voucher.getStartApply() <= presentDate && (voucher.getStartApply() + voucher.getDuration()) >= presentDate) {
                voucherDTO = new VoucherDTO();
                voucherDTO.setDescription(voucher.getDescription());
                voucherDTO.setVoucherID(voucher.getVoucherID());
                voucherDTO.setAmount(voucher.getAmount());
                voucherDTO.setName(voucher.getName());
                voucherDTO.setValidUntil(voucher.getStartDate().toLocalDate().plusDays(voucher.getStartApply() + voucher.getDuration()));
                voucherDTOS.add(voucherDTO);
            }
        }

        log.info("Get voucher for account " + gmail + " successfully");
        json.put("vouchers", voucherDTOS);
        json.put("type", true);
        return json;
    }
}
