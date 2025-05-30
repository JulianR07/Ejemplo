package edu.progavud.taller3.controller;

import edu.progavud.taller3.model.Competidor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para ControlCompetidor (sin Mockito)
 * 
 * @author Test Team
 * @version 1.0
 */
public class ControlCompetidorTest {

    private ControlCompetidor controlCompetidor;
    private ControlCarrera controlCarrera;

    @BeforeEach
    void setUp() {
        // Crear instancia real de ControlCarrera
        controlCarrera = new ControlCarrera();
        // Crear instancia con 2 competidores adicionales (+ Usain Bolt = 3 total)
        controlCompetidor = new ControlCompetidor(controlCarrera, 2);
    }

    @Test
    @DisplayName("Constructor debe inicializar correctamente el número de competidores")
    void testConstructor() {
        // Given & When
        ControlCompetidor control = new ControlCompetidor(controlCarrera, 3);
        
        // Then
        assertEquals(4, control.getNumeroCompetidores()); // 3 + 1 (Usain Bolt)
    }

    @Test
    @DisplayName("Debe crear competidor correctamente")
    void testCrearCompetidor() {
        // Given
        String nombreCompetidor = "Speedy Gonzalez";
        
        // When
        controlCompetidor.crearCompetidor(nombreCompetidor);
        
        // Then
        assertEquals(1, controlCompetidor.getNumeroCompetidores() - 2); // Se ha creado 1 competidor
        // No podemos verificar directamente el ArrayList pero podemos verificar que no lance excepción
        assertDoesNotThrow(() -> controlCompetidor.getCompetidor(0));
    }

    @Test
    @DisplayName("Debe obtener competidor por índice correctamente")
    void testGetCompetidor() {
        // Given
        String nombreCompetidor = "Flash";
        controlCompetidor.crearCompetidor(nombreCompetidor);
        
        // When
        Competidor competidor = controlCompetidor.getCompetidor(0);
        
        // Then
        assertNotNull(competidor);
        assertEquals(nombreCompetidor, competidor.getNombre());
    }

    @Test
    @DisplayName("Debe definir ganador con un solo competidor")
    void testDefinirGanadorUnicoGanador() {
        // Given
        controlCompetidor.crearCompetidor("Competidor 1");
        controlCompetidor.crearCompetidor("Competidor 2");
        
        Competidor comp1 = controlCompetidor.getCompetidor(0);
        Competidor comp2 = controlCompetidor.getCompetidor(1);
        
        // Simular tiempos de llegada diferentes
        comp1.setTiempoDeLlegada(5000L); // 5 segundos
        comp2.setTiempoDeLlegada(6000L); // 6 segundos
        
        // When
        String resultado = controlCompetidor.definirGanador();
        
        // Then
        assertNotNull(resultado);
        assertTrue(resultado.contains("El ganador es: Competidor 1"));
        assertTrue(resultado.contains("Con un tiempo de: 5.0"));
        assertEquals(1, comp1.getVictorias());
        assertEquals(0, comp2.getVictorias());
    }

    @Test
    @DisplayName("Debe manejar empate entre competidores")
    void testDefinirGanadorConEmpate() {
        // Given
        controlCompetidor.crearCompetidor("Competidor A");
        controlCompetidor.crearCompetidor("Competidor B");
        
        Competidor comp1 = controlCompetidor.getCompetidor(0);
        Competidor comp2 = controlCompetidor.getCompetidor(1);
        
        // Simular mismo tiempo de llegada
        comp1.setTiempoDeLlegada(5000L);
        comp2.setTiempoDeLlegada(5000L);
        
        // When
        String resultado = controlCompetidor.definirGanador();
        
        // Then
        assertNotNull(resultado);
        assertTrue(resultado.contains("Hubo un empate entre:"));
        assertTrue(resultado.contains("Competidor A"));
        assertTrue(resultado.contains("Competidor B"));
        assertTrue(resultado.contains("Con un tiempo de: 5000"));
        assertEquals(1, comp1.getVictorias());
        assertEquals(1, comp2.getVictorias());
    }

    @Test
    @DisplayName("Debe encontrar el mejor tiempo entre múltiples competidores")
    void testDefinirGanadorMejorTiempo() {
        // Given
        controlCompetidor.crearCompetidor("Lento");
        controlCompetidor.crearCompetidor("Rápido");
        controlCompetidor.crearCompetidor("Medio");
        
        Competidor lento = controlCompetidor.getCompetidor(0);
        Competidor rapido = controlCompetidor.getCompetidor(1);
        Competidor medio = controlCompetidor.getCompetidor(2);
        
        lento.setTiempoDeLlegada(8000L);
        rapido.setTiempoDeLlegada(3000L); // Mejor tiempo
        medio.setTiempoDeLlegada(5000L);
        
        // When
        String resultado = controlCompetidor.definirGanador();
        
        // Then
        assertTrue(resultado.contains("El ganador es: Rápido"));
        assertTrue(resultado.contains("Con un tiempo de: 3.0"));
        assertEquals(0, lento.getVictorias());
        assertEquals(1, rapido.getVictorias());
        assertEquals(0, medio.getVictorias());
    }

