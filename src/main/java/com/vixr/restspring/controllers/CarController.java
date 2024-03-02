package com.vixr.restspring.controllers;

import com.vixr.restspring.dto.CarDto;
import com.vixr.restspring.dto.UserDto;
import com.vixr.restspring.models.Car;
import com.vixr.restspring.models.User;
import com.vixr.restspring.services.CarService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {


    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Operation(summary = "Get all cars", description = "Returns all existing cars")
    @GetMapping("/")
    public ResponseEntity<List<Car>> getAllCars() {
        return new ResponseEntity<>(carService.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Create new car", description = "Create new car")
    @PostMapping("/")
    public ResponseEntity<String> createCar(@RequestBody CarDto carRequest) {
        try {
            carService.create(carRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Car created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while creating car");
        }
    }

    @Operation(summary = "Get a car by id", description = "Returns a car as per the id")
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable("id") int id) {
        return new ResponseEntity<>(carService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Update a car with id", description = "Update a car with id")
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> updateCar(@PathVariable Integer id, @RequestBody CarDto updateRequest) {
        try {
            carService.updateUser(id, updateRequest);
            return ResponseEntity.ok("Car with id " + id + " updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found with id: " + id);
        }
    }

    @Operation(summary = "Delete all cars", description = "Delete all cars")
    @DeleteMapping("/")
    @ResponseBody
    public ResponseEntity<String> deleteAllCars() {
        try {
            carService.deleteAll();
            return ResponseEntity.ok("All cars deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting all cars");
        }
    }

    @Operation(summary = "Delete a car by id", description = "Delete a car as per the id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCarById(@PathVariable Integer id) {
        try {
            carService.deleteById(id);
            return ResponseEntity.ok("Car with id " + id + " deleted successfully");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting Car");
        }
    }

    @Operation(summary = "Delete user's cars by id", description = "Delete user's cars by id")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteAllUsersCars(@PathVariable Integer id) {
        try {
            carService.deleteAllUsersCars(id);
            return ResponseEntity.ok("The user's machines with id = " + id + " have been deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        }
    }
}
