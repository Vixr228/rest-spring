package com.vixr.restspring.dto;

import java.math.BigDecimal;

public class CarDto {

    private String name;
    private Integer userId;
    private BigDecimal price;

    public String getName() {
        return name;
    }

    public Integer getUserId() {
        return userId;
    }

    public BigDecimal getPrice() {
        return price;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
