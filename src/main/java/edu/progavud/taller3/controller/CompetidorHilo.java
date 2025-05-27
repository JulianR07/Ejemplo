/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.progavud.taller3.controller;

import edu.progavud.taller3.model.Competidor;
import java.util.Random;

/**
 *
 * @author Miguel
 */
public class CompetidorHilo extends Thread {

    private Competidor competidor;
    private ControlCarrera cCarrera;
    private Random rng;

    public CompetidorHilo(ControlCarrera cCarrera, String nombre) {
        this.cCarrera = cCarrera;
        competidor = new Competidor(nombre);
        rng = new Random();
    }

    @Override
    public void run() {
        competidor.reiniciarPosicion();
        long tiempoInicio = System.currentTimeMillis();
        while (competidor.getPosicion() < cCarrera.getDistanciaCarrera()) {
            competidor.mover(1);
            cCarrera.moverCompetidor(this, 1);
            try {
                sleep(rng.nextInt(0, 250));
            } catch (InterruptedException ex) {
                System.out.println("Soy el metodo run, algo paso");
            }
            System.out.println("El competidor: " + competidor.getNombre() + " Se ha movido, su pos actual es: " + competidor.getPosicion());
        }
        competidor.setTiempoDeLlegada(System.currentTimeMillis() - tiempoInicio);
        System.out.println("El competidor: " + competidor.getNombre() + " Llego, su tiempo es: " + ((float) (competidor.getTiempoDeLlegada())/1000));
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
        cCarrera.moverCompetidor(this, 5);
    }

    public Competidor getDatosCompetidor() {
        return competidor;
    }

}
