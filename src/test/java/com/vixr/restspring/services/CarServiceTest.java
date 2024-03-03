package com.vixr.restspring.services;

import com.vixr.restspring.dto.CarDto;
import com.vixr.restspring.models.Car;
import com.vixr.restspring.models.User;
import com.vixr.restspring.repositories.CarRepository;
import com.vixr.restspring.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate(){
        User user = new User("Antoha", "Heh", "heh@mail.ru");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        CarDto dto = new CarDto();
        dto.setName("Toyota");
        dto.setUserId(1);
        dto.setPrice(BigDecimal.valueOf(10000));

        when(carRepository.save(any(Car.class))).thenReturn(new Car(dto.getName(), user, dto.getPrice()));

        Car result = carService.create(dto);

        assertNotNull(result);
        assertEquals("Toyota", result.getName());
        assertEquals(user, result.getUser());
        assertEquals(BigDecimal.valueOf(10000), result.getPrice());
    }

    @Test
    public void testCreateMissingAttributes() {
        CarDto dto = new CarDto();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.create(dto);
        });
        assertEquals("Required attributes are not filled in", exception.getMessage());
    }

    @Test
    public void testCreateUserNotFound() {
        CarDto dto = new CarDto();
        dto.setName("Toyota");
        dto.setUserId(1);
        dto.setPrice(BigDecimal.valueOf(10000));
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.create(dto);
        });
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    public void testFindById(){
        User user = new User("Anton", "Pog", "email@mail.ru");
        Car car = new Car("Toyota", user, BigDecimal.valueOf(10000));
        when(carRepository.findById(1)).thenReturn(Optional.of(car));

        Car result = carService.findById(1);

        assertEquals(car, result);
    }

    @Test
    public void testFindByIdNotFound(){
        when(carRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.findById(1);
        });

        assertEquals("Car not found with id: 1", exception.getMessage());
    }

    @Test
    public void findByUserId(){
        User user = new User("Anton", "Pog", "email@mail.ru");
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Toyota", user, BigDecimal.valueOf(10000)));
        cars.add(new Car("Honda", user, BigDecimal.valueOf(15000)));

        when(carRepository.findByUserId(1)).thenReturn(cars);

        List<Car> result = carService.findByUserId(1);

        assertEquals(cars, result);
    }

    @Test
    public void testDeleteAllUsersCars() {
        // Создаем пользователя и список его машин
        User user = new User("Anton", "Heh", "mail@mail.ru");
        user.setId(1);
        List<Car> userCars = new ArrayList<>();
        userCars.add(new Car("Toyota", user, BigDecimal.valueOf(10000.00)));
        userCars.add(new Car("Honda", user, BigDecimal.valueOf(15000.00)));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        when(carRepository.findByUserId(1)).thenReturn(userCars);

        carService.deleteAllUsersCars(1);

        verify(carRepository, times(1)).deleteAll(userCars);
    }

    @Test
    public void testDeleteAllUsersCarsUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.deleteAllUsersCars(1);
        });
        assertEquals("User not found with id: 1", exception.getMessage());

        verify(carRepository, never()).deleteAll(anyCollection());
    }
}