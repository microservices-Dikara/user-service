package com.dikara.user.controller;


import com.dikara.user.constant.BaseResponse;
import com.dikara.user.constant.GlobalMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Slf4j
public class BaseTransactionController {

  public static BaseResponse<?> buildSuccessResponse(Object data) {
    return BaseResponse.builder().code(GlobalMessage.SUCCESS.code).message(GlobalMessage.SUCCESS.message).data(data).build();
  }

}