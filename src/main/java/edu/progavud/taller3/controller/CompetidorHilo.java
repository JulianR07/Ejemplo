/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.progavud.taller3.controller;

import edu.progavud.taller3.model.Competidor;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

/**
 *
 * @author Miguel
 */
public class CompetidorHilo extends Thread {

    private Competidor competidor;
    private ControlCarrera cCarrera;
    private boolean isPausado;
    private Random rng;

    public CompetidorHilo(ControlCarrera cCarrera, Competidor competidor) {
        this.cCarrera = cCarrera;
        this.competidor = competidor;
        rng = new Random();
        isPausado = false;
    }

    @Override
    public void run() {
            competidor.reiniciarPosicion();
            long tiempoInicio = System.currentTimeMillis();
            while (competidor.getPosicion() < cCarrera.getDistanciaCarrera()) {
                if(isPausado) {
                    LockSupport.park();
                }
                competidor.mover(1);
                cCarrera.moverCompetidorLabel(this, 1);
                try {
                    sleep(rng.nextInt(0, 250));
                } catch (InterruptedException ex) {
                    System.out.println("Soy el metodo run, algo paso");
                }
                System.out.println("El competidor: " + competidor.getNombre() + " Se ha movido, su pos actual es: " + competidor.getPosicion());
            }
            competidor.setTiempoDeLlegada(System.currentTimeMillis() - tiempoInicio);       
    }

    public void accidente() {
        try {
            sleep(rng.nextInt(0, 250));
        } catch (InterruptedException ex) {
            System.out.println("Soy el metodo accidente, algo paso");
        }
    }

    public void impulsar() {
        competidor.mover(5);
        cCarrera.moverCompetidorLabel(this, 5);
    }
    
    public void pausarHilo() {
        isPausado = true;
    }

    public Competidor getDatosCompetidor() {
        return competidor;
    }

}
