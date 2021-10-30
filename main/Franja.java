package main;

import java.util.*;

public class Franja { 
	private Set<Integer> _franja;

	public Franja() {
		_franja = new HashSet<>();
	}

	@Override
	public String toString() {
		StringBuilder personarPorFranja = new StringBuilder("Dnis asignados: ");
		for (Integer dni : _franja) {
			personarPorFranja.append(dni);
			personarPorFranja.append(' ');
		}
		return personarPorFranja.toString();
	}

	void agregarPersona(Integer dniPersona) {
		_franja.add(dniPersona);
	}

	public Set<Integer> getFranja() {
		return _franja;
	}

	public Integer cantDePersonas() {
		return _franja.size();
	}

}
