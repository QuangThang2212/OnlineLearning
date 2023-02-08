package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.Model.Account;

import java.util.List;

public interface AccountService {
    List<Account> findAll();
    Account findByAccountNameAndPassword(Account account);
    Account save(Account account);
    Account update(Account account);
}
