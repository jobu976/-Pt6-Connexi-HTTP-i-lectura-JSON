package com.example.pt6_jose_buales;

public class Team {

    // Datos básicos (lista)
    private String code;
    private String name;

    // Datos detalle
    private String stadium;
    private int titles;
    private String image;

    // Constructor para la LISTA
    public Team(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Constructor para el DETALLE
    public Team(String stadium, int titles, String image) {
        this.stadium = stadium;
        this.titles = titles;
        this.image = image;
    }

    // ===== Getters =====
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getStadium() {
        return stadium;
    }

    public int getTitles() {
        return titles;
    }

    public String getImage() {
        return image;
    }
}
