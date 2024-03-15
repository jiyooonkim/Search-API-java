package org.example;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class Product {
    @Id
    private String name;
    private String message;
    private String etc;

}
