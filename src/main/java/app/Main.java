package app;

import model.Coche;
import service.GestionConcesionario;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            mostrarMenu();
            try {
                int opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1 -> {
                        if (GestionConcesionario.iniciarEntityManager()) {
                            System.out.println("Entity Manager inicializado con éxito");
                        } else {
                            System.out.println("No se ha podido inicializar el Entity Manager");
                        }
                    }
                    case 2 -> {

                        if (GestionConcesionario.limpiarEstadoYCargarDatos()) {
                            System.out.println("Se han borrado los datos que habían guardados y se han creado unos nuevos de ejemplo");
                        } else {
                            System.out.println("Error al borrar los datos que habían e intentar crear unos nuevos");
                        }
                    }

                    case 3 -> {
                        System.out.println("Introduzca el nombre del concesionario que quiere dar de alta:");
                        String nombreConcesionario = sc.nextLine();

                        System.out.println("Introduzca la dirección del concesionario que quiere dar de alta:");
                        String direccionConcesionario = sc.nextLine();

                        if (GestionConcesionario.darDeAltaConcesionario(nombreConcesionario, direccionConcesionario)) {
                            System.out.println("Concesionario dado de alta con éxito");
                        } else {
                            System.out.println("No se ha podido dar de alta el concesionario");
                        }
                    }

                    case 4 -> {
                        String matriculaCoche;
                        while (true) {
                            System.out.println("Introduzca la matrícula del coche que quiere registrar");
                            matriculaCoche = sc.nextLine();

                            if (!Coche.matriculaValida(matriculaCoche)) {
                                System.out.println("La matrícula no cumple el patrón [4 dígitos + 3 letras]");
                            } else {
                                break;
                            }
                        }

                        System.out.println("Introduzca la marca del coche que quiere registrar");
                        String marcaCoche = sc.nextLine();

                        System.out.println("Introduzca la modelo del coche que quiere registrar");
                        String modeloCoche = sc.nextLine();

                        try {
                            System.out.println("Introduzca el precio del coche que quiere registrar");
                            double precioCoche = Double.parseDouble(sc.nextLine());

                            System.out.println("Introduzca el ID del concesionario donde quiere registrar el coche");
                            Long idConcesionario = Long.parseLong(sc.nextLine());

                            if (GestionConcesionario.darDeAltaCoche(matriculaCoche, marcaCoche, modeloCoche, precioCoche, idConcesionario)) {
                                System.out.println("Coche registrado correctamente");
                            } else {
                                System.out.println("No se ha podido registrar el coche");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Por favor, al introducir el precio e ID del concesionario, asegurese de que sea un valor numérico");
                        }
                    }

                    case 5 -> {
                        String matriculaCoche;
                        while (true) {
                            System.out.println("Introduzca la matrícula del coche al que quiere añadirle el equipamiento");
                            matriculaCoche = sc.nextLine();

                            if (!Coche.matriculaValida(matriculaCoche)) {
                                System.out.println("La matrícula no cumple el patrón [4 dígitos + 3 letras]");
                            } else {
                                break;
                            }
                        }

                        try {
                            System.out.println("Inotrduzca el ID del equipamiento que quiere añadirle al coche");
                            Long idEquipamiento = Long.parseLong(sc.nextLine());

                            double precioTotalDelCoche = GestionConcesionario.instalarEquipamientoACoche(matriculaCoche, idEquipamiento);

                            if (precioTotalDelCoche == -1) {
                                System.out.println("Lo sentimos, ha ocurrido un error y no se ha podido añadir el equipamiento al coche");
                            } else {
                                System.out.printf("El precio total del coche es %.2f", precioTotalDelCoche);
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Por favor, al introducir el ID del equipamiento, asegurese de que sea un valor numérico");
                        }
                    }

                    case 6 -> {
                        String matriculaCoche;

                        while (true) {
                            System.out.println("Introduzca la matrícula del coche del que quiere registrar la reparación");
                            matriculaCoche = sc.nextLine();

                            if (!Coche.matriculaValida(matriculaCoche)) {
                                System.out.println("La matrícula no cumple el patrón [4 dígitos + 3 letras]");
                            } else {
                                break;
                            }
                        }

                        try {
                            System.out.println("Introduzca el ID del mecánico al que quiere asignarle la reparación");
                            Long idMecanico = Long.parseLong(sc.nextLine());

                            System.out.println("Introduzca la fecha de la reparación, para ello especifique: ");

                            try {
                                System.out.println("Año de la reparación: ");
                                int anyoReparacion = Integer.parseInt(sc.nextLine());

                                System.out.println("Mes de la reparación: ");
                                int mesReparacion = Integer.parseInt(sc.nextLine());

                                System.out.println("Día de la reparación: ");
                                int diaReparacion = Integer.parseInt(sc.nextLine());

                                LocalDate fechaReparacion = LocalDate.of(anyoReparacion, mesReparacion, diaReparacion);

                                if (fechaReparacion.isBefore(LocalDate.now())){
                                    System.out.println("La fecha no puede ser posterior a la actual");
                                    return;
                                }

                                System.out.println("Por favor, introduzca el coste de la reparación");
                                double precioReparacion = Double.parseDouble(sc.nextLine());

                                System.out.println("Por favor, introduzca una breve descripción de la reparación");
                                String descripcionReparacion = sc.nextLine();

                                if (GestionConcesionario.registrarReparacion(matriculaCoche, idMecanico, fechaReparacion, precioReparacion, descripcionReparacion)) {
                                    System.out.println("Reparación añadida con éxito");
                                } else {
                                    System.out.println("No se ha podido registrar la reparación");
                                }
                            }catch (DateTimeException e){
                                System.out.println("Por favor, introduzca una fecha válida");
                            }

                        } catch (Exception e) {
                            System.out.println("Por favor, al introducir el ID del mecánico asegurese de que sea un valor numérico");
                        }
                    }

                    case 7 -> {
                        boolean dniValido = false;
                        String dni = "";
                        while (!dniValido) {
                            System.out.println("Introduzca el DNI del nuevo propietario");
                            dni = sc.nextLine();

                            if (dni.length() <= 20) {
                                dniValido = true;
                            } else {
                                System.out.println("El DNI no puede tener más de 20 caracteres");
                            }
                        }

                        System.out.println("Introduzca el nombre del nuevo propietario");
                        String nombre = sc.nextLine();

                        String matriculaCoche;
                        while (true) {
                            System.out.println("Introduzca la matrícula del coche que se va a vender");
                            matriculaCoche = sc.nextLine();

                            if (!Coche.matriculaValida(matriculaCoche)) {
                                System.out.println("La matrícula no cumple el patrón [4 dígitos + 3 letras]");
                            } else {
                                break;
                            }
                        }

                        try {
                            System.out.println("Introduzca el ID del conecsionario al que pertenece el coche");
                            Long idConcesionario = Long.parseLong(sc.nextLine());

                            if (GestionConcesionario.venderCoche(dni, nombre, matriculaCoche, idConcesionario)) {
                                System.out.println("Compra realizada con éxito");
                            } else {
                                System.out.println("No se ha podido realizar la venta");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Por favor, asegurese de que el ID del concesionario sea un valor numérico");
                        }

                    }

                    case 8 -> {
                        try {
                            System.out.println("Por favor, introduzca el ID del concesionario del que quiere listar los coches");
                            Long idConcesionario = Long.parseLong(sc.nextLine());

                            System.out.println(GestionConcesionario.listarCochesConcesionario(idConcesionario));

                        } catch (NumberFormatException e) {
                            System.out.println("Por favor, al introducir el ID del concesionario asegurese de que sea un valor numérico");
                        }
                    }

                    case 9 -> {
                        try {
                            System.out.println("Por favor, introduzca el ID del mecanico del que quiere mostrar sus reparaciones");
                            Long idMecanico = Long.parseLong(sc.nextLine());

                            System.out.println(GestionConcesionario.mostrarReparacionMecanico(idMecanico));

                        } catch (NumberFormatException e) {
                            System.out.println("Por favor, al introducir el ID del concesionario asegurese de que sea un valor numérico");
                        }
                    }

                    case 10 -> {
                        System.out.println("Por favor, introduzca el ID del concesionario del que quiere mostrar sus ganancias ventas");

                        Long idConcesionario = Long.parseLong(sc.nextLine());

                        System.out.println(GestionConcesionario.darVentasTotalesConcesionario(idConcesionario));
                    }

                    case 11 -> {

                        String matriculaCoche;
                        while (true){
                            System.out.println("Por favor, introduzca la matrícula del coche que quiere mostrar su precio total actualmente");
                            matriculaCoche = sc.nextLine();

                            if (!Coche.matriculaValida(matriculaCoche)){
                                System.out.println("La matrícula no cumple el patrón [4 dígitos + 3 letras]");
                            } else {
                                break;
                            }
                        }

                        double costeTotalCoche = GestionConcesionario.darCosteActualDeCoche(matriculaCoche);

                        if (costeTotalCoche == -1) {
                            System.out.println("Lo sentimos, no se ha podido sacar el coste total del coche");
                        } else {
                            System.out.println("Coste total del coche -> " + costeTotalCoche);
                        }
                    }

                    case 12 -> {
                        return;
                    }
                    default ->
                            System.out.println("Por favor introduzca un número que corresponda a una de las opciones");
                }

            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduzca un número");
            }
        }
    }

    private static void mostrarMenu() {

        System.out.println("Seleccione una opción");
        System.out.println("1. Iniciar EntityManager");
        System.out.println("2. Borrar estado actual y cargar nuevos datos de prueba");
        System.out.println("3. Dar de alta un concesionario");
        System.out.println("4. Dar de alta un nuevo coche");
        System.out.println("5. Instalar equipamiento a un coche");
        System.out.println("6. Registrar reparación");
        System.out.println("7. Vender Coche");
        System.out.println("8. Listar el stock de un concesionario");
        System.out.println("9. Listar las reparaciones de un Mecánico");
        System.out.println("10. Listar el ventas de un concesionario");
        System.out.println("11. Coste actual de un determinado coche");
        System.out.println("12. Salir");
    }
}