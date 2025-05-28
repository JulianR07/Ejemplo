package edu.progavud.taller3.model;

/**
 * @author Jorge Mendez
 * @author Julian Roldan
 * @author Jose Cucanchon
 * @version 1.0
 * 
 * Clase que representa a un competidor del aplicativo
 */
public class Competidor {

    private String nombre;
    private int posicion;
    private long tiempoDeLlegada;
    private int victorias;

    /**
     * Constructor del Competidor, inicializa sus atributos
     * @param nombre Recibe el nombre ingresado por el usuario
     */
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
    /**
     * Actualiza la posicion actual del jugador
     * @param distanciaMov Recibe la cantidad de distancia recorrida
     */
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

    public int getVictorias() {
        return victorias;
    }

    public void ganar() {
        victorias++;
    }

}
