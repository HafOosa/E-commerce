package com.hafsa.ecommerce.dto;

import com.hafsa.ecommerce.model.User;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private User.Role Role;


}
