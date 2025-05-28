package edu.progavud.taller3.controller;

import edu.progavud.taller3.model.Competidor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ControlCarreraTest {

    private ControlCarrera controlCarrera;

    @BeforeEach
    void setUp() throws Exception {
        controlCarrera = crearControlCarreraParaPruebas();
    }

    @Test
    @DisplayName("Debería definir correctamente un ganador único")
    void testDefinirGanadorUnico() throws Exception {
        ArrayList<Competidor> competidoresTest = new ArrayList<>();
        ArrayList<CompetidorHilo> competidoresHilosTest = new ArrayList<>();

        Competidor comp1 = new Competidor("Competidor1");
        comp1.setTiempoDeLlegada(1500L); // 1.5 segundos

        Competidor comp2 = new Competidor("Competidor2");
        comp2.setTiempoDeLlegada(1200L); // 1.2 segundos (ganador)

        Competidor comp3 = new Competidor("Competidor3");
        comp3.setTiempoDeLlegada(1800L); // 1.8 segundos

        competidoresTest.add(comp1);
        competidoresTest.add(comp2);
        competidoresTest.add(comp3);

        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp1));
        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp2));
        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp3));

        // Inyectar ambas listas usando reflection
        Field competidoresField = ControlCarrera.class.getDeclaredField("competidores");
        competidoresField.setAccessible(true);
        competidoresField.set(controlCarrera, competidoresTest);

        Field competidoresHilosField = ControlCarrera.class.getDeclaredField("competidoresHilos");
        competidoresHilosField.setAccessible(true);
        competidoresHilosField.set(controlCarrera, competidoresHilosTest);

        // Ejecutar el método definirGanador
        Method definirGanadorMethod = ControlCarrera.class.getDeclaredMethod("definirGanador");
        definirGanadorMethod.setAccessible(true);
        String resultado = (String) definirGanadorMethod.invoke(controlCarrera);

        // Verificaciones
        assertNotNull(resultado, "El resultado no debería ser null");
        assertTrue(resultado.contains("Competidor2"), "El ganador debería ser Competidor2");
        assertTrue(resultado.contains("1.2"), "Debería mostrar el tiempo correcto");
        assertFalse(resultado.contains("empate"), "No debería haber empate");

        // Verificar que el ganador tiene una victoria
        assertEquals(1, comp2.getVictorias(), "El ganador debería tener 1 victoria");
        assertEquals(0, comp1.getVictorias(), "Los perdedores no deberían tener victorias");
        assertEquals(0, comp3.getVictorias(), "Los perdedores no deberían tener victorias");
    }

    @Test
    @DisplayName("Debería manejar correctamente un empate entre competidores")
    void testDefinirGanadorConEmpate() throws Exception {
        ArrayList<Competidor> competidoresTest = new ArrayList<>();
        ArrayList<CompetidorHilo> competidoresHilosTest = new ArrayList<>();

        Competidor comp1 = new Competidor("CompetidorA");
        comp1.setTiempoDeLlegada(1000L);

        Competidor comp2 = new Competidor("CompetidorB");
        comp2.setTiempoDeLlegada(1000L); // Mismo tiempo = empate

        Competidor comp3 = new Competidor("CompetidorC");
        comp3.setTiempoDeLlegada(1500L);

        competidoresTest.add(comp1);
        competidoresTest.add(comp2);
        competidoresTest.add(comp3);

        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp1));
        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp2));
        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp3));

        // Inyectar usando reflection
        Field competidoresField = ControlCarrera.class.getDeclaredField("competidores");
        competidoresField.setAccessible(true);
        competidoresField.set(controlCarrera, competidoresTest);

        Field competidoresHilosField = ControlCarrera.class.getDeclaredField("competidoresHilos");
        competidoresHilosField.setAccessible(true);
        competidoresHilosField.set(controlCarrera, competidoresHilosTest);

        // Ejecutar el método
        Method definirGanadorMethod = ControlCarrera.class.getDeclaredMethod("definirGanador");
        definirGanadorMethod.setAccessible(true);
        String resultado = (String) definirGanadorMethod.invoke(controlCarrera);

        // Verificaciones
        assertNotNull(resultado, "El resultado no debería ser null");
        assertTrue(resultado.contains("empate"), "Debería detectar el empate");
        assertTrue(resultado.contains("CompetidorA"), "Debería incluir al primer ganador");
        assertTrue(resultado.contains("CompetidorB"), "Debería incluir al segundo ganador");
        assertFalse(resultado.contains("CompetidorC"), "No debería incluir al perdedor");
        assertTrue(resultado.contains("1000"), "Debería mostrar el tiempo de empate");

        // Verificar que ambos ganadores tienen victorias
        assertEquals(1, comp1.getVictorias(), "CompetidorA debería tener 1 victoria");
        assertEquals(1, comp2.getVictorias(), "CompetidorB debería tener 1 victoria");
        assertEquals(0, comp3.getVictorias(), "CompetidorC no debería tener victorias");
    }

    @Test
    @DisplayName("Debería definir correctamente el ganador absoluto")
    void testDefinirGanadorAbsoluto() throws Exception {
        ArrayList<Competidor> competidoresTest = new ArrayList<>();
        ArrayList<CompetidorHilo> competidoresHilosTest = new ArrayList<>();

        Competidor comp1 = new Competidor("Veterano");
        comp1.ganar(); // 1 victoria
        comp1.ganar(); // 2 victorias
        comp1.ganar(); // 3 victorias

        Competidor comp2 = new Competidor("Novato");
        comp2.ganar(); // 1 victoria

        Competidor comp3 = new Competidor("Principiante");
        // 0 victorias

        competidoresTest.add(comp1);
        competidoresTest.add(comp2);
        competidoresTest.add(comp3);

        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp1));
        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp2));
        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp3));

        // Inyectar usando reflection
        Field competidoresField = ControlCarrera.class.getDeclaredField("competidores");
        competidoresField.setAccessible(true);
        competidoresField.set(controlCarrera, competidoresTest);

        Field competidoresHilosField = ControlCarrera.class.getDeclaredField("competidoresHilos");
        competidoresHilosField.setAccessible(true);
        competidoresHilosField.set(controlCarrera, competidoresHilosTest);

        // Ejecutar el método
        Method definirGanadorAbsolutoMethod = ControlCarrera.class.getDeclaredMethod("definirGanadorAbsoluto");
        definirGanadorAbsolutoMethod.setAccessible(true);
        String resultado = (String) definirGanadorAbsolutoMethod.invoke(controlCarrera);

        // Verificaciones
        assertNotNull(resultado, "El resultado no debería ser null");
        assertTrue(resultado.contains("Veterano"), "Debería incluir al ganador absoluto");
        assertTrue(resultado.contains("Victorias: 3"), "Debería mostrar las victorias del veterano");
        assertTrue(resultado.contains("Victorias: 1"), "Debería mostrar las victorias del novato");
        assertTrue(resultado.contains("Victorias: 0"), "Debería mostrar las victorias del principiante");
        assertTrue(resultado.contains("ganador Absoluto es: Veterano"),
                "Debería declarar correctamente al ganador absoluto");
    }

    @Test
    @DisplayName("Debería manejar empate en ganador absoluto")
    void testDefinirGanadorAbsolutoConEmpate() throws Exception {
        ArrayList<Competidor> competidoresTest = new ArrayList<>();
        ArrayList<CompetidorHilo> competidoresHilosTest = new ArrayList<>();

        Competidor comp1 = new Competidor("EmpateA");
        comp1.ganar(); // 1 victoria
        comp1.ganar(); // 2 victorias

        Competidor comp2 = new Competidor("EmpateB");
        comp2.ganar(); // 1 victoria
        comp2.ganar(); // 2 victorias (empate)

        Competidor comp3 = new Competidor("Perdedor");
        comp3.ganar(); // 1 victoria solamente

        competidoresTest.add(comp1);
        competidoresTest.add(comp2);
        competidoresTest.add(comp3);

        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp1));
        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp2));
        competidoresHilosTest.add(new CompetidorHilo(controlCarrera, comp3));

        // Inyectar usando reflection
        Field competidoresField = ControlCarrera.class.getDeclaredField("competidores");
        competidoresField.setAccessible(true);
        competidoresField.set(controlCarrera, competidoresTest);

        Field competidoresHilosField = ControlCarrera.class.getDeclaredField("competidoresHilos");
        competidoresHilosField.setAccessible(true);
        competidoresHilosField.set(controlCarrera, competidoresHilosTest);

        // Ejecutar el método
        Method definirGanadorAbsolutoMethod = ControlCarrera.class.getDeclaredMethod("definirGanadorAbsoluto");
        definirGanadorAbsolutoMethod.setAccessible(true);
        String resultado = (String) definirGanadorAbsolutoMethod.invoke(controlCarrera);

        // Verificaciones
        assertNotNull(resultado, "El resultado no debería ser null");
        assertTrue(resultado.contains("empate"), "Debería detectar el empate");
        assertTrue(resultado.contains("EmpateA"), "Debería incluir al primer empatado");
        assertTrue(resultado.contains("EmpateB"), "Debería incluir al segundo empatado");
        assertTrue(resultado.contains("Victorias: 2"), "Debería mostrar las victorias correctas");
    }

    @Test
    @DisplayName("Debería retornar correctamente la distancia de carrera")
    void testGetDistanciaCarrera() {
        assertEquals(100, controlCarrera.getDistanciaCarrera(),
                "La distancia de carrera debería ser 100 por defecto");
    }

    @Test
    @DisplayName("Debería generar mensaje de accidente con formato correcto")
    void testAccidenteCompetidorFormato() throws Exception {
        ArrayList<Competidor> competidores = new ArrayList<>();
        competidores.add(new Competidor("Test1"));
        competidores.add(new Competidor("Test2"));
        competidores.add(new Competidor("Test3"));

        Field competidoresField = ControlCarrera.class.getDeclaredField("competidores");
        competidoresField.setAccessible(true);
        competidoresField.set(controlCarrera, competidores);

        ArrayList<CompetidorHilo> competidoresHilos = new ArrayList<>();
        for (Competidor comp : competidores) {
            competidoresHilos.add(new CompetidorHilo(controlCarrera, comp));
        }

        Field competidoresHilosField = ControlCarrera.class.getDeclaredField("competidoresHilos");
        competidoresHilosField.setAccessible(true);
        competidoresHilosField.set(controlCarrera, competidoresHilos);

        for (int i = 0; i < 10; i++) {
            String mensaje = controlCarrera.accidenteCompetidor();

            assertNotNull(mensaje, "El mensaje no debería ser null");
            assertTrue(mensaje.startsWith("El competidor "),
                    "El mensaje debería empezar con 'El competidor '");
            assertTrue(mensaje.endsWith(" se accidento"),
                    "El mensaje debería terminar con ' se accidento'");

            String[] partes = mensaje.split(" ");
            String numeroStr = partes[2]; // "El competidor X se accidento"
            int numero = Integer.parseInt(numeroStr);

            assertTrue(numero >= 1 && numero <= 3,
                    "El número del competidor debería estar entre 1 y 3");
        }
    }

    @Test
    @DisplayName("Debería generar mensaje de impulso con formato correcto")
    void testImpulsarCompetidorFormato() throws Exception {
        ArrayList<Competidor> competidores = new ArrayList<>();
        competidores.add(new Competidor("Test1"));
        competidores.add(new Competidor("Test2"));

        Field competidoresField = ControlCarrera.class.getDeclaredField("competidores");
        competidoresField.setAccessible(true);
        competidoresField.set(controlCarrera, competidores);

        ArrayList<CompetidorHilo> competidoresHilos = new ArrayList<>();
        for (Competidor comp : competidores) {
            competidoresHilos.add(new CompetidorHilo(controlCarrera, comp));
        }

        Field competidoresHilosField = ControlCarrera.class.getDeclaredField("competidoresHilos");
        competidoresHilosField.setAccessible(true);
        competidoresHilosField.set(controlCarrera, competidoresHilos);

        for (int i = 0; i < 10; i++) {
            String mensaje = controlCarrera.impulsarCompetidor();

            assertNotNull(mensaje, "El mensaje no debería ser null");
            assertTrue(mensaje.startsWith("El competidor: "),
                    "El mensaje debería empezar con 'El competidor: '");
            assertTrue(mensaje.endsWith(" se impulso"),
                    "El mensaje debería terminar con ' se impulso'");

            String[] partes = mensaje.split(" ");
            String numeroStr = partes[2]; // "El competidor: X se impulso"
            int numero = Integer.parseInt(numeroStr);

            assertTrue(numero >= 1 && numero <= 2,
                    "El número del competidor debería estar entre 1 y 2");
        }
    }

    private ControlCarrera crearControlCarreraParaPruebas() throws Exception {
        ControlCarrera control = new ControlCarrera() {
            {
                try {
                    Field competidoresHilosField = ControlCarrera.class.getDeclaredField("competidoresHilos");
                    competidoresHilosField.setAccessible(true);
                    competidoresHilosField.set(this, new ArrayList<CompetidorHilo>());

                    Field competidoresField = ControlCarrera.class.getDeclaredField("competidores");
                    competidoresField.setAccessible(true);
                    competidoresField.set(this, new ArrayList<Competidor>());

                    Field distanciaCarreraField = ControlCarrera.class.getDeclaredField("distanciaCarrera");
                    distanciaCarreraField.setAccessible(true);
                    distanciaCarreraField.set(this, 100);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void moverCompetidorLabel(CompetidorHilo competidor, int posAvanzada) {
                // No hacer nada en las pruebas
            }
        };

        return control;
    }
}
