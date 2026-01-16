package model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Coche {

    @Id
    @Column(length = 20)
    private String matricula;

    private String marca;
    private String modelo;
    @Column(name = "precio_base")
    private double precioBase;
    @ManyToOne()
    @JoinColumn(name = "concesionario_id")
    private Concesionario concesionario;
    @ManyToOne()
    @JoinColumn(name = "propietario_id")
    private Propietario propietario;

    @ManyToMany()
    @JoinTable(
            // nombre de la tabla surgida de la relación N:M
            name = "coche_equipamiento",
            // con joinColumns coge la clave primaria (@ID) de la tabla actual (Coche)
            joinColumns = @JoinColumn(name = "coche_matricula"),
            // como el atributo equipamiento_id viene de la otra tabla debemos especificarlo con inverseJoinColumns
            // --> así cogerá la clave primaria de la tabla Equipamiento (ya que lo especificamos en List<Equipamiento>)
            inverseJoinColumns = @JoinColumn(name = "equipamiento_id")
    )
    List<Equipamiento> listaDeEquipamientos;

    public Coche(String matricula, String marca, String modelo, double precioBase, Concesionario concesionario) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.precioBase = precioBase;
        this.concesionario = concesionario;
        // cuando metemos un nuevo coche no tiene propietario
        this.propietario = null;
        listaDeEquipamientos = new ArrayList<>();
    }

    public Coche() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
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

    public List<Equipamiento> getListaDeEquipamientos() {
        return listaDeEquipamientos;
    }

    public void setListaDeEquipamientos(List<Equipamiento> listaDeEquipamientos) {
        this.listaDeEquipamientos = listaDeEquipamientos;
    }

    public static boolean matriculaValida(String matriculaAComprobar) {

        Pattern patronMatricula = Pattern.compile("\\d{4}\\p{L}{3}");

        Matcher matcher = patronMatricula.matcher(matriculaAComprobar);

        return matcher.matches();
    }
}
