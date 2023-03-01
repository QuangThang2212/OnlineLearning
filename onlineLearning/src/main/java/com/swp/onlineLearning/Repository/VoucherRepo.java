package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepo extends JpaRepository<Voucher, Integer> {
    Voucher findByVoucherID(int id);
}
