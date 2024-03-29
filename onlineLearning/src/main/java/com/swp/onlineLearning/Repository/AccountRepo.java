package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.RoleUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM Account WHERE NOT gmail=?1")
    Page<Account> findAllExcept(String gmail, Pageable pageable);
    List<Account> findByRoleUser(RoleUser roleUser);
    @Query(nativeQuery = true, value = "SELECT * FROM Account a where roleid=?1 and (name like %?2% Or gmail like %?2%)")
    List<Account> findByRoleUserAndSearch(Integer roleID, String search);
    Account findByGmail(String gmail);
    Account findByAccountID(int id);
    @Query(nativeQuery = true, value = "SELECT * FROM Account a inner join role_user b on a.roleid = b.roleid WHERE a.accountid = ?1 AND b.name = ?2")
    Account findByAccountIDAndRoleUser(int id,String name);
    @Query(nativeQuery = true, value = "select count(accountid) from account")
    Integer countTotalAccount();
    @Query(nativeQuery = true, value = "select count(accountid) from account where roleid = ?1")
    Integer totalByRole(int roleID);
}
