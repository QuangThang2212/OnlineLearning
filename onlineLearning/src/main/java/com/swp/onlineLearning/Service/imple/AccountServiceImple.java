package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Service
public class AccountServiceImple implements AccountService, UserDetailsService {
    @Autowired
    private AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepo.findByAccountName(username);
        Account accountResult = account.orElse(null);
        if(accountResult == null){
            throw new UsernameNotFoundException("User not found in the database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(accountResult.getRole().getName()));
        return new org.springframework
                .security.core
                .userdetails
                .User(accountResult.getAccountName(), accountResult.getPassword(), authorities);
    }
    @Override
    public List<Account> findAll() {
        return accountRepo.findAll();
    }

    @Override
    public Account findByAccountNameAndPassword(Account account) {
        Optional<Account> accountFind = accountRepo.findByAccountNameAndPassword(account.getAccountName(), account.getPassword());
        return accountFind.orElse(null);
    }

    @Override
    public Account save(Account account) {
        if(account==null){
            return null;
        }
        return accountRepo.save(account);
    }

    @Override
    public Account update(Account account) {
        if(account==null){
            return null;
        }
        return accountRepo.save(account);
    }


}
