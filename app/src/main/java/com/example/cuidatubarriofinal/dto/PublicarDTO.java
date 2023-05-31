package com.example.cuidatubarriofinal.dto;

public class PublicarDTO {
    private String dniUsuario, comentario;

    public PublicarDTO(String dniUsuario, String comentario) {
        this.dniUsuario = dniUsuario;
        this.comentario = comentario;
    }

    public String getDniUsuario() {
        return dniUsuario;
    }

    public void setDniUsuario(String dniUsuario) {
        this.dniUsuario = dniUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
