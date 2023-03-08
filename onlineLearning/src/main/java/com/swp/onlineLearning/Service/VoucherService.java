package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.ChangeStatusVoucherDTO;
import com.swp.onlineLearning.DTO.VoucherDTO;

import java.util.HashMap;

public interface VoucherService {
    HashMap<String, Object> createVoucher(VoucherDTO voucherDTO);
    HashMap<String, Object> getAllVoucher(int page, int size);
    HashMap<String, Object> changeVoucherStatus(ChangeStatusVoucherDTO changeStatusVoucherDTO);
    HashMap<String, Object> getVoucherForUpdate(Integer voucherID);
    HashMap<String, Object> getVoucherForUser(Integer courseID, String gmail);
}
