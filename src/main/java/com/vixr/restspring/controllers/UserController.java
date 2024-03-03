package com.vixr.restspring.controllers;

import com.vixr.restspring.dto.UserDto;
import com.vixr.restspring.models.User;
import com.vixr.restspring.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Get all users", description = "Returns all existing users")
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Create new user", description = "Create new user")
    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody UserDto dto) {
        try {
            return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while creating car");
        }
    }

    @Operation(summary = "Get a user by id", description = "Returns a user as per the id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        }
    }

    @Operation(summary = "Delete a user by id", description = "Delete a user as per the id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Integer id) {
        try {
            service.deleteById(id);
            return ResponseEntity.ok("User with id " + id + " deleted successfully");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting user");
        }
    }

    @Operation(summary = "Delete all users", description = "Delete all users")
    @DeleteMapping("/")
    @ResponseBody
    public ResponseEntity<String> deleteAllUsers() {
        try {
            service.deleteAll();
            return ResponseEntity.ok("All users deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting all users");
        }
    }

    @Operation(summary = "Update a user with id", description = "Update a user with id")
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> updateUser(@PathVariable Integer id, @RequestBody UserDto updateRequest) {
        try {
            service.updateUser(id, updateRequest);
            return ResponseEntity.ok("User with id " + id + " updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        }
    }

    @Operation(summary = "Get user full info (with cars)", description = "Get user full info (with cars)")
    @GetMapping("/userDetails/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(service.getUserDetails(id), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        }

    }

}
