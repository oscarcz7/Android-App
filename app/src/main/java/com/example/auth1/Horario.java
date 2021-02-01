package com.example.auth1;

import androidx.annotation.NonNull;

public class Horario {

    String id;
    String hora;
    String estado;

    public Horario(String id, String hora, String estado) {
        this.id = id;
        this.hora = hora;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    @Override
    public String toString() {
        return hora;
    }
}
