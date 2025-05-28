/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.progavud.taller3.controller;

import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author Miguel
 */
public class ControlCarrera {

    private ArrayList<CompetidorHilo> competidores;
    private ControlVentana cVentana;
    private int numeroCompetidores;
    private int distanciaCarrera;

    public ControlCarrera() {
        competidores = new ArrayList<>();
        cVentana = new ControlVentana(this);
        numeroCompetidores = cVentana.mostrarDigiteNumCompetidores() + 1;
        distanciaCarrera = 100;
        crearCompetidores();

    }

    public void crearCompetidores() { //este metodo se debe generalizar :3
        CompetidorHilo competidorNuevo = new CompetidorHilo(this, "Usain Bolt");
        competidores.add(competidorNuevo);
        for (int i = 1; i < numeroCompetidores; i++) {
            competidorNuevo = new CompetidorHilo(this, cVentana.mostrarDigiteNomCompetidores());
            competidores.add(competidorNuevo);
        }
    }

    public void iniciarCarrera() {
        for (CompetidorHilo competidor : competidores) {
            competidor.start();
        }
        Timer timer = new Timer(100, null);
        timer.addActionListener(e -> {
            boolean todosTerminaron = true;
            for (CompetidorHilo competidor : competidores) {
                if (competidor.isAlive()) {
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
        long mejorTiempo = competidores.get(0).getDatosCompetidor().getTiempoDeLlegada();
        for (CompetidorHilo competidor : competidores) {
            if (competidor.getDatosCompetidor().getTiempoDeLlegada() < mejorTiempo) {
                mejorTiempo = competidor.getDatosCompetidor().getTiempoDeLlegada();
                indexGanadores.clear();
                indexGanadores.add(competidores.indexOf(competidor));
            } else if (competidor.getDatosCompetidor().getTiempoDeLlegada() == mejorTiempo) {
                indexGanadores.add(competidores.indexOf(competidor));
            }
        }
        if (indexGanadores.size() > 1) {
            mensaje = "Hubo un empate entre:\n";
            for (int index : indexGanadores) {
                mensaje += competidores.get(index).getDatosCompetidor().getNombre() + "\n";
                competidores.get(index).getDatosCompetidor().ganar();
            }
            mensaje += "Con un tiempo de: " + competidores.get(indexGanadores.get(0)).getDatosCompetidor().getTiempoDeLlegada();
        } else {
            mensaje = "El ganador es: " + competidores.get(indexGanadores.get(0)).getDatosCompetidor().getNombre()
                    + "\nCon un tiempo de: " + ((float) (competidores.get(indexGanadores.get(0)).getDatosCompetidor().getTiempoDeLlegada()) / 1000);
            competidores.get(indexGanadores.get(0)).getDatosCompetidor().ganar();
        }
        return mensaje;
    }

    public String definirGanadorAbsoluto() {
        String mensajeVictoriasCompetidor = ""; //esta es la primera parte del mensaje :D
        ArrayList<Integer> indexGanadores = new ArrayList<>();
        int mayorNumVictorias = 0;
        for (CompetidorHilo competidor : competidores) {
            mensajeVictoriasCompetidor += competidor.getDatosCompetidor().getNombre() + ", Victorias: " + competidor.getDatosCompetidor().getVictorias() + "\n";
            if (competidor.getDatosCompetidor().getVictorias() > mayorNumVictorias) {
                mayorNumVictorias = competidor.getDatosCompetidor().getVictorias();
                indexGanadores.clear();
                indexGanadores.add(competidores.indexOf(competidor));
            } else if (competidor.getDatosCompetidor().getTiempoDeLlegada() == mayorNumVictorias) {
                indexGanadores.add(competidores.indexOf(competidor));
            }
        }
        String mensajeGanadorAbsoluto = null;
        if (indexGanadores.size() > 1) {
            mensajeGanadorAbsoluto = "Hubo un empate entre:\n";
            for (int index : indexGanadores) {
                mensajeGanadorAbsoluto += competidores.get(index).getDatosCompetidor().getNombre() + "\n";
            }
        } else {
            mensajeGanadorAbsoluto = "El ganador Absoluto es: " + competidores.get(indexGanadores.get(0)).getDatosCompetidor().getNombre();
        }
        return mensajeVictoriasCompetidor + "\n" + mensajeGanadorAbsoluto;
    }

    public void moverCompetidorLabel(CompetidorHilo competidor, int posAvanzada) {
        cVentana.moverCompetidor(competidores.indexOf(competidor), posAvanzada, distanciaCarrera);
    }

    public String accidenteCompetidor() {
        competidores.get(0).accidente(); //Si en vez de 0, va un random de 0 hasta el size del array, se hace al azar para todos :D
        return "El competidor 1  se accidento"; //tambien, con el random, esto es para el label que esta en el panel
    }

    public String impulsarCompetidor() {
        competidores.get(1).impulsar();
        return "El competidor 2 se impulso";
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

}
