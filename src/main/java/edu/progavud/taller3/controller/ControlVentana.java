package edu.progavud.taller3.controller;

import edu.progavud.taller3.view.PanelCarrera;
import edu.progavud.taller3.view.PanelFinal;
import edu.progavud.taller3.view.PanelInicio;
import edu.progavud.taller3.view.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

/**
 *
 * @author Miguel
 */
public class ControlVentana implements ActionListener {

    ControlCarrera cCarrera;
    VentanaPrincipal ventanaPrincipal;

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
        if (comando.equals("ACCIDENTE1")) {
            ((PanelCarrera) ventanaPrincipal.panelCanvas).lblInformacion.setText(cCarrera.accidenteCompetidor());
        }
        if (comando.equals("IMPULSAR2")) {
            ((PanelCarrera) ventanaPrincipal.panelCanvas).lblInformacion.setText(cCarrera.impulsarCompetidor());
        }
        if(comando.equals("SALIR")){
           cCarrera.cerrarPrograma();
        }
    }

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

    public String mostrarDigiteNomCompetidores() {
        String nombre = ventanaPrincipal.mostrarDigiteNomCompetidor();
        if (nombre == null || nombre.isBlank()) {
            return ventanaPrincipal.mostrarDigiteNomCompetidor();
        }
        return nombre;
    }

    public void crearImagenesCompetidores() {
        ((PanelCarrera) ventanaPrincipal.panelCanvas).crearImagenCompetidor("Usain Bolt.png");
        for (int i = 1; i < cCarrera.getNumeroCompetidores(); i++) {
            ((PanelCarrera) ventanaPrincipal.panelCanvas).crearImagenCompetidor("Periquin.png");
        }
    }

    public void mostrarGanador(String mensaje) {
        ventanaPrincipal.cargarPanelFinal(mensaje);
    }

    public void moverCompetidor(int indexCompetidor, int cantidadAvanzada, int distanciaCarrera) {
        ((PanelCarrera) ventanaPrincipal.panelCanvas).moverCompetidor(indexCompetidor, cantidadAvanzada, distanciaCarrera);
    }

    public void mostrarMensaje(String mensaje) {
        ventanaPrincipal.mostrarMensajeJOptionPane(mensaje);
    }
}
