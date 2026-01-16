package model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Equipamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private double coste;


    // especificamos que esta tabla es la que se relaciona con Coche
    // por lo que de etsa cogerá la clave primaria para generar la relacion de la relación N:M
    @ManyToMany(mappedBy = "listaDeEquipamientos")
    private List<Coche> listaCoches;

    public Equipamiento(String nombre, double coste) {
        this.nombre = nombre;
        this.coste = coste;
    }

    public Equipamiento(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }

    public List<Coche> getListaCoches() {
        return listaCoches;
    }

    public void setListaCoches(List<Coche> listaCoches) {
        this.listaCoches = listaCoches;
    }

    @Override
    public String toString() {
        return "Equipamiento -> " +
                "nombre=" + nombre;
    }
}
