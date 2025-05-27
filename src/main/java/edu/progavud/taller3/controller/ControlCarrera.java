/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.progavud.taller3.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class ControlCarrera {

    private ArrayList<CompetidorHilo> competidores;
    private int meta;

    public ControlCarrera() {
        competidores = new ArrayList<>();
        meta = 100;//Cambiarlo desde ControlVentana :3   
        crearCompetidor("Usain Bolt");
        crearCompetidor("Periquin");//tambien desde cVentana
        iniciarCarrera();
        mostrarGanador();
    }

    public void crearCompetidor(String nombre) {
        CompetidorHilo competidorNuevo = new CompetidorHilo(this, nombre);
        competidores.add(competidorNuevo);
    }

    public void iniciarCarrera() {
        for (CompetidorHilo competidor : competidores) {
            competidor.start();
        }
        for(CompetidorHilo competidor: competidores) {
            try {
                competidor.join();
            } catch (InterruptedException ex) {
                System.out.println("Este hilo fue interrumpido soy el metodo IniciarCarrera()");
            }
        }
    }

    public void mostrarGanador() {
        ArrayList<Integer> indexGanadores = new ArrayList<>();
        long mejorTiempo = competidores.get(0).getDatosCompetidor().getTiempoDeLlegada();
        for (CompetidorHilo competidor : competidores) {
            if (competidor.getDatosCompetidor().getTiempoDeLlegada() < mejorTiempo) {
                indexGanadores.clear();
                indexGanadores.add(competidores.indexOf(competidor));
            }else if (competidor.getDatosCompetidor().getTiempoDeLlegada() == mejorTiempo) {
                indexGanadores.add(competidores.indexOf(competidor));
            }
        }
        if(indexGanadores.size() > 1) {
            String mensaje = "Hubo un empate entre:\n";
            for(int index: indexGanadores) {
                mensaje += competidores.get(index).getDatosCompetidor().getNombre() + "\n";
                competidores.get(index).getDatosCompetidor().ganar();
            }
            mensaje += "Con un tiempo de: " + competidores.get(indexGanadores.get(0)).getDatosCompetidor().getTiempoDeLlegada();
            System.out.println(mensaje);
        } else {
            String mensaje = "El ganador es: " + competidores.get(indexGanadores.get(0)).getDatosCompetidor().getNombre() 
                    +"\nCon un tiempo de: " + ((float) (competidores.get(indexGanadores.get(0)).getDatosCompetidor().getTiempoDeLlegada())/1000);
            System.out.println(mensaje);
            competidores.get(indexGanadores.get(0)).getDatosCompetidor().ganar();
        }
    }

    public int getMeta() {
        return meta;
    }

}
