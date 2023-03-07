package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.RoleDTO;
import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.CourseRate;
import com.swp.onlineLearning.Model.RoleUser;
import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Repository.RoleRepo;
import com.swp.onlineLearning.Service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Transactional
public class AccountServiceImple implements AccountService, UserDetailsService {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Value("${role.user}")
    private String roleUser;
    @Value("${role.courseExpert}")
    private String roleCourseExpert;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username==null || username.isEmpty()){
            log.error("User name input null");
            throw new UsernameNotFoundException("User name input null");
        }
        Account account = accountRepo.findByGmail(username);
        if(account == null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }else{
            log.info("User found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRoleUser().getName()));
        return new org.springframework
                .security.core
                .userdetails
                .User(account.getName(), account.getPassword(), authorities);
    }
    @Override
    public HashMap<String, Object> findAllExcept(String gmail, int pageNumber, int size) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        if(pageNumber<1 || size <1){
            log.error("Invalid page "+pageNumber+" or size "+size);
            json.put("msg", "Invalid page "+pageNumber+" or size "+size);
            return json;
        }

        int totalNumber = accountRepo.findAllExcept(gmail, PageRequest.of(pageNumber-1,size)).getTotalPages();
        if(totalNumber==0){
            log.error("0 account founded");
            json.put("msg", "0 account founded for page");
            return json;
        }else if(pageNumber>totalNumber){
            log.error("invalid page "+pageNumber);
            json.put("msg", "invalid page "+pageNumber);
            return json;
        }
        
        Page<Account> accounts = accountRepo.findAllExcept(gmail, PageRequest.of(pageNumber-1,size));
        if(accounts.isEmpty()){
            log.error("0 account founded for page "+pageNumber);
            json.put("msg", "0 account founded for page "+pageNumber);
            return json;
        }
        List<Account> list = accounts.stream().toList();

        List<UserDTO> userDTOs = new ArrayList<>();
        for(Account a : list){
            UserDTO userDTO = new UserDTO();
            userDTO.setAccountID(a.getAccountID());
            userDTO.setName(a.getName().trim());
            userDTO.setGmail(a.getGmail().trim());
            userDTO.setImage(a.getImage());
            userDTO.setRole(a.getRoleUser().getName());

            userDTOs.add(userDTO);
        }
        
        json.put("users",userDTOs);
        json.put("numPage",totalNumber);
        json.put("type",true);
        return json;
    }

    @Override
    public Account findByGmail(String gmail) {
        return accountRepo.findByGmail(gmail);
    }

    @Override
    public HashMap<String, Object> save(UserDTO userDTO){
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        if(userDTO==null){
            log.error("Not allow null account to register");
            json.put("msg", "Not allow null account to register");
            return json;
        }

        //object validation
        userDTO.setCreateAt(LocalDateTime.now());
        userDTO.setBanStatus(false);
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setGmail(userDTO.getGmail().trim());

        ModelMapper modelMapper = new ModelMapper();
        Account account = new Account();
        modelMapper.map(userDTO, account);

        RoleUser role = roleRepo.findByName(roleUser);
        if(role==null){
            log.error("Role "+roleUser+" isn't exist");
            json.put("msg", "Role "+roleUser+" isn't exist");
            return json;
        }else{
            account.setRoleUser(role);
        }

        //check exits mail
        Account checkMailExit = accountRepo.findByGmail(account.getGmail());
        if(checkMailExit!=null){
            log.error(account.getGmail()+" had already registered in system, ");
            json.put("msg", account.getGmail()+" had already registered in system");
            return json;
        }

        //save account
        try {
            accountRepo.save(account);
        }catch (Exception e){
            log.error("Save user with gmail " + account.getGmail()+" fail\n" + e.getMessage());
            json.put("msg", "Save user with gmail "+ account.getGmail()+" fail");
            return json;
        }
        log.info("Saving new user with email:"+ account.getGmail()+" successfully");

        json.put("msg", "Register successfully, please login for more service and voucher");
        json.replace("type",true);
        return json;
    }

    @Override
    public HashMap<String, Object> activeAccount(UserDTO userDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        if(userDTO==null){
            log.error("Not allow null account to register");
            json.put("msg", "Not allow null account to register");
            return json;
        }

        return null;
    }

    @Override
    public HashMap<String, Object> update(int id) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);


        return null;
    }

    @Override
    public HashMap<String, Object> changRole(RoleDTO roleDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        if(roleDTO==null){
            log.error("Not allow null account to register");
            json.put("msg", "Not allow null account to register");
            return json;
        }
        RoleUser roleUser = roleRepo.findByName(roleDTO.getName());
        if(roleUser == null){
            log.error("This role \""+roleDTO.getName()+"\" not exist in the system");
            json.put("msg", "This role \""+roleDTO.getName()+"\" not exist in the system");
            return json;
        }
        Account account = accountRepo.getById(roleDTO.getAccountID());
        if(account == null){
            log.error("Account with id="+roleDTO.getAccountID()+" not exist in the system");
            json.put("msg", "Account with id="+roleDTO.getAccountID()+" not exist in the system");
            return json;
        }
        List<CourseRate> courseRate = account.getCourseRates();
        if(!courseRate.isEmpty()){
            log.error("Account with id="+roleDTO.getAccountID()+" had enrolled course on system, not allow change role to "+roleDTO.getName());
            json.put("msg", "Account with id="+roleDTO.getAccountID()+" had enrolled course on system, not allow change role to "+roleDTO.getName());
            return json;
        }

        try{
            account.setRoleUser(roleUser);
            accountRepo.save(account);
        }catch (Exception e) {
            log.error("Change role for account with id="+roleDTO.getAccountID()+" fail\n" +e.getMessage());
            json.put("msg", "Change role for account with id="+roleDTO.getAccountID()+" fail");
            return json;
        }
        log.error("Change role for account with id="+roleDTO.getAccountID()+" successfully");
        json.put("msg", "Change role for account with id="+roleDTO.getAccountID()+" successfully");
        json.put("type",true);
        return json;
    }

    @Override
    public HashMap<String, Object> findBAllCourseExpert() {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        if(roleCourseExpert.equals("")){
            log.error("Can't found course expert role");
            json.put("msg", "Can't found course expert role");
            return json;
        }
        RoleUser roleUser = roleRepo.findByName(roleCourseExpert);
        if(roleUser==null){
            log.error("Can't found course expert role");
            json.put("msg", "Can't found course expert role");
            return json;
        }
        List<Account> accounts = accountRepo.findByRoleUser(roleUser);
        if(accounts.isEmpty()){
            log.error("0 course expert found on the system");
            json.put("msg", "0 course expert found on the system");
            return json;
        }
        json.put("msg", accounts.size()+" course expert found on the system");
        json.put("users", accounts);
        json.put("type",true);
        return json;
    }

}
