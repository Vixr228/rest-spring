package com.vixr.restspring.services;

import com.vixr.restspring.dto.CarDto;
import com.vixr.restspring.models.Car;
import com.vixr.restspring.models.User;
import com.vixr.restspring.repositories.CarRepository;
import com.vixr.restspring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Autowired
    public CarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }


    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public Car create(CarDto dto) {
        if (dto.getName() != null && dto.getUserId() != null && dto.getPrice() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + dto.getUserId()));

            Car car = new Car(dto.getName(), user, dto.getPrice());
            return carRepository.save(car);
        } else {
            throw new IllegalArgumentException("Required attributes are not filled in");
        }
    }

    public Car findById(Integer id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found with id: " + id));
    }

    public void deleteById(Integer id) {
        carRepository.deleteById(id);
    }

    public void deleteAll() {
        carRepository.deleteAll();
    }

    public void updateUser(Integer id, CarDto carDto) {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            User user = userRepository.findById(carDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + carDto.getUserId()));
            car.setName(carDto.getName());
            car.setUser(user);
            car.setPrice(carDto.getPrice());
            carRepository.save(car);
        } else {
            throw new IllegalArgumentException("Car not found with id: " + id);
        }
    }

    public void deleteAllUsersCars(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Car> userCars = carRepository.findByUserId(user.getId());
            carRepository.deleteAll(userCars);
        } else {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
    }

    public List<Car> findByUserId(Integer id) {
        return carRepository.findByUserId(id);
    }
}
