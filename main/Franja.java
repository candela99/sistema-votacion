package main;

import java.util.*;

public class Franja { //8hs, 9hs, 10hs...
	private Set<Integer> _franja;
	//modela un HashSet de personas en cada franja horario, mostrando cual persona va en cada una
	
	Franja(){
		_franja = new HashSet<>();
	}
	
	void agregarPersona(Integer dniPersona) {
		_franja.add(dniPersona);
	}
	
	Integer cantDePersonas() {
		return _franja.size();
	}
}
