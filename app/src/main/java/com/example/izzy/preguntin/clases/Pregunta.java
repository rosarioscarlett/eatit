package com.example.izzy.preguntin.clases;

/**
 * Created by Izzy on 10/6/2017.
 */

public class Pregunta {
    String titulo;
    String descripcion;
    int credito;
    String imagen;

    public Pregunta(){

    }

    public Pregunta(String titulo, String descripcion, int credito, String imagen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.credito = credito;
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }



    public int getCredito() {
        return credito;
    }

    public void setCredito(int credito) {
        this.credito = credito;
    }
}
