package model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    private double precioFinal;

    @ManyToOne()
    @JoinColumn(name = "concesionario_id")
    private Concesionario concesionario;
    @ManyToOne()
    @JoinColumn(name = "propietario_id")
    private Propietario propietario;
    @ManyToOne()
    @JoinColumn(name = "coche_matricula")
    private Coche coche;

    public Venta(LocalDate fecha, double precioFinal, Concesionario concesionario, Propietario propietario, Coche coche) {
        this.fecha = fecha;
        this.precioFinal = precioFinal;
        this.concesionario = concesionario;
        this.propietario = propietario;
        this.coche = coche;
    }

    public Venta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public Concesionario getConcesionario() {
        return concesionario;
    }

    public void setConcesionario(Concesionario concesionario) {
        this.concesionario = concesionario;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Coche getCoche() {
        return coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }

    @Override
    public String toString() {
        return "id=" + id + "\n" +
                " fecha=" + fecha + "\n" +
                " precioFinal=" + precioFinal + "\n" +
                " concesionario=" + concesionario + "\n" +
                " propietario=" + propietario + "\n" +
                " coche=" + coche + "\n";
    }
}
