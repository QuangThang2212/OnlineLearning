package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.ChangeStatusVoucherDTO;
import com.swp.onlineLearning.DTO.VoucherDTO;
import com.swp.onlineLearning.Service.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voucher")
@Slf4j
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @PostMapping("/create")
    public ResponseEntity<HashMap<String, Object>> createVoucher(@Valid @RequestBody VoucherDTO voucherDTO, BindingResult result){
        HashMap<String, Object> json = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                stringBuilder.append(error.getDefaultMessage()).append("\n");
            }
            json.put("msg",stringBuilder.toString());
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = voucherService.createVoucher(voucherDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/sale_manager")
    public ResponseEntity<HashMap<String, Object>> getAllVoucher(@RequestParam("page")int page, @RequestParam("limit")int limit){
        HashMap<String, Object> json = voucherService.getAllVoucher(page, limit);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("change_status")
    public ResponseEntity<HashMap<String, Object>> changeStatus(@RequestBody ChangeStatusVoucherDTO changeStatusVoucherDTO){
        HashMap<String, Object> json = voucherService.changeVoucherStatus(changeStatusVoucherDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/update")
    public ResponseEntity<HashMap<String, Object>> getVoucherForUpdate(@RequestParam("id")Integer id){
        HashMap<String, Object> json = voucherService.getVoucherForUpdate(id);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
