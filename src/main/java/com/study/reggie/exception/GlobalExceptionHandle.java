package com.study.reggie.exception;


import com.study.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author That's all
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
public class GlobalExceptionHandle {

    /**
     * 邮箱发送失败的异常处理
     */
    @ExceptionHandler(MailException.class)
    public R<String> sendMailException(MailException e) {
        String message = e.getMessage();
        log.error(message);
        assert message != null;
        if (message.contains("Invalid Addresses")) {
            return R.error("收件人地址格式不正确");
        }
        return R.error("获取邮箱验证码失败");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<String> requestBodyMissionExceptionHandler(HttpMessageNotReadableException e) {
        String message = e.getMessage();
        log.info(message);
        assert message != null;
        if (message.contains("Required request body is missing")) {
            return R.error("错误的请求");
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(IOException.class)
    public R<String> ioExceptionHandler(IOException e) {
        log.info("io异常信息==>{}", e.getMessage());
        return R.error("读取文件失败!");
    }

    /**
     * 自定义异常处理
     *
     * @param e 异常源
     * @return R
     */
    @ExceptionHandler(CustomerException.class)
    public R<String> customerException(CustomerException e) {
        return R.error(e.getMessage());
    }


}
