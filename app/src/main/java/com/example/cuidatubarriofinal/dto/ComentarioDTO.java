package com.example.cuidatubarriofinal.dto;

public class ComentarioDTO {
    String usuario, dni, comentario;

    public ComentarioDTO(String usuario, String dni, String comentario) {
        this.usuario = usuario;
        this.dni = dni;
        this.comentario = comentario;
    }

    public ComentarioDTO() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
