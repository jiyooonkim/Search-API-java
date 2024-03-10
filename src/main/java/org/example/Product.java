package org.example;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

public class Product {
    @Getter
    @Setter
    @Id
    private String name;
    private String message;

}
