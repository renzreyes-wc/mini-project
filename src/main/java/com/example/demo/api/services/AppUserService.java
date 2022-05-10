package com.example.demo.api.services;

import com.example.demo.api.utils.JwtUtils;
import com.example.demo.api.models.AppUser;
import com.example.demo.api.models.forms.AppUserLoginForm;
import com.example.demo.api.repositories.AppUserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, JwtUtils jwtUtils) {
        this.appUserRepository = appUserRepository;
        this.jwtUtils = jwtUtils;
    }

    public Map<String, Object> createUser(AppUser user) {
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new IllegalStateException("Invalid parameter(s).");
        }

        boolean isEmailExist = appUserRepository
                .findByEmail(user.getEmail())
                .isPresent();

        if (isEmailExist) {
            throw new IllegalStateException("Invalid parameter(s).");
        }

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hashedPassword);

        user.setIsDisabled(0);

        this.appUserRepository.save(user);

        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("id", user.getId());
        apiResponse.put("email", user.getEmail());
        apiResponse.put("firstName", user.getFirstName());
        apiResponse.put("middleName", user.getMiddleName());
        apiResponse.put("lastName", user.getLastName());
        apiResponse.put("birthDay", user.getBirthDay());

        return apiResponse;
    }

    public Map<String, Object> loginUser(AppUserLoginForm loginUserForm) {
        if (!EmailValidator.getInstance().isValid(loginUserForm.getEmail())) {
            throw new IllegalStateException("Invalid parameter(s).");
        }

        AppUser user = this.appUserRepository.findByEmail(loginUserForm.getEmail())
                .orElseThrow(() ->  new IllegalStateException("Invalid parameter(s)."));

        if (user.getIsDisabled() == 1) {
            throw new IllegalStateException("Invalid parameter(s).");
        }

        boolean checkPassword = BCrypt.checkpw(loginUserForm.getPassword(), user.getPassword());

        if (!checkPassword) {
            throw new IllegalStateException("Invalid parameter(s).");
        }

        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("token", jwtUtils.generateJwt(user));

        return apiResponse;
    }

    public Map<String, Object> verifyUser(String token) throws Exception {
        String subject = jwtUtils.verifyJwt(token);

        AppUser user = appUserRepository.findByEmail(subject)
                .orElseThrow(() -> new IllegalStateException("Invalid token."));

        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("id", user.getId());
        apiResponse.put("email", user.getEmail());
        apiResponse.put("firstName", user.getFirstName());
        apiResponse.put("middleName", user.getMiddleName());
        apiResponse.put("lastName", user.getLastName());
        apiResponse.put("birthDay", user.getBirthDay());

        return apiResponse;
    }

    @Transactional
    public Map<String, Object> updateUser(Integer userId, AppUser updatedUser) {
        AppUser user = this.appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User does not exist."));

        if (user.getIsDisabled() == 1) {
            throw new IllegalStateException("Invalid parameter(s).");
        }

        if (updatedUser.getEmail() != null && updatedUser.getEmail().trim().length() > 0
                && !Objects.equals(updatedUser.getEmail(), user.getEmail())) {
            Optional<AppUser> appUserOptional = this.appUserRepository.findByEmail(updatedUser.getEmail());

            if (appUserOptional.isPresent()) {
                throw new IllegalStateException("Invalid parameter(s).");
            }

            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getFirstName() != null && updatedUser.getFirstName().trim().length() > 0
                && !Objects.equals(updatedUser.getFirstName(), user.getFirstName())) {
            user.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getMiddleName() != null && updatedUser.getMiddleName().trim().length() > 0
                && !Objects.equals(updatedUser.getMiddleName(), user.getMiddleName())) {
            user.setMiddleName(updatedUser.getMiddleName());
        }

        if (updatedUser.getLastName() != null && updatedUser.getLastName().trim().length() > 0
                && !Objects.equals(updatedUser.getLastName(), user.getLastName())) {
            user.setLastName(updatedUser.getLastName());
        }

        if (updatedUser.getBirthDay() != null && updatedUser.getBirthDay().toString().trim().length() > 0
                && !Objects.equals(updatedUser.getBirthDay(), user.getBirthDay())) {
            user.setBirthDay(updatedUser.getBirthDay());
        }

        if (updatedUser.getPassword() != null && updatedUser.getPassword().trim().length() > 0) {
            user.setPassword(BCrypt.hashpw(updatedUser.getPassword(), BCrypt.gensalt(12)));
        }

        appUserRepository.save(user);

        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("id", user.getId());
        apiResponse.put("email", user.getEmail());
        apiResponse.put("firstName", user.getFirstName());
        apiResponse.put("middleName", user.getMiddleName());
        apiResponse.put("lastName", user.getLastName());
        apiResponse.put("birthDay", user.getBirthDay());

        return apiResponse;
    }

    @Transactional
    public void deleteUser(Integer userId) {
        AppUser user = this.appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User does not exist."));

        if (user.getIsDisabled() == 1) {
            throw new IllegalStateException("User already deleted.");
        }

        user.setIsDisabled(1);

        appUserRepository.save(user);
    }
}
