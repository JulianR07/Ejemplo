package edu.progavud.taller3.controller;

import edu.progavud.taller3.model.Competidor;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Jorge Mendez
 * @author Julian Roldan
 * @author Jose Cucanchon
 * @version 1.0
 *
 * Clase donde esta el metodo run() correspondiente a los hilos de ejecucion del
 * programa
 */
public class CompetidorHilo extends Thread {

    private Competidor competidor;
    private ControlCarrera cCarrera;
    private boolean isPausado;
    private Random rng;

    /**
     * Constructor de la clase CompetidorHilo
     *
     * @param cCarrera Recibe el parametro que permite la comunicacion con
     * ControlCarrera
     * @param competidor Recibe un competidor que respresentará un hilo
     */
    public CompetidorHilo(ControlCarrera cCarrera, Competidor competidor) {
        this.cCarrera = cCarrera;
        this.competidor = competidor;
        rng = new Random();
        isPausado = false;
    }

    /**
     * Método run() que le permite a los competidores moverse 1 posicion y
     * posterior a esto dormir una cantidad aleatoria de tiempo, eventualmente
     * cuando recorran la distancia total de la carrera el tiempo de llegada se
     * almacenta en un atributo de cada competidor respectivo
     */
    @Override
    public void run() {
        competidor.reiniciarPosicion();
        long tiempoInicio = System.currentTimeMillis();
        while (competidor.getPosicion() < cCarrera.getDistanciaCarrera()) {
            if (isPausado) {
                LockSupport.park();
            }
            competidor.mover(1);
            cCarrera.moverCompetidorLabel(this, 1);
            try {
                sleep(rng.nextInt(0, 250));
            } catch (InterruptedException ex) {
                cCarrera.getcVentana().mostrarMensaje("Error: Hilo Interrumpido");
            }
            // todavia no lo quité por si depronto te sirve :3 System.out.println("El competidor: " + competidor.getNombre() + " Se ha movido, su pos actual es: " + competidor.getPosicion());
        }
        competidor.setTiempoDeLlegada(System.currentTimeMillis() - tiempoInicio);
    }

    /**
     * Método que duerme un hilo aleatorio una cantidad aleatoria de tiempo
     */
    public void accidente() {
        try {
            sleep(rng.nextInt(0, 250));
        } catch (InterruptedException ex) {
            cCarrera.getcVentana().mostrarMensaje("Soy el metodo accidente, algo paso");
        }
    }

    /**
     * Método que impulsa 5 posiciones a un competidor aleatorio
     */
    public void impulsar() {
        competidor.mover(5);
        cCarrera.moverCompetidorLabel(this, 5);
    }

    public void pausarHilo() {
        isPausado = true;
    }
}
