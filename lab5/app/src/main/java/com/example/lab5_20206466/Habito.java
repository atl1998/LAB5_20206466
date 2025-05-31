package com.example.lab5_20206466;

public class Habito {
    private String nombre;
    private String categoria;
    private int frecuenciaHoras;
    private String fechaInicio;
    private String horaInicio;

    public Habito(String nombre, String categoria, int frecuenciaHoras, String fechaInicio, String horaInicio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.frecuenciaHoras = frecuenciaHoras;
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
    }

    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public int getFrecuenciaHoras() { return frecuenciaHoras; }
    public String getFechaInicio() { return fechaInicio; }
    public String getHoraInicio() { return horaInicio; }
}