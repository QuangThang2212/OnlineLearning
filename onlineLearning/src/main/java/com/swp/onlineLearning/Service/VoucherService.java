package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.VoucherDTO;

import java.util.HashMap;

public interface VoucherService {
    HashMap<String, Object> createVoucher(VoucherDTO voucherDTO);
    HashMap<String, Object> getAllVoucher(int page, int size);
}
