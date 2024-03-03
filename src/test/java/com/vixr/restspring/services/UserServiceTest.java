package com.vixr.restspring.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.vixr.restspring.dto.UserDetailsDto;
import com.vixr.restspring.dto.UserDto;
import com.vixr.restspring.models.Car;
import com.vixr.restspring.models.User;
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

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private CarService carService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        UserDto dto = new UserDto();
        dto.setFirstName("Anton");
        dto.setLastName("Heh");
        dto.setEmail("antoha.heh@mail.com");

        when(userRepository.save(any(User.class)))
                .thenReturn(new User(dto.getFirstName(), dto.getLastName(), dto.getEmail()));

        User result = userService.create(dto);

        assertNotNull(result);
        assertEquals("Anton", result.getFirstName());
        assertEquals("Heh", result.getLastName());
        assertEquals("antoha.heh@mail.com", result.getEmail());
    }

    @Test
    public void testCreateInvalidEmail() {
        UserDto dto = new UserDto();
        dto.setFirstName("Antoha");
        dto.setLastName("Heh");
        dto.setEmail("email");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.create(dto);
        });
        assertEquals("Invalid email", exception.getMessage());
    }

    @Test
    public void testCreateMissingAttributes() {
        UserDto dto = new UserDto();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.create(dto);
        });
        assertEquals("Required attributes are not filled in", exception.getMessage());
    }

    @Test
    public void testGetAll() {
        // Создаем список пользователей для возврата из репозитория
        List<User> users = new ArrayList<>();
        users.add(new User("Antoha", "Heh", "antoha.heh@mail.com"));
        users.add(new User("Serega", "Per", "serega@yandex.com"));

        // Задаем поведение мока userRepository.findAll()
        when(userRepository.findAll()).thenReturn(users);

        // Вызываем метод getAll() из userService
        List<User> result = userService.getAll();

        // Проверяем, что результат совпадает с ожидаемым списком пользователей
        assertEquals(users.size(), result.size());
        assertEquals(users.get(0).getFirstName(), result.get(0).getFirstName());
        assertEquals(users.get(1).getLastName(), result.get(1).getLastName());

        // Проверяем, что метод findAll() был вызван один раз
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindById(){
        User user = new User("Anton", "Pog", "email@mail.ru");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.findById(1);

        assertEquals(user, result);
    }

    @Test
    public void testFindByIdNotFound(){
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findById(1);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    public void testGetUserDetails() {
        User user = new User("Anton", "Heh", "antoha.heh@gmail.com");
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Toyota", user, BigDecimal.valueOf(10000)));
        cars.add(new Car("Honda", user, BigDecimal.valueOf(15000)));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(carService.findByUserId(1)).thenReturn(cars);

        UserDetailsDto result = userService.getUserDetails(1);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(cars, result.getCarList());
        assertEquals(BigDecimal.valueOf(25000), result.getTotalPrice());
    }

    @Test
    public void testGetUserDetailsNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserDetails(1);
        });
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    public void testValidateValidEmail() {
        assertTrue(userService.validateEmail("test@example.com"));
        assertTrue(userService.validateEmail("user123@mail.ru"));
        assertTrue(userService.validateEmail("antoha.heh12345@gmail.com"));
    }

    @Test
    public void testValidateInvalidEmail() {
        assertFalse(userService.validateEmail("email"));
        assertFalse(userService.validateEmail("user@.com"));
        assertFalse(userService.validateEmail("user@mail@com"));
        assertFalse(userService.validateEmail("user@mail."));
        assertFalse(userService.validateEmail("@gmail.com"));
    }


}