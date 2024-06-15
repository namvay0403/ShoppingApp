package com.nam.ShoppingApp.dto;

import lombok.Data;

@Data
public class UserDao {
    private Long id;
    private String email;
    private String name;
    private String userRole;
}
