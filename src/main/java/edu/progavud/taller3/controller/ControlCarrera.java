package edu.progavud.taller3.controller;

import edu.progavud.taller3.model.Competidor;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;

/**
 * @author Jorge Mendez
 * @author Julian Roldan
 * @author Jose Cucanchon
 * @version 1.0
 * 
 * Clase que gestiona la logica durante la ejecucion de la carrera,
 * funciona como control principal del aplicativo
 */
public class ControlCarrera {

    private ArrayList<CompetidorHilo> competidoresHilos;
    private ArrayList<Competidor> competidores;
    private ControlVentana cVentana;
    private int numeroCompetidores;
    private int distanciaCarrera;
    /**
     * Constructor de la clase ControlCarrera, inicializa
     * los atributos de la clase
     */
    public ControlCarrera() {
        competidoresHilos = new ArrayList<>();
        competidores = new ArrayList<>();
        cVentana = new ControlVentana(this);
        numeroCompetidores = cVentana.mostrarDigiteNumCompetidores() + 1;
        distanciaCarrera = 100;
        crearCompetidores();
    }
    /**
     * Crea a usain bolt por defecto y ademas
     * segun la cantidad de competidores ingresadas
     * crea los demas competidores y los guarda con su nombre.
     */
    public void crearCompetidores() {
        Competidor competidorNuevo = new Competidor("Usain Bolt");
        competidores.add(competidorNuevo);
        
        for (int i = 1; i < numeroCompetidores; i++) {
            competidorNuevo = new Competidor(cVentana.mostrarDigiteNomCompetidores());
            competidores.add(competidorNuevo);
        }
    }
    /**
     * Por cada competidor en la lista, se le va a 
     * crear un hilo con su respectivo nombre.
     */
    public void inicializarHilos() {
        competidoresHilos.clear();
        for (Competidor competidor : competidores) {
            CompetidorHilo competidorHiloNuevo = new CompetidorHilo(this, competidor);
            competidoresHilos.add(competidorHiloNuevo);
        }
    }
    /**
     * Inicializa los hilos y el tiempo,
     * ademas valida constantemente la vida de 
     * los hilos de modo que al terminarse todos 
     * se detenga el tiempo de la carrera y se defina
     * el ganador
     */
    public void iniciarCarrera() {
        inicializarHilos();
        for (CompetidorHilo competidor : competidoresHilos) {
            competidor.start();
        }
        Timer timer = new Timer(100, null);
        timer.addActionListener(e -> {
            boolean todosTerminaron = true;
            for (CompetidorHilo competidorHilo : competidoresHilos) {
                if (competidorHilo.isAlive()) {
                    todosTerminaron = false;
                    break;
                }
            }
            if (todosTerminaron) {
                timer.stop();
                cVentana.mostrarGanador(definirGanador());
            }
        });
        timer.start();
    }
    /**
     * Método que valida cual fue el mejor tiempo y
     * tambien gestiona si hubo un empate
     * @return Devuelve un mensaje con el ganador o ganadores y su tiempo respectivo
     */
    public String definirGanador() {
        String mensaje = null;
        ArrayList<Integer> indexGanadores = new ArrayList<>();
        long mejorTiempo = competidoresHilos.get(0).getDatosCompetidor().getTiempoDeLlegada();
        for (CompetidorHilo competidor : competidoresHilos) {
            if (competidor.getDatosCompetidor().getTiempoDeLlegada() < mejorTiempo) {
                mejorTiempo = competidor.getDatosCompetidor().getTiempoDeLlegada();
                indexGanadores.clear();
                indexGanadores.add(competidoresHilos.indexOf(competidor));
            } else if (competidor.getDatosCompetidor().getTiempoDeLlegada() == mejorTiempo) {
                indexGanadores.add(competidoresHilos.indexOf(competidor));
            }
        }
        if (indexGanadores.size() > 1) {
            mensaje = "Hubo un empate entre:\n";
            for (int index : indexGanadores) {
                mensaje += competidoresHilos.get(index).getDatosCompetidor().getNombre() + "\n";
                competidoresHilos.get(index).getDatosCompetidor().ganar();
            }
            mensaje += "Con un tiempo de: " + competidoresHilos.get(indexGanadores.get(0)).getDatosCompetidor().getTiempoDeLlegada();
        } else {
            mensaje = "El ganador es: " + competidoresHilos.get(indexGanadores.get(0)).getDatosCompetidor().getNombre()
                    + "\nCon un tiempo de: " + ((float) (competidoresHilos.get(indexGanadores.get(0)).getDatosCompetidor().getTiempoDeLlegada()) / 1000);
            competidoresHilos.get(indexGanadores.get(0)).getDatosCompetidor().ganar();
        }
        return mensaje;
    }
    /**
     * Método que indica que jugador fue el que mas carreras ganó en total.
     * @return Devuelve un mensaje con las victorias de cada jugador y el nombre dle jugador absoluto
     */
    public String definirGanadorAbsoluto() {
        String mensajeVictoriasCompetidor = ""; //esta es la primera parte del mensaje :D
        ArrayList<Integer> indexGanadores = new ArrayList<>();
        int mayorNumVictorias = 0;
        for (CompetidorHilo competidor : competidoresHilos) {
            mensajeVictoriasCompetidor += competidor.getDatosCompetidor().getNombre() + ", Victorias: " + competidor.getDatosCompetidor().getVictorias() + "\n";
            if (competidor.getDatosCompetidor().getVictorias() > mayorNumVictorias) {
                mayorNumVictorias = competidor.getDatosCompetidor().getVictorias();
                indexGanadores.clear();
                indexGanadores.add(competidoresHilos.indexOf(competidor));
            } else if (competidor.getDatosCompetidor().getTiempoDeLlegada() == mayorNumVictorias) {
                indexGanadores.add(competidoresHilos.indexOf(competidor));
            }
        }
        String mensajeGanadorAbsoluto = null;
        if (indexGanadores.size() > 1) {
            mensajeGanadorAbsoluto = "Hubo un empate entre:\n";
            for (int index : indexGanadores) {
                mensajeGanadorAbsoluto += competidoresHilos.get(index).getDatosCompetidor().getNombre() + "\n";
            }
        } else {
            mensajeGanadorAbsoluto = "El ganador Absoluto es: " + competidoresHilos.get(indexGanadores.get(0)).getDatosCompetidor().getNombre();
        }
        return mensajeVictoriasCompetidor + "\n" + mensajeGanadorAbsoluto;
    }
    /**
     * Método que permite visualizar el movimiento del competidor
     * @param competidor Recibe el competidor que se desea mover
     * @param posAvanzada Recibe la cantidad de posiciones que se va a mover el competidor
     */
    public void moverCompetidorLabel(CompetidorHilo competidor, int posAvanzada) {
        cVentana.moverCompetidor(competidoresHilos.indexOf(competidor), posAvanzada, distanciaCarrera);
    }
    /**
     * Método que indica que jugador ha tenido un accidente
     * @return Devuelve el mensaje con el numero del competidor afefctado
     */
    public String accidenteCompetidor() {
        Random rng = new Random();
        int competidorAfectado = rng.nextInt(0, competidores.size());
        competidoresHilos.get(competidorAfectado).accidente(); //Si en vez de 0, va un random de 0 hasta el size del array, se hace al azar para todos :D
        return "El competidor " + (competidorAfectado + 1) +" se accidento"; //tambien, con el random, esto es para el label que esta en el panel
    }
    /**
     * Método que indica que jugador ha sido impulsado
     * @return Devuelve el mensaje con el numero del competidor afectado
     */
    public String impulsarCompetidor() {
        Random rng = new Random();
        int competidorAfectado = rng.nextInt(0, competidores.size());
        competidoresHilos.get(competidorAfectado).impulsar();
        return "El competidor: " + (competidorAfectado + 1) +"se impulso";
    }

    public int getDistanciaCarrera() {
        return distanciaCarrera;
    }

    public int getNumeroCompetidores() {
        return numeroCompetidores;
    }

    public ControlVentana getcVentana() {
        return cVentana;
    }
    /**
     * Método que termina el programa durante la carrera o 
     * en la pantalla del final.
     */
    public void cerrarPrograma() {
        
        for (CompetidorHilo competidor : competidoresHilos) {
            if (competidor.isAlive()) {
                competidor.pausarHilo();
            }
        }
        cVentana.mostrarMensaje(definirGanadorAbsoluto());
        System.exit(0);
    }
}
