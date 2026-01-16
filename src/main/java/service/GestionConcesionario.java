package service;

import jakarta.persistence.*;
import model.*;
import utils.GeneradorInformacion;

import java.time.LocalDate;
import java.util.List;

public class GestionConcesionario {

    private static EntityManagerFactory emf;

    /**
     * Metodo encargado de crear el objeto EntityManager
     */
    public static boolean iniciarEntityManager() {

        try {
            emf = Persistence.createEntityManagerFactory("miUnidadPersistencia");

            EntityManager em = emf.createEntityManager();
            em.close();

            return true;
        } catch (Exception e) {
            System.err.println("Error al inicializar el Entity Manager: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo encargado de borrar los datos que hay actualmente en
     * la base de datos para poner unos nuevos predefinidos
     *
     * @return devuelve true o false en función de si se ha podido o no realizar sustituir los datos
     */
    public static boolean limpiarEstadoYCargarDatos() {
        if (emf == null) {
            return false;
        }
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Borramos las tablas(con el nombre de la clase) según su orden de dependencia
            // depende de mecanico
            em.createQuery("delete from Reparacion").executeUpdate();
            // no depende
            em.createQuery("delete from Mecanico").executeUpdate();
            // depende de propietario, comcesionario y coche
            em.createQuery("delete from Venta").executeUpdate();
            // depende de concesionario y propietario
            em.createQuery("delete from Coche").executeUpdate();
            // no depende
            em.createQuery("delete from Equipamiento").executeUpdate();
            // no depende
            em.createQuery("delete from Propietario").executeUpdate();
            // no depende
            em.createQuery("delete from Concesionario").executeUpdate();

            // Cargamos los nuevos datos
            Concesionario concesionario = new Concesionario("Talleres Freddy", "Calle Elm Street 131");
            Equipamiento equipamiento1 = new Equipamiento("Retrovisores eléctricos", 20);
            Equipamiento equipamiento2 = new Equipamiento("Aire acondicionado", 500);
            Mecanico mecanico1 = new Mecanico("Chapa y pintura", "Freddy");
            Mecanico mecanico2 = new Mecanico("Neumáticos", "Jason");
            Propietario propietario1 = new Propietario("29640826K", "Billy");
            Propietario propietario2 = new Propietario("29672846K", "Stu");
            Coche coche1 = new Coche("1111AAA", "Seat", "Ibiza", 7000, concesionario);
            Coche coche2 = new Coche("2222BBB", "Renault", "Clio", 5000, concesionario);

            // guardamos los datos creados en nuestra base de datos en orden inverso al de borrado
            em.persist(concesionario);
            em.persist(propietario1);
            em.persist(propietario2);
            em.persist(equipamiento1);
            em.persist(equipamiento2);
            em.persist(mecanico1);
            em.persist(mecanico2);

            coche1.getListaDeEquipamientos().add(equipamiento1);

            coche2.getListaDeEquipamientos().add(equipamiento2);

            em.persist(coche1);
            em.persist(coche2);

            // si no ha ocurrido ninun error hacemos el commit
            em.getTransaction().commit();

            return true;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        // si ocurre algún error, devolvemos false
        return false;
    }

    /**
     * Metodo encargado de dar de alta un nuevo concesionario
     *
     * @param nombreConcesionario    nombre del Concesionario
     * @param direccionConcesionario dirección del Concesionario
     * @return devuelve true o false en función de si se ha podido o no dar de alta el nuevo Concesionario
     */
    public static boolean darDeAltaConcesionario(String nombreConcesionario, String direccionConcesionario) {

        if (emf == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Concesionario concesionario = new Concesionario(nombreConcesionario, direccionConcesionario);

            em.persist(concesionario);

            em.getTransaction().commit();

            return true;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return false;
    }

    /**
     * Metodo encargado de dar de alta un nuevo coche
     *
     * @param matriculaCoche  matricula del coche a dar de alta
     * @param marcaCoche      marca del coche a dar de alta
     * @param modeloCoche     modelo del coche a dar de alta
     * @param precioBaseCoche precio del coche a dar de alta
     * @param idConcesionario ID del concesionario donde se quiere guardar el coche
     * @return devuelve true o false en función de si se ha podido o no dar de alta el nuevo Coche
     */
    public static boolean darDeAltaCoche(String matriculaCoche, String marcaCoche, String modeloCoche, double precioBaseCoche, Long idConcesionario) {
        if (emf == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();

        try {
            // guardamos el Concesionario al que corresponde al Id indicado por el usuario
            Concesionario concesionarioAGuardarCoche = em.find(Concesionario.class, idConcesionario);

            if (concesionarioAGuardarCoche == null) {
                return false;
            }
            // iniciam,os transaccion para añadir el coche
            em.getTransaction().begin();

            Coche cocheAAnnadir = new Coche(matriculaCoche, marcaCoche, modeloCoche, precioBaseCoche, concesionarioAGuardarCoche);

            em.persist(cocheAAnnadir);

            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return false;
    }

    /**
     * Metodo encargado de añadir equipamientos a la lista
     * de equipamientos de un coche
     *
     * @param matriculaCoche matricula del coche (clave primaria de coche)
     * @param idEquipamiento ID del equipamiento (clave primario de equipamiento)
     * @return devuelve true o false en función de si se ha podido o no añadir el equipamiento al coche
     */
    public static double instalarEquipamientoACoche(String matriculaCoche, Long idEquipamiento) {
        if (emf == null) {
            return -1;
        }
        EntityManager em = emf.createEntityManager();

        // guardamos el equipamiento asociado al id recibido
        Equipamiento equipamientoAAnnadir = em.find(Equipamiento.class, idEquipamiento);

        // guardamos el coche asociado la matrícula recibida
        Coche cocheAAnnadirEquipamiento = em.find(Coche.class, matriculaCoche);

        // si no existen finalizamos
        if (equipamientoAAnnadir == null || cocheAAnnadirEquipamiento == null) {
            return -1;
        }

        // si el coche ya tenia el equipamiento finalizamos
        if (cocheAAnnadirEquipamiento.getListaDeEquipamientos().contains(equipamientoAAnnadir)) {
            return -1;
        }

        try {
            // en una transaccion añadimos el equipamiento al coche y el coche al equipamiento
            em.getTransaction().begin();

            cocheAAnnadirEquipamiento.getListaDeEquipamientos().add(equipamientoAAnnadir);

            equipamientoAAnnadir.getListaCoches().add(cocheAAnnadirEquipamiento);

            em.getTransaction().commit();

            // sacamos el precio del coche base + el precio de los equipamientos
            double precioTotalDelCoche = cocheAAnnadirEquipamiento.getPrecioBase();

            for (Equipamiento equipamiento : cocheAAnnadirEquipamiento.getListaDeEquipamientos()) {
                precioTotalDelCoche += equipamiento.getCoste();
            }

            return precioTotalDelCoche;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return -1;
    }

    /**
     * Metodo encargado de añadir una nueva reparación
     *
     * @param matriculaCoche        matrícula del coche al que se le va a asignar la reparación
     * @param idMecanico            ID del mecánico al que se le va a asignar la reparación
     * @param fechaReparacion       fecha de la reparación
     * @param precioReparacion      precio de la reparación
     * @param descripcionReparacion breve descripción de la reparación
     * @return devuelve true o false en función de si se ha podido o no registrar la reparación
     */
    public static boolean registrarReparacion(String matriculaCoche, Long idMecanico, LocalDate fechaReparacion, double precioReparacion, String descripcionReparacion) {
        if (emf == null) {
            return false;
        }
        EntityManager em = emf.createEntityManager();

        // guardamos el coche asignado a la matricula recibida
        Coche cocheAAsignarReparacion = em.find(Coche.class, matriculaCoche);

        // guardamos el mecanico asignado al id recibido
        Mecanico mecanicoAAsignarReparacion = em.find(Mecanico.class, idMecanico);

        // si no exiten, finalizamos
        if (cocheAAsignarReparacion == null || mecanicoAAsignarReparacion == null) {
            return false;
        }

        try {
            // en una transaccion, añadimos la reparacion con los datos recibidos por parametro
            em.getTransaction().begin();

            Reparacion reparacion = new Reparacion(fechaReparacion, precioReparacion, descripcionReparacion, cocheAAsignarReparacion, mecanicoAAsignarReparacion);

            em.persist(reparacion);

            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return false;
    }

    /**
     * Metodo encargado de realizar la venta de un coche de un
     * concesionario y asignandosela a un nuevo propietario
     *
     * @param dniNuevoPropietario    dni del nuevo propietario del coche
     * @param nombreNuevoPropietario nombre del nuevo rpopietario del coche
     * @param matriculaCoche         matricula del coche a transferir
     * @param idConcesionario        id del Concesionario al que pertenece el coche
     * @return devuelve true o false en funcion de si ha podido o no relizar la venta del coche
     */
    public static boolean venderCoche(String dniNuevoPropietario, String nombreNuevoPropietario, String matriculaCoche, Long idConcesionario) {
        if (emf == null) {
            return false;
        }
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // sacamos los objetos asociados a las claves recibidas y validamos que existan
            Coche coche = em.find(Coche.class, matriculaCoche);
            Concesionario concesionario = em.find(Concesionario.class, idConcesionario);

            if (coche == null || concesionario == null || coche.getPropietario() != null) {
                return false;
            }

            // parae vitar propietarios duplicados nos aseguramos de que no existan propietarios con el DNI recibido
            // importante meterle dentro de un try catch ya que si no encuentra el prtopietario finalizaria el metodo
            Propietario propietario = null;
            try {
                // sacamos el Propietario asociado al dni recibido
                String stringConsultaPropietario = "select p from Propietario p where p.dni = :dniRecibido";
                TypedQuery<Propietario> consultaPropietario = em.createQuery(stringConsultaPropietario, Propietario.class);
                consultaPropietario.setParameter("dniRecibido", dniNuevoPropietario);
                propietario = consultaPropietario.getSingleResult();
            } catch (NoResultException e) {
            }

            // si el usuario no existia, lo creamos, en caso de que ya existiera, le añadimos el coche solamente
            if (propietario == null) {
                // creamos nuevo propietario y le añadimos el coche
                propietario = new Propietario(dniNuevoPropietario, nombreNuevoPropietario);
                em.persist(propietario);
            }

            coche.setPropietario(propietario);
            coche.setConcesionario(null);

            // para crear la venta, sacamos le precio base del coche
            double precioTotalDelCoche = coche.getPrecioBase();

            if (!coche.getListaDeEquipamientos().isEmpty()) {
                for (Equipamiento equipamiento : coche.getListaDeEquipamientos()) {
                    precioTotalDelCoche += equipamiento.getCoste();
                }
            }

            // sacamos el coste de todas las reparaciones del coche, para sumarlas al precio base
            String strConsultaCosteReparaciones = "select sum(r.coste) from Reparacion r where r.coche.matricula = :matriculaRecibida";
            TypedQuery<Double> consultaCosteReparaciones = em.createQuery(strConsultaCosteReparaciones, Double.class);
            consultaCosteReparaciones.setParameter("matriculaRecibida", matriculaCoche);
            Double costeReparaciones = consultaCosteReparaciones.getSingleResult();

            // si hay reparaciones asociadas al coche le sumamos el precio
            if (costeReparaciones != null) precioTotalDelCoche += costeReparaciones;

            // Creamos la venta
            Venta venta = new Venta(LocalDate.now(), precioTotalDelCoche, concesionario, propietario, coche);
            em.persist(venta);

            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return false;
    }

    /**
     * Metodo encargado de mostrar los datos de los Coches de un concesionario
     *
     * @param idConcesionario ID del Concesionario del que se quiere mostrar el stock
     * @return devuelve un String con los datos de los Coches del Concesionario especificado
     */
    public static String listarCochesConcesionario(Long idConcesionario) {
        if (emf == null) {
            return "El Entity Manager Factory debe ser inicializado";
        }
        EntityManager em = emf.createEntityManager();

        try {
            // sacamos el concesionario asociado al id recibido y validamos que exista
            Concesionario concesionario = em.find(Concesionario.class, idConcesionario);

            if (concesionario == null) {
                return "No se ha podido encontrar el concesionario con ID: " + idConcesionario;
            }

            // seleccionamos el Coche de la tabla Coche que tenga el atributo concesionario con el id especificado por parametro
            // y ademas su atributo propietario sea nulo
            String stringConsultaCochesConcesionario = "select c from Coche c where c.concesionario.id = :idConcesionario AND c.propietario is null";

            // creamos la consulta, especificando lo que va a devolver y sustituyendo los parametros de esta por el idConcesionario que recibimos por parametro
            TypedQuery<Coche> consultaCochesConcesionario = em.createQuery(stringConsultaCochesConcesionario, Coche.class);

            consultaCochesConcesionario.setParameter("idConcesionario", idConcesionario);

            // ejecutamos la consulta y guardamos la lista de Coches obtenida
            List<Coche> listaCochesConcesionario = consultaCochesConcesionario.getResultList();

            if (listaCochesConcesionario.isEmpty()) {
                return "No se han encontrado coches en el concesionario";
            }

            // oara crear un String con toda la informacion de los coches concesionario
            StringBuilder stringCochesConcesionario = new StringBuilder();

            int contadorCoches = 0;
            for (Coche cocheActual : listaCochesConcesionario) {

                contadorCoches++;
                stringCochesConcesionario.append("==============================================\n");
                stringCochesConcesionario.append("Coche número " + contadorCoches + ": \n")
                        .append(GeneradorInformacion.darDatosCoche(cocheActual)).append(System.lineSeparator());
            }
            return stringCochesConcesionario.toString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            em.close();
        }
        return "Lo sentimos mucho, ha ocurrido un error inesperado.";
    }

    /**
     * Metodo encargado de crear un String que muestra la información
     * de todas las reparaciones de un determinado Mecanico
     *
     * @param idMecanico ID del mecánico del que se quiere mostrar las reparaciones
     * @return devuelve un String con la información de las Reparaciones asociadas al mecánico recibido
     */
    public static String mostrarReparacionMecanico(Long idMecanico) {
        if (emf == null) {
            return "El Entity Manager Factory debe ser inicializado";
        }
        EntityManager em = emf.createEntityManager();

        try {
            // Sacamos el mecánico asociado al id recibido y validamos que exista
            Mecanico mecanico = em.find(Mecanico.class, idMecanico);

            if (mecanico == null) return "No se han encontrado ningún mecánico con el ID: " + idMecanico;

            // sacamos la lista de reparaciones del mecanico
            String stringConsultaReparacionesMecanico = "select r from Reparacion r where r.mecanico.id = :idMecanico";

            TypedQuery<Reparacion> consultaReparacionesMecanico = em.createQuery(stringConsultaReparacionesMecanico, Reparacion.class);
            consultaReparacionesMecanico.setParameter("idMecanico", idMecanico);

            List<Reparacion> listaReparacionesMecanico = consultaReparacionesMecanico.getResultList();

            // si no hay reparaciones asociadas al mecanico, finalizamos
            if (listaReparacionesMecanico.isEmpty())
                return "No hay reparaciones asociadas al mecánico con ID: " + idMecanico;

            // recorremos las reparaciones y añadimos su información a un String
            StringBuilder stringReparacionesMecanico = new StringBuilder();

            int contadorReparaciones = 1;
            for (Reparacion reparacionActual : listaReparacionesMecanico) {
                contadorReparaciones++;

                stringReparacionesMecanico.append("==============================================\n");
                stringReparacionesMecanico.append("Reparación número " + contadorReparaciones + ": \n")
                        .append(GeneradorInformacion.darDatosReparacion(reparacionActual))
                        .append(System.lineSeparator());
            }
            return stringReparacionesMecanico.toString();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            em.close();
        }
        return "Lo sentimos mucho, ha ocurrido un error inesperado.";
    }

    /**
     * Metodo encargado de crear un String con la información de las ventas de un
     * determinado concesionario, informando también de las ganancias de este
     *
     * @param idConcesionario ID del Concesionario a mostrar la información
     * @return devuelve un String con la información de las ventas de un
     * determinado concesionario y las ganancias de este
     */
    public static String darVentasTotalesConcesionario(Long idConcesionario) {
        if (emf == null) return "El Entity Manager Factory debe ser inicializado";

        EntityManager em = emf.createEntityManager();

        try {
            // Sacamos la lista de Ventas del concesionario
            String stringConsultaVentas = "select v from Venta v where v.concesionario.id = :idConcesionario"; // string para la consulta
            TypedQuery<Venta> consultaVentas = em.createQuery(stringConsultaVentas, Venta.class); // consulta
            consultaVentas.setParameter("idConcesionario", idConcesionario); // ajustamos los parametros
            List<Venta> listaVentasConcesionario = consultaVentas.getResultList(); // sacamos la lista de las ventas del concesionario

            // si no hay ventas asociadas al concesionario, finalizamos
            if (listaVentasConcesionario.isEmpty())
                return "No se han encontrado ventas asociadas al concesionario seleccionado";

            // recorremos las ventas añadimos su informacion a un String
            StringBuilder stringVentasConcesionario = new StringBuilder();
            int contadorVentas = 1;
            double totalRecaudado = 0;
            for (Venta ventaActual : listaVentasConcesionario) {

                stringVentasConcesionario.append("==============================================\n");
                stringVentasConcesionario.append("Venta número " + contadorVentas + ": ");
                stringVentasConcesionario.append(GeneradorInformacion.darDatosVenta(ventaActual));
                stringVentasConcesionario.append(System.lineSeparator());

                totalRecaudado += ventaActual.getPrecioFinal();
            }
            stringVentasConcesionario.append("\n----------------------------------------------");
            stringVentasConcesionario.append("[¡¡¡ Total recaudado -> " + totalRecaudado + " !!!]");

            return stringVentasConcesionario.toString();

        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        return "Lo sentimos, ha ocurrido un error";
    }

    /**
     * Metodo encargado de calcular y devolver el coste total invertido en un coche
     *
     * @param matriculaCoche matricula del coche del que se quiere calcular su precio total
     * @return devuelve un double que indica el precio total invertido en el coche con la matricula recibida
     */
    public static double darCosteActualDeCoche(String matriculaCoche) {
        if (emf == null) return -1;
        EntityManager em = emf.createEntityManager();
        try {
            // comprobamos que el coche exista
            Coche coche = em.find(Coche.class, matriculaCoche);
            if (coche == null) return -1;

            // sacamos el precio base del coche
            String stringConsultaPrecio = "select c.precioBase from Coche c where c.matricula = :matriculaRecibida AND c.propietario is not null";
            TypedQuery<Double> consultaPrecio = em.createQuery(stringConsultaPrecio, Double.class);
            consultaPrecio.setParameter("matriculaRecibida", matriculaCoche);

            Double precioBaseCoche;
            try {
                precioBaseCoche = consultaPrecio.getSingleResult();
            } catch (NoResultException e) {
                System.err.println("El coche debe tener propietario");
                return -1;
            }

            // sacamos el coste total de las reparaciones del coche
            String stringConsultaReparaciones = "select sum(r.coste) from Reparacion r where r.coche.matricula = :matriculaRecibida";
            TypedQuery<Double> consultaReparacion = em.createQuery(stringConsultaReparaciones, Double.class);
            consultaReparacion.setParameter("matriculaRecibida", matriculaCoche);
            Double costeTotalReparaciones = consultaReparacion.getSingleResult();

            // sacamose el coste de los equipamientos -> importante especificar que vamos a pasar por la tabla intermedia --> c.listaDeEquipamientos
            String stringConsultaEquipamiento = "select sum(e.coste) from Coche c JOIN c.listaDeEquipamientos e where c.matricula = :matriculaRecibida";
            TypedQuery<Double> consultaEquipamiento = em.createQuery(stringConsultaEquipamiento, Double.class);
            consultaEquipamiento.setParameter("matriculaRecibida", matriculaCoche);
            Double costeTotalEquipamientos = consultaEquipamiento.getSingleResult();

            // validamos que no nos ha llegaod ningun valor nulo porque sino al sumarlo el programa fallaria

            if (costeTotalReparaciones == null) costeTotalReparaciones = 0.0;
            if (precioBaseCoche == null) precioBaseCoche = 0.0;
            if (costeTotalEquipamientos == null) costeTotalEquipamientos = 0.0;

            return precioBaseCoche + costeTotalReparaciones + costeTotalEquipamientos;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            em.close();
        }
        return -1;
    }
}
