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
 * Clase que gestiona la logica durante la ejecucion de la carrera, funciona
 * como control principal del aplicativo
 */
public class ControlCarrera {

    private ArrayList<CompetidorHilo> competidoresHilos;
    private ControlCompetidor cCompetidor;
    private ControlVentana cVentana;
    private int distanciaCarrera;

    /**
     * Constructor de la clase ControlCarrera, inicializa los atributos de la
     * clase
     */
    public ControlCarrera() {
        competidoresHilos = new ArrayList<>();
        cVentana = new ControlVentana(this);
        cCompetidor = new ControlCompetidor(this, cVentana.mostrarDigiteNumCompetidores());
        distanciaCarrera = 100;
        crearCompetidores();
        cVentana.definirRecursosCarrera();
    }

    /**
     * Crea a usain bolt por defecto y ademas segun la cantidad de competidores
     * ingresadas crea los demas competidores y los guarda con su nombre.
     */
    public void crearCompetidores() {
        cCompetidor.crearCompetidor("Usain Bolt");
        for (int i = 1; i < cCompetidor.getNumeroCompetidores(); i++) {
            cCompetidor.crearCompetidor(cVentana.mostrarDigiteNomCompetidores());
        }
    }

    /**
     * Por cada competidor creado de ControlCompetidor, se va a crear un hilo, que maneje el competidor agregado;
     */
    public void inicializarHilos() {
        competidoresHilos.clear();
        for (int i = 0; i < cCompetidor.getNumeroCompetidores(); i++) {
            CompetidorHilo competidorHiloNuevo = new CompetidorHilo(this, cCompetidor.getCompetidor(i));
            competidoresHilos.add(competidorHiloNuevo);
        }
    }

    /**
     * Inicializa los hilos y el tiempo, ademas valida constantemente la vida de
     * los hilos de modo que al terminarse todos se detenga el tiempo de la
     * carrera y se defina el ganador
     */
    public void iniciarCarrera() {
        inicializarHilos();
        for (CompetidorHilo competidor : competidoresHilos) {
            competidor.start();
        }
        Timer timer = new Timer(50, null);
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
                cVentana.mostrarGanador(cCompetidor.definirGanador());
            }
        });
        timer.start();
    }

    /**
     * Método que permite visualizar el movimiento del competidor
     *
     * @param competidor Recibe el competidor que se desea mover
     * @param posAvanzada Recibe la cantidad de posiciones que se va a mover el
     * competidor
     */
    public void moverCompetidorLabel(CompetidorHilo competidor, int posAvanzada) {
        cVentana.moverCompetidor(competidoresHilos.indexOf(competidor), posAvanzada, distanciaCarrera);
    }

    /**
     * Método que indica que jugador ha tenido un accidente
     *
     * @return Devuelve el mensaje con el numero del competidor afefctado
     */
    public String accidenteCompetidor() {
        Random rng = new Random();
        int competidorAfectado = rng.nextInt(0, cCompetidor.getNumeroCompetidores());
        competidoresHilos.get(competidorAfectado).accidente(); 
        return "El competidor " + (competidorAfectado + 1) + " se accidento";
    }

    /**
     * Método que indica que jugador ha sido impulsado
     *
     * @return Devuelve el mensaje con el numero del competidor afectado
     */
    public String impulsarCompetidor() {
        Random rng = new Random();
        int competidorAfectado = rng.nextInt(0,  cCompetidor.getNumeroCompetidores());
        competidoresHilos.get(competidorAfectado).impulsar();
        return "El competidor: " + (competidorAfectado + 1) + " se impulso";
    }

    public int getDistanciaCarrera() {
        return distanciaCarrera;
    }

    public int getNumeroCompetidores() {
        return cCompetidor.getNumeroCompetidores();
    }

    public ControlVentana getcVentana() {
        return cVentana;
    }

    public String getNombreCompetidor(int indexCompetidor) {
        return cCompetidor.getCompetidor(indexCompetidor).getNombre();
    }

    /**
     * Método que termina el programa durante la carrera o en la pantalla del
     * final.
     */
    public void cerrarPrograma() {

        for (CompetidorHilo competidor : competidoresHilos) {
            if (competidor.isAlive()) {
                competidor.pausarHilo();
            }
        }
        cVentana.mostrarMensaje(cCompetidor.definirGanadorAbsoluto());
        System.exit(0);
    }
}
