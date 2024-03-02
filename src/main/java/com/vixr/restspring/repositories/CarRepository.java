package com.vixr.restspring.repositories;

import com.vixr.restspring.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    @Query("SELECT c FROM Car c WHERE c.user.id = :userId")
    List<Car> findByUserId(Integer userId);
}
