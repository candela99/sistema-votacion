package main;

public class Turno {
	private Mesa _m;
	private Persona _p;
	private Integer _horario;
	
	Turno(Persona p, Integer horario, Mesa m){
		_p = p;
		_horario = horario;
		_m = m;
	}

	public Mesa get_m() {
		return _m;
	}

	public Integer get_horario() {
		return _horario;
	}
}
