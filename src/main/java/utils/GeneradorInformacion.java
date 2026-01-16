package utils;

import model.Coche;
import model.Equipamiento;
import model.Reparacion;
import model.Venta;

import java.time.format.DateTimeFormatter;

public class GeneradorInformacion {

    /**
     * Metodo encargado de generar un String con los datos de un Coche recibido
     *
     * @param coche Coche del que se mostraran los datos
     * @return devuelve un String con los datos del Coche recibido
     */
    public static String darDatosCoche(Coche coche) {
        StringBuilder datosCoche = new StringBuilder();

        datosCoche.append("idConcesionario: " + coche.getMatricula()).append(System.lineSeparator());
        datosCoche.append("Modelo: " + coche.getModelo()).append(System.lineSeparator());
        datosCoche.append("Marca: " + coche.getMarca()).append(System.lineSeparator());
        datosCoche.append("Precio base: " + coche.getPrecioBase()).append(System.lineSeparator());

        datosCoche.append("Extras: " + "\n");
        for (Equipamiento equipamientoActual : coche.getListaDeEquipamientos()) {
            datosCoche.append(equipamientoActual.getNombre()).append(System.lineSeparator());
        }
        return datosCoche.toString();
    }

    /**
     * Metodo encargado de generar un String con los datos de una reparación recibida
     *
     * @param reparacion Reparacion del que se mostraran los datos
     * @return devuelve un String con los datos de la Reparacion recibido
     */
    public static String darDatosReparacion(Reparacion reparacion) {
        StringBuilder datosReparacion = new StringBuilder();

        datosReparacion.append("ID de la reparación: " + reparacion.getId());
        datosReparacion.append(System.lineSeparator());
        datosReparacion.append("Fecha: " + reparacion.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        datosReparacion.append(System.lineSeparator());
        datosReparacion.append("Coste: " + reparacion.getCoste());
        datosReparacion.append(System.lineSeparator());
        datosReparacion.append("Descripción: " + reparacion.getDescripcion());
        datosReparacion.append(System.lineSeparator());
        datosReparacion.append("Matrícula del coche: " +reparacion.getCoche().getMatricula());
        datosReparacion.append(System.lineSeparator());

        return datosReparacion.toString();
    }

    /**
     * Metodo encargado de generar un String con los datos de una Venta recibida
     *
     * @param venta Venta de la que se mostraran los datos
     * @return devuelve un String con los datos de la Venta recibido
     */
    public static String darDatosVenta(Venta venta) {
        StringBuilder datosReparacion = new StringBuilder();

        datosReparacion.append("ID de la venta: " + venta.getId());
        datosReparacion.append(System.lineSeparator());
        datosReparacion.append("Fecha: " + venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        datosReparacion.append(System.lineSeparator());
        datosReparacion.append("Precio final: " + venta.getPrecioFinal());
        datosReparacion.append(System.lineSeparator());

        return datosReparacion.toString();
    }
}
