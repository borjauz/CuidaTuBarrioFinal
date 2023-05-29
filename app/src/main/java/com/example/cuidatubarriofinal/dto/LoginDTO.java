package com.example.cuidatubarriofinal.dto;

public class LoginDTO {
    private String dni, password;

    public LoginDTO(String dni, String password) {
        this.dni = dni;
        this.password = password;
    }

    public LoginDTO() {
    }
}
