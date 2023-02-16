package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.Config.ApplicationConfig;
import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.RoleUser;
import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Repository.RoleRepo;
import com.swp.onlineLearning.Service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private String roleUserName;
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
        authorities.add(new SimpleGrantedAuthority(account.getRole().getName()));
        return new org.springframework
                .security.core
                .userdetails
                .User(account.getName(), account.getPassword(), authorities);
    }
    @Override
    public HashMap<String, Object> findAll(int pageNumber) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        List<Account> accounts = accountRepo.findAll();
        if(accounts.size()==0){
            log.error("0 account founded");
            json.put("msg", "NoAccountFound");
            return json;
        }
        json.put("users",json);
        return json;
    }

    @Override
    public Account findByGmail(String gmail) {
        Account accountFind = accountRepo.findByGmail(gmail);
        return accountFind;
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
        userDTO.setAccountID(passwordEncoder.encode(userDTO.getGmail()));
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        Account account = new Account();
        modelMapper.map(userDTO, account);

        //save role
        RoleUser role = roleRepo.findByName(roleUserName);
        if(role==null){
            RoleUser user = new RoleUser();
            user.setName(roleUserName);
            RoleUser roleCheck = roleRepo.save(user);
            if(roleCheck==null){
                log.error("Save role "+roleUserName+" fail");
                json.put("msg", "Save role "+roleUserName+" fail");
                return json;
            }
            account.setRole(roleCheck);
        }else{
            account.setRole(role);
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

}
