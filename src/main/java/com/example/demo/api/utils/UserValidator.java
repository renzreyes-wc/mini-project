package com.example.demo.api.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component
public class UserValidator {

    public void validateCreateUser(Object object, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Not Empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "Not Empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "Not Empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Not Empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthDay", "Not Empty");
    }

    public void validateLoginUser(Object object, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Not Empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Not Empty");
    }
}
