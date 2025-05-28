/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.progavud.taller3.controller;

import edu.progavud.taller3.model.Competidor;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author Miguel
 */
public class ControlCarrera {

    private ArrayList<CompetidorHilo> competidoresHilos;
    private ArrayList<Competidor> competidores;
    private ControlVentana cVentana;
    private int numeroCompetidores;
    private int distanciaCarrera;

    public ControlCarrera() {
        competidoresHilos = new ArrayList<>();
        competidores = new ArrayList<>();
        cVentana = new ControlVentana(this);
        numeroCompetidores = cVentana.mostrarDigiteNumCompetidores() + 1;
        distanciaCarrera = 100;
        crearCompetidores();
    }

    public void crearCompetidores() { //este metodo se debe generalizar :3
        Competidor competidorNuevo = new Competidor("Usain Bolt");

        competidores.add(competidorNuevo);
        for (int i = 1; i < numeroCompetidores; i++) {
            competidorNuevo = new Competidor(cVentana.mostrarDigiteNomCompetidores());
            competidores.add(competidorNuevo);
        }
    }

    public void inicializarHilos() {
        competidoresHilos.clear();
        for (Competidor competidor : competidores) {
            CompetidorHilo competidorHiloNuevo = new CompetidorHilo(this, competidor);
            competidoresHilos.add(competidorHiloNuevo);
        }
    }

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

    public void moverCompetidorLabel(CompetidorHilo competidor, int posAvanzada) {
        cVentana.moverCompetidor(competidoresHilos.indexOf(competidor), posAvanzada, distanciaCarrera);
    }

    public String accidenteCompetidor() {
        Random rng = new Random();
        int competidorAfectado = rng.nextInt(0, competidores.size());
        competidoresHilos.get(competidorAfectado).accidente(); //Si en vez de 0, va un random de 0 hasta el size del array, se hace al azar para todos :D
        return "El competidor " + (competidorAfectado + 1) +" se accidento"; //tambien, con el random, esto es para el label que esta en el panel
    }

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
