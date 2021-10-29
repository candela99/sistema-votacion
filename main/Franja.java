package main;

import java.util.*;

public class Franja { //8hs, 9hs, 10hs...
	private Set<Integer> _franja;
	//modela un HashSet de personas en cada franja horario, mostrando cual persona va en cada una
	
	public Franja(){
		_franja = new HashSet<>();
	}
	
	public String toStringFranja() {
		String personarPorFranjaString = "";
		for (Integer dni : _franja) {
			personarPorFranjaString = personarPorFranjaString  + dni + " "; 
		}
		return "Franja: " + personarPorFranjaString;
	}
	
	void agregarPersona(Integer dniPersona) {
		_franja.add(dniPersona);
	}
	
	public Set<Integer> getFranja(){
		return _franja;
	}
	
	public Integer cantDePersonas() {
		return _franja.size();
	}
}