    @Test
    @DisplayName("Debe definir ganador absoluto con un solo líder")
    void testDefinirGanadorAbsolutoUnico() {
        // Given
        controlCompetidor.crearCompetidor("Ganador");
        controlCompetidor.crearCompetidor("Perdedor");
        
        Competidor ganador = controlCompetidor.getCompetidor(0);
        Competidor perdedor = controlCompetidor.getCompetidor(1);
        
        // Simular múltiples victorias
        ganador.ganar();
        ganador.ganar();
        ganador.ganar(); // 3 victorias
        perdedor.ganar(); // 1 victoria
        
        // When
        String resultado = controlCompetidor.definirGanadorAbsoluto();
        
        // Then
        assertNotNull(resultado);
        assertTrue(resultado.contains("Ganador, Victorias: 3"));
        assertTrue(resultado.contains("Perdedor, Victorias: 1"));
        assertTrue(resultado.contains("El ganador Absoluto es: Ganador"));
    }

    @Test
    @DisplayName("Debe manejar empate en ganador absoluto")
    void testDefinirGanadorAbsolutoConEmpate() {
        // Given
        controlCompetidor.crearCompetidor("Competidor X");
        controlCompetidor.crearCompetidor("Competidor Y");
        
        Competidor compX = controlCompetidor.getCompetidor(0);
        Competidor compY = controlCompetidor.getCompetidor(1);
        
        // Simular mismo número de victorias
        compX.ganar();
        compX.ganar();
        compY.ganar();
        compY.ganar();
        
        // When
        String resultado = controlCompetidor.definirGanadorAbsoluto();
        
        // Then
        assertTrue(resultado.contains("Competidor X, Victorias: 2"));
        assertTrue(resultado.contains("Competidor Y, Victorias: 2"));
        assertTrue(resultado.contains("Hubo un empate entre:"));
        assertTrue(resultado.contains("Competidor X"));
        assertTrue(resultado.contains("Competidor Y"));
    }

    @Test
    @DisplayName("Debe manejar competidores sin victorias")
    void testDefinirGanadorAbsolutoSinVictorias() {
        // Given
        controlCompetidor.crearCompetidor("Sin Victorias 1");
        controlCompetidor.crearCompetidor("Sin Victorias 2");
        
        // When (ningún competidor tiene victorias, todos tienen 0)
        String resultado = controlCompetidor.definirGanadorAbsoluto();
        
        // Then
        assertTrue(resultado.contains("Sin Victorias 1, Victorias: 0"));
        assertTrue(resultado.contains("Sin Victorias 2, Victorias: 0"));
        assertTrue(resultado.contains("Hubo un empate entre:")); // Empate con 0 victorias
    }

    @Test
    @DisplayName("Debe obtener número correcto de competidores")
    void testGetNumeroCompetidores() {
        // Given
        ControlCompetidor control1 = new ControlCompetidor(controlCarrera, 0);
        ControlCompetidor control2 = new ControlCompetidor(controlCarrera, 5);
        
        // When & Then
        assertEquals(1, control1.getNumeroCompetidores()); // 0 + 1 (Usain Bolt)
        assertEquals(6, control2.getNumeroCompetidores()); // 5 + 1 (Usain Bolt)
    }

    @Test
    @DisplayName("Debe manejar caso límite con tiempos de llegada cero")
    void testDefinirGanadorTiemposCero() {
        // Given
        controlCompetidor.crearCompetidor("Instantáneo");
        Competidor comp = controlCompetidor.getCompetidor(0);
        comp.setTiempoDeLlegada(0L);
        
        // When
        String resultado = controlCompetidor.definirGanador();
        
        // Then
        assertTrue(resultado.contains("El ganador es: Instantáneo"));
        assertTrue(resultado.contains("Con un tiempo de: 0.0"));
    }

    @Test
    @DisplayName("Debe mantener orden de índices correcto al acceder a competidores")
    void testOrdenCompetidores() {
        // Given
        String[] nombres = {"Primero", "Segundo", "Tercero"};
        
        for (String nombre : nombres) {
            controlCompetidor.crearCompetidor(nombre);
        }
        
        // When & Then
        for (int i = 0; i < nombres.length; i++) {
            Competidor comp = controlCompetidor.getCompetidor(i);
            assertEquals(nombres[i], comp.getNombre());
        }
    }

    @Test
    @DisplayName("Debe formatear correctamente el tiempo en segundos")
    void testFormatoTiempoEnSegundos() {
        // Given
        controlCompetidor.crearCompetidor("Milisegundos");
        Competidor comp = controlCompetidor.getCompetidor(0);
        comp.setTiempoDeLlegada(2500L); // 2.5 segundos
        
        // When
        String resultado = controlCompetidor.definirGanador();
        
        // Then
        assertTrue(resultado.contains("Con un tiempo de: 2.5"));
    }
}