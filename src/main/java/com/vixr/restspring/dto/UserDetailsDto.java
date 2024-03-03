package com.vixr.restspring.dto;

import com.vixr.restspring.models.Car;
import com.vixr.restspring.models.User;

import java.math.BigDecimal;
import java.util.List;

public class UserDetailsDto {
    User user;

    List<Car> carList;

    BigDecimal totalPrice;

    public UserDetailsDto(User user, List<Car> carList) {
        this.user = user;
        this.carList = carList;
        this.totalPrice = carList.stream()
                .map(Car::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "{" +
                "\n\tuser=" + user +
                ", \n\tcarList=" + carList +
                ", \n\ttotalPrice=" + totalPrice +
                "\n}";
    }
}
