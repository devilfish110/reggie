package com.study.reggie.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author That's all
 */
@Slf4j
public class CustomerException extends Exception {

    private static final Long serialVersionUID = 1L;

    public CustomerException(String message) {
        super(message);
    }
}
