package com.vixr.restspring.dto;

import com.vixr.restspring.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
}
