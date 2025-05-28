package edu.progavud.taller3.controller;

import edu.progavud.taller3.view.PanelCarrera;
import edu.progavud.taller3.view.PanelFinal;
import edu.progavud.taller3.view.PanelInicio;
import edu.progavud.taller3.view.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

/**
 * @author Jorge Mendez
 * @author Julian Roldan
 * @author Jose Cucanchon
 * @version 1.0
 * 
 * Clase que se encarga de gestionar la logica 
 * realcionada con las ventanas y sus paneles
 */
public class ControlVentana implements ActionListener {

    ControlCarrera cCarrera;
    VentanaPrincipal ventanaPrincipal;
    /**
     * Constructor de la clase ControlVentana
     * @param cCarrera Recibe el parametro que permite la comunicacion con ControlCarrera
     */
    public ControlVentana(ControlCarrera cCarrera) {
        this.cCarrera = cCarrera;
        ventanaPrincipal = new VentanaPrincipal(this);
        ventanaPrincipal.cargarPanelInicio();
    }

    public void asignarOyentesPanelInicio() {
        ((PanelInicio) ventanaPrincipal.panelCanvas).btnIniciarCarrera.addActionListener(this);
    }

    public void asignarOyentesPanelCarrera() {
        ((PanelCarrera) ventanaPrincipal.panelCanvas).btnAccidente1.addActionListener(this);
        ((PanelCarrera) ventanaPrincipal.panelCanvas).btnImpulsar2.addActionListener(this);
        ((PanelCarrera) ventanaPrincipal.panelCanvas).btnSalir.addActionListener(this);
    }

    public void asignarOyentesPanelFinal() {
        ((PanelFinal) ventanaPrincipal.panelCanvas).btnNuevaCarrera.addActionListener(this);
        ((PanelFinal) ventanaPrincipal.panelCanvas).btnSalir.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (comando.equals("INICIARCARRERA")) {
            ventanaPrincipal.cargarPanelCarrera(cCarrera.getNumeroCompetidores());
            SwingUtilities.invokeLater(() -> {
                cCarrera.iniciarCarrera();
            });
        }
        if (comando.equals("ACCIDENTE")) {
            ((PanelCarrera) ventanaPrincipal.panelCanvas).lblInformacion.setText(cCarrera.accidenteCompetidor());
        }
        if (comando.equals("IMPULSAR")) {
            ((PanelCarrera) ventanaPrincipal.panelCanvas).lblInformacion.setText(cCarrera.impulsarCompetidor());
        }
        if(comando.equals("SALIR")){
           cCarrera.cerrarPrograma();
        }
    }
    /**
     * Método que valida la cantidad de competidores ingresados
     * a traves del metodo de ventantaPrincipal.
     * @return Devuelve el numero de competidores ingresados.
     */
    public int mostrarDigiteNumCompetidores() {
        try {
            int numCompetidores = Integer.parseInt(ventanaPrincipal.mostrarDigiteNumCompetidores());
            if (numCompetidores <= 4 && numCompetidores > 0) {
                return numCompetidores;
            } else {
                mostrarMensaje("Digito no valido");
                return mostrarDigiteNumCompetidores();
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Digito no valido");
            return mostrarDigiteNumCompetidores();
        }
    }
    /**
     * Método que valida los nombres ingresados
     * a traves del metodo de ventanaPrincipal
     * @return Devuelve el nombre del competidor ingresado
     */
    public String mostrarDigiteNomCompetidores() {
        String nombre = ventanaPrincipal.mostrarDigiteNomCompetidor();
        if (nombre == null || nombre.isBlank()) {
            return ventanaPrincipal.mostrarDigiteNomCompetidor();
        }
        return nombre;
    }
    /**
     * Método que inicializa la imagen de los competidores
     * a traves del metodo de PanelCarrera
     */
    public void crearImagenesCompetidores() {
        ((PanelCarrera) ventanaPrincipal.panelCanvas).crearImagenCompetidor("UsainBolt.gif");
        for (int i = 1; i < cCarrera.getNumeroCompetidores(); i++) {
            ((PanelCarrera) ventanaPrincipal.panelCanvas).crearImagenCompetidor("UdistritalinosoAeroplano.jpg");
        }
    }
    /**
     * Método que envia los parametros de informacion al método de PanelCarrera
     * @param indexCompetidor Recibe el identificador del competidor que se va a mover
     * @param cantidadAvanzada Recibe la cantidad de posiciones que se va a avanzar
     * @param distanciaCarrera Recibe la cantidad de distacia de la carrera
     */
    public void moverCompetidor(int indexCompetidor, int cantidadAvanzada, int distanciaCarrera) {
        ((PanelCarrera) ventanaPrincipal.panelCanvas).moverCompetidor(indexCompetidor, cantidadAvanzada, distanciaCarrera);
    }
    
    public void mostrarGanador(String mensaje) {
        ventanaPrincipal.cargarPanelFinal(mensaje);
    }
    
    public void mostrarMensaje(String mensaje) {
        ventanaPrincipal.mostrarMensajeJOptionPane(mensaje);
    }
}
