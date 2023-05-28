package com.example.cuidatubarriofinal.dto;

public class RegistroDTO {

    private String dni, password, usuario;

    public RegistroDTO(String dni, String password, String usuario) {
        this.dni = dni;
        this.password = password;
        this.usuario = usuario;
    }

    public RegistroDTO() {
    }
}
