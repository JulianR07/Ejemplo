/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.progavud.taller3.model;

/**
 *
 * @author Miguel
 */
public class Competidor{
    private String nombre;
    private int posicion;
    private long tiempoDeLlegada;
    private int victorias;

    public Competidor(String nombre) {
        this.nombre = nombre;
        posicion = 0;
        tiempoDeLlegada = 0;
        victorias = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }
    
    public void mover(int distanciaMov) {
        posicion += distanciaMov;
    }
    
    public void reiniciarPosicion() {
        posicion = 0;
    }

    public long getTiempoDeLlegada() {
        return tiempoDeLlegada;
    }

    public void setTiempoDeLlegada(long tiempoDeLlegada) {
        this.tiempoDeLlegada = tiempoDeLlegada;
    }
    
    public void ganar() {
        victorias++;
    }
  
}
