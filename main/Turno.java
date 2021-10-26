package main;

public class Turno {
	private Mesa _mesa;
	private Persona _persona;
	private Integer _horario;
	
	Turno(Persona p, Integer horario, Mesa m){
		_persona = p;
		_horario = horario;
		_mesa = m;
	}
	public Mesa get_mesa() {
		return _mesa;
	}

	public Persona get_persona() {
		return _persona;
	}

	public Integer get_horario() {
		return _horario;
	}
	
}
