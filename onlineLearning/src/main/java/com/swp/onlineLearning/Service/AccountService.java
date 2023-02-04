package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.Model.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {
    List<Account> findAll();
    Account findByAccountNameAndPassword(Account account);
    Account save(Account account);
    Account update(Account account);
}
