package com.anshuman.spring.reactive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    private Integer id;
    private String make;
    private String model;
    private String color;

    public Car(String make, String model, String color) {
        this.make = make;
        this.model = model;
        this.color = color;
    }
}
