/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.progavud.taller3.controller;

import edu.progavud.taller3.model.Competidor;
import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class ControlCompetidor {

    private ControlCarrera cCarrera;
    private ArrayList<Competidor> competidores;
    private int numeroCompetidores;

    public ControlCompetidor(ControlCarrera cCarrera, int numeroCompetidores) {
        this.cCarrera = cCarrera;
        competidores = new ArrayList<>();
        this.numeroCompetidores = 1 + numeroCompetidores; //Siempre debe estar minimo usain bolt;
    }

    public void crearCompetidor(String nombre) {
        Competidor nuevoCompetidor = new Competidor(nombre);
        competidores.add(nuevoCompetidor);
    }

    /**
     * Método que valida cual fue el mejor tiempo y tambien gestiona si hubo un
     * empate
     *
     * @return Devuelve un mensaje con el ganador o ganadores y su tiempo
     * respectivo
     */
    public String definirGanador() {
        String mensaje = null;
        ArrayList<Integer> indexGanadores = new ArrayList<>();
        long mejorTiempo = competidores.get(0).getTiempoDeLlegada();
        for (Competidor competidor : competidores) {
            if (competidor.getTiempoDeLlegada() < mejorTiempo) {
                mejorTiempo = competidor.getTiempoDeLlegada();
                indexGanadores.clear();
                indexGanadores.add(competidores.indexOf(competidor));
            } else if (competidor.getTiempoDeLlegada() == mejorTiempo) {
                indexGanadores.add(competidores.indexOf(competidor));
            }
        }
        if (indexGanadores.size() > 1) {
            mensaje = "Hubo un empate entre:\n";
            for (int index : indexGanadores) {
                mensaje += competidores.get(index).getNombre() + "\n";
                competidores.get(index).ganar();
            }
            mensaje += "Con un tiempo de: " + competidores.get(indexGanadores.get(0)).getTiempoDeLlegada();
        } else {
            mensaje = "El ganador es: " + competidores.get(indexGanadores.get(0)).getNombre()
                    + "\nCon un tiempo de: " + ((float) (competidores.get(indexGanadores.get(0)).getTiempoDeLlegada()) / 1000);
            competidores.get(indexGanadores.get(0)).ganar();
        }
        return mensaje;
    }

    public String definirGanadorAbsoluto() {
        String mensajeVictoriasCompetidor = ""; //esta es la primera parte del mensaje 😄
        ArrayList<Integer> indexGanadores = new ArrayList<>();
        int mayorNumVictorias = 0;
        for (Competidor competidor : competidores) {
            mensajeVictoriasCompetidor += competidor.getNombre() + ", Victorias: " + competidor.getVictorias() + "\n";
            if (competidor.getVictorias() > mayorNumVictorias) {
                mayorNumVictorias = competidor.getVictorias();
                indexGanadores.clear();
                indexGanadores.add(competidores.indexOf(competidor));
            } else if (competidor.getVictorias() == mayorNumVictorias) {
                indexGanadores.add(competidores.indexOf(competidor));
            }
        }
        String mensajeGanadorAbsoluto = null;
        if (indexGanadores.size() > 1) {
            mensajeGanadorAbsoluto = "Hubo un empate entre:\n";
            for (int index : indexGanadores) {
                mensajeGanadorAbsoluto += competidores.get(index).getNombre() + "\n";
            }
        } else {
            mensajeGanadorAbsoluto = "El ganador Absoluto es: " + competidores.get(indexGanadores.get(0)).getNombre();
        }
        return mensajeVictoriasCompetidor + "\n" + mensajeGanadorAbsoluto;
    }

    public int getNumeroCompetidores() {
        return numeroCompetidores;
    }

    public Competidor getCompetidor(int indexCompetidor) {
        return competidores.get(indexCompetidor);
    }
}
