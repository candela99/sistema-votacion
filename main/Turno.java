package main;

public class Turno {
	private Mesa _mesa;
	private Integer _persona;
	private Integer _horario;

	Turno(Integer p, Integer horario, Mesa m) {
		_persona = p;
		_horario = horario;
		_mesa = m;
	}

	@Override
	public String toString() {
		return "Persona: " + _persona + ", mesa: " + _mesa._nombreMesa + ", horario: " + _horario;
	}

	public Mesa get_mesa() {
		return _mesa;
	}

	public Integer get_persona() {
		return _persona;
	}

	public Integer get_horario() {
		return _horario;
	}

}
