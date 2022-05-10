package com.example.demo.api.controllers;

import com.example.demo.api.utils.UserValidator;
import com.example.demo.api.services.AppUserService;
import com.example.demo.api.models.AppUser;
import com.example.demo.api.models.forms.AppUserLoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class AppUserController {
    private final AppUserService appUserService;
    private UserValidator userValidator;

    @Autowired
    public AppUserController(AppUserService appUserService, UserValidator userValidator) {
        this.appUserService = appUserService;
        this.userValidator = userValidator;
    }

    @PostMapping(path = "api/user")
    public ResponseEntity<Object> createUser(@RequestBody AppUser user, BindingResult bindingResult) {
        userValidator.validateCreateUser(user, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new IllegalStateException("Parameters error");
        }

        Object createUser = this.appUserService.createUser(user);

        return ResponseEntity
                .status(200)
                .body(createUser);
    }

    @PostMapping(path = "login")
    public ResponseEntity<Object> loginUser(@RequestBody AppUserLoginForm loginUserForm, BindingResult bindingResult) {
        userValidator.validateLoginUser(loginUserForm, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new IllegalStateException("Invalid Parameters");
        }

        Object loginUser = this.appUserService.loginUser(loginUserForm);

        return ResponseEntity
                .status(200)
                .body(loginUser);
    }

    @GetMapping(path = "verify")
    public ResponseEntity<Object> verifyUser(@RequestHeader(value = "token") String token) throws Exception {
        Object verifyUser = this.appUserService.verifyUser(token);

        return ResponseEntity
                .status(200)
                .body(verifyUser);
    }

    @PutMapping(path = "api/user/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable("userId") int userId, @RequestBody AppUser updatedUser) {
        Object updateUser = this.appUserService.updateUser(userId, updatedUser);

        return ResponseEntity
                .status(200)
                .body(updateUser);
    }

    @DeleteMapping(path = {"api/user/{userId}"})
    public void deleteUser(@PathVariable() Integer userId) {
        this.appUserService.deleteUser(userId);
    }
}
