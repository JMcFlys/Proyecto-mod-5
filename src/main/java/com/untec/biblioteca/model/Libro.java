package com.untec.biblioteca.model;

/**
 * Modelo que representa un Libro en la Biblioteca Digital UNTEC.
 */
public class Libro {

    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int anio;
    private boolean disponible;

    // Constructor vacío
    public Libro() {}

    // Constructor completo
    public Libro(int id, String titulo, String autor, String isbn, int anio, boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anio = anio;
        this.disponible = disponible;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return "Libro{id=" + id + ", titulo='" + titulo + "', autor='" + autor + "'}";
    }
}
