package testsDominio;

import org.junit.Test;

import dominio.*;
import org.junit.Assert;

public class TestAsignarPuntos {

	@Test
	public void testAumentarSalud_tope() {
		Personaje.cargarTablaNivel();

		Humano h = new Humano("Nicolas", new Guerrero(), 1);
		Assert.assertTrue(h.getSaludTope() == 105);
		h.ganarExperiencia(50);
		Assert.assertTrue(h.getSaludTope() == 130);
	}

	@Test
	public void testAumentarEnergia_tope() {
		Personaje.cargarTablaNivel();

		Humano h = new Humano("Nicolas", new Guerrero(), 1);
		Assert.assertTrue(h.getEnergiaTope() == 105);
		h.ganarExperiencia(50);
		Assert.assertTrue(h.getEnergiaTope() == 125);
	}

	@Test
	public void testMasDe200Puntos() {
		Humano h = new Humano("Nicolas", 100, 100, 199, 199, 199, new Guerrero(), 0, 1, 1);
		h.asignarPuntosSkills(2, 2, 2);
		Assert.assertTrue(h.getFuerza() == 199);
		Assert.assertTrue(h.getDestreza() == 199);
		Assert.assertTrue(h.getInteligencia() == 199);
	}
}
