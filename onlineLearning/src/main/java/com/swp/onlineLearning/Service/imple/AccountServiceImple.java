package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class AccountServiceImple implements AccountService {
    @Autowired
    private AccountRepo accountRepo;
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
