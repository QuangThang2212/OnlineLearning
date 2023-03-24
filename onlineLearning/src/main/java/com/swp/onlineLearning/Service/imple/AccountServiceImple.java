package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.RoleDTO;
import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;
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
    @Autowired
    private SendMailService sendMailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            log.error("User name input null");
            throw new UsernameNotFoundException("User name input null");
        }
        Account account = accountRepo.findByGmail(username);
        if (account == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
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
        json.put("type", false);
        if (pageNumber < 1 || size < 1) {
            log.error("Invalid page " + pageNumber + " or size " + size);
            json.put("msg", "Invalid page " + pageNumber + " or size " + size);
            return json;
        }

        int totalNumber = accountRepo.findAllExcept(gmail, PageRequest.of(pageNumber - 1, size)).getTotalPages();
        if (totalNumber == 0) {
            log.error("0 account founded");
            json.put("msg", "0 account founded for page");
            return json;
        } else if (pageNumber > totalNumber) {
            log.error("invalid page " + pageNumber);
            json.put("msg", "invalid page " + pageNumber);
            return json;
        }

        Page<Account> accounts = accountRepo.findAllExcept(gmail, PageRequest.of(pageNumber - 1, size));
        if (accounts.isEmpty()) {
            log.error("0 account founded for page " + pageNumber);
            json.put("msg", "0 account founded for page " + pageNumber);
            return json;
        }
        List<Account> list = accounts.stream().toList();

        List<UserDTO> userDTOs = new ArrayList<>();
        for (Account a : list) {
            UserDTO userDTO = new UserDTO();
            userDTO.setAccountID(a.getAccountID());
            userDTO.setName(a.getName().trim());
            userDTO.setGmail(a.getGmail().trim());
            userDTO.setImage(a.getImage());
            userDTO.setRole(a.getRoleUser().getName());

            userDTOs.add(userDTO);
        }

        json.put("users", userDTOs);
        json.put("numPage", totalNumber);
        json.put("type", true);
        return json;
    }

    @Override
    public Account findByGmail(String gmail) {
        return accountRepo.findByGmail(gmail);
    }

    @Override
    @Transactional
    public HashMap<String, Object> save(UserDTO userDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (userDTO == null) {
            log.error("Not allow null account to register");
            json.put("msg", "Not allow null account to register");
            return json;
        }
        
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setGmail(userDTO.getGmail().trim());

        ModelMapper modelMapper = new ModelMapper();
        Account account = new Account();
        modelMapper.map(userDTO, account);
        account.setCreateAt(LocalDateTime.now());
        account.setBanStatus(false);

        if(userDTO.getType().equals("normal")){
            String title = "Register confirm to active account";
            String content = "Thank you for create account on our system, welcome our community";
            String button = "Return website";

            try {
                sendMailService.sendMail(title, content, button, account.getGmail());
            } catch (Exception e) {
                log.error("Send mail fail \n" + e.getMessage());
                json.put("msg", "Send mail fail, please try register again");
                json.put("type", false);
                return json;
            }
        }
        RoleUser role = roleRepo.findByName(roleUser);
        if (role == null) {
            log.error("Role " + roleUser + " isn't exist");
            json.put("msg", "Role " + roleUser + " isn't exist");
            return json;
        } else {
            account.setRoleUser(role);
        }
        
        try {
            accountRepo.save(account);
        } catch (Exception e) {
            log.error("Save user with gmail " + account.getGmail() + " fail\n" + e.getMessage());
            json.put("msg", "Save user with gmail " + account.getGmail() + " fail");
            return json;
        }
        log.info("Saving new user with email:" + account.getGmail() + " successfully");

        json.put("msg", "Register successfully");
        json.replace("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> forgotPassword(String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (gmail == null) {
            log.error("Not allow null gmail");
            json.put("msg", "Not allow null gmail");
            return json;
        }
        Account account = accountRepo.findByGmail(gmail);
        if (account == null) {
            log.error("This gmail isn't found in system, please register");
            json.put("msg", "This gmail isn't found in system, please register");
            return json;
        }

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase(Locale.ROOT);
        String digits = "0123456789";
        String alphanum = upper + lower + digits;
        Random random = new Random();
        char[] newPassword = new char[15];
        for (int i = 0; i < newPassword.length; i++) {
            newPassword[i] = alphanum.charAt(random.nextInt(alphanum.length()));
        }

        String title = "Confirm change password";
        String content = "This mail were send to confirm you want to change your password";
        String button = "New Password: " + Arrays.toString(newPassword);

        try {
            sendMailService.sendMail(title, content, button, account.getGmail());
            json.put("msg", "Please check your mail to change your password");
            json.put("type", true);
        } catch (Exception e) {
            log.error("Send mail fail \n" + e.getMessage());
            json.put("msg", "Send mail fail, please try again");
            json.put("type", false);
        }
        return json;
    }

    @Override
    @Transactional
    public HashMap<String, Object> changRole(RoleDTO roleDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (roleDTO == null) {
            log.error("Not allow null account to register");
            json.put("msg", "Not allow null account to register");
            return json;
        }
        RoleUser roleUser = roleRepo.findByName(roleDTO.getName());
        if (roleUser == null) {
            log.error("This role \"" + roleDTO.getName() + "\" not exist in the system");
            json.put("msg", "This role \"" + roleDTO.getName() + "\" not exist in the system");
            return json;
        }
        Account account = accountRepo.findByAccountID(roleDTO.getAccountID());
        if (account == null) {
            log.error("Account with id=" + roleDTO.getAccountID() + " not exist in the system");
            json.put("msg", "Account with id=" + roleDTO.getAccountID() + " not exist in the system");
            return json;
        }
        List<CourseRate> courseRate = account.getCourseRates();
        if (!courseRate.isEmpty()) {
            log.error("Account with id=" + roleDTO.getAccountID() + " had enrolled course on system, not allow change role to " + roleDTO.getName());
            json.put("msg", "Account with id=" + roleDTO.getAccountID() + " had enrolled course on system, not allow change role to " + roleDTO.getName());
            return json;
        }

        try {
            account.setRoleUser(roleUser);
            accountRepo.save(account);
        } catch (Exception e) {
            log.error("Change role for account with id=" + roleDTO.getAccountID() + " fail\n" + e.getMessage());
            json.put("msg", "Change role for account with id=" + roleDTO.getAccountID() + " fail");
            return json;
        }
        log.error("Change role for account with id=" + roleDTO.getAccountID() + " successfully");
        json.put("msg", "Change role for account with id=" + roleDTO.getAccountID() + " successfully");
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> findBAllCourseExpert(String search) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", true);

        RoleUser roleUser = roleRepo.findByName(roleCourseExpert);
        List<Account> accounts;
        System.out.println(search);
        if (search.isEmpty() || search.equals("null")) {
            accounts = accountRepo.findByRoleUser(roleUser);
        } else {
            accounts = accountRepo.findByRoleUserAndSearch(roleUser.getRoleID(), search);
        }
        json.put("users", accounts);
        if (accounts.isEmpty()) {
            log.error("0 course expert found on the system");
            json.put("msg", "0 course expert found on the system");
            return json;
        }
        json.put("msg", accounts.size() + " course expert found on the system");
        return json;
    }

    @Override
    public HashMap<String, Object> findUser(Integer id) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Account account = accountRepo.findByAccountID(id);
        if (account == null) {
            log.error("Account not found");
            json.put("msg", "Account not found");
            return json;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setAccountID(account.getAccountID());
        userDTO.setName(account.getName());
        userDTO.setImage(account.getImage());
        userDTO.setGmail(account.getGmail());

        log.info("successfully");
        json.put("user", userDTO);
        json.put("msg", "successfully");
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> findUserWithGmail(String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Account account = accountRepo.findByGmail(gmail);
        if (account == null) {
            log.error("Account not found");
            json.put("msg", "Account not found");
            return json;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setAccountID(account.getAccountID());
        userDTO.setName(account.getName());
        userDTO.setImage(account.getImage());
        userDTO.setGmail(account.getGmail());

        log.info("successfully");
        json.put("user", userDTO);
        json.put("msg", "successfully");
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> update(UserDTO userDTO, String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        Account account = accountRepo.findByGmail(gmail);
        account.setName(userDTO.getName());
        account.setImage(userDTO.getImage());
        try {

            accountRepo.save(account);
        } catch (Exception e) {
            log.error("Update user information fail");
            json.put("msg", "Update user information fail");
            return json;
        }
        json.put("msg", "Update user information successfully");
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> changePassword(UserDTO userDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Account account = accountRepo.findByGmail(userDTO.getGmail());
        if (account == null) {
            log.error("Account isn't exist in the system");
            json.put("msg", "Account isn't exist in the system");
            return json;
        }
        account.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        try {
            accountRepo.save(account);
        } catch (Exception e) {
            log.error("Change password of gmail " + account.getGmail() + " fail\n" + e.getMessage());
            json.put("msg", "Change password of gmail " + account.getGmail() + " fail");
            return json;
        }
        log.info("Change password of gmail email:" + account.getGmail() + " successfully");

        json.put("msg", "Change password successfully");
        json.replace("type", true);
        return json;
    }
}
