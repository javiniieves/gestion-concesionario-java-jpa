package model;

import jakarta.persistence.*;

@Entity
public class Propietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String dni;
    private String nombre;

    public Propietario(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }

    public Propietario(){}

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
