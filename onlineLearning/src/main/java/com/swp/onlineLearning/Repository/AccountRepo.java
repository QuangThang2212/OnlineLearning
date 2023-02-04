package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, String> {
    List<Account> findAll();
    Optional<Account> findByAccountNameAndPassword(String accountName, String pass);
    Account save(Account account);
}
