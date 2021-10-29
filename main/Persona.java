package main;

public class Persona {
	private	Integer _dni;
	private Integer _edad;
	private String _nombre;
	private Boolean _EnfPreexistentes, _trabaja;
	
	public Persona(Integer dni, String nombre, Integer edad, Boolean enfPreexistentes, Boolean trabaja) {
		this._dni = dni;
		this._nombre = nombre;
		this._edad = edad;
		this._EnfPreexistentes = enfPreexistentes;
		this._trabaja = trabaja;
	}
	
	public String toStringPersona() {
		return "Persona: -dni: " + _dni + " -nombre: " + _nombre + " -edad: " + _edad + " -Enfermedad Preexistente: " + _EnfPreexistentes + " -Trabaja: " + _trabaja; 
	}
	
	public Integer get_dni() {
		return _dni;
	}

	public String get_nombre() {
		return _nombre;
	}

	public Integer get_Edad() {
		return _edad;
	}

	public Boolean get_EnfPreexistentes() {
		return _EnfPreexistentes;
	}

	public Boolean esTrabajador() {
		return _trabaja;
	}
	
}
