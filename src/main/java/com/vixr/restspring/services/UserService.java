package com.vixr.restspring.services;

import com.vixr.restspring.dto.UserDetailsDto;
import com.vixr.restspring.dto.UserDto;
import com.vixr.restspring.models.Car;
import com.vixr.restspring.models.User;
import com.vixr.restspring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final UserRepository userRepository;
    private final CarService carService;

    @Autowired
    public UserService(UserRepository userRepository, CarService carService) {
        this.userRepository = userRepository;
        this.carService = carService;
    }

    public User create(UserDto dto) {
        if(dto.getFirstName() != null
                && dto.getEmail() != null) {
            if(validateEmail(dto.getEmail())){
                User user = new User(dto.getFirstName(), dto.getLastName(), dto.getEmail());
                return userRepository.save(user);
            } else {
                throw new IllegalArgumentException("Invalid email");
            }
        } else {
            throw new IllegalArgumentException("Required attributes are not filled in");
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    // Метод для обновления информации о пользователе
    public void updateUser(Integer id, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
    }

    public UserDetailsDto getUserDetails(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Car> cars = carService.findByUserId(id);
            return new UserDetailsDto(user, cars);
        } else {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
    }

    boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

}
