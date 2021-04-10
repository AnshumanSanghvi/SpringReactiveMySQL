package com.anshuman.spring.reactive.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vehicle {
    private Car car;
    private Bike bike;
}
