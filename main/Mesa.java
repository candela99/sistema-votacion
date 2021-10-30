package main;

import java.util.*;

abstract public class Mesa {
	protected Map<Integer, Franja> _franjas;
	protected Turno _turnoPresidente;
	protected Integer _presidenteMesa;
	protected String _nombreMesa;
	protected Integer _numeroMesa;
	protected Integer _cantVotantesPorMesa;

	public Mesa(String nombreMesa, Integer presidenteMesa) {
		_nombreMesa = nombreMesa;
		_presidenteMesa = presidenteMesa;
		_cantVotantesPorMesa = 0;
	}

	@Override
	public String toString() {
		return "Nombre de la mesa: " + _nombreMesa + " Presidente de mesa: " + _presidenteMesa;
	}

	public Integer votantesPorMesa() {
		for (Franja f : _franjas.values()) {
			_cantVotantesPorMesa += f.cantDePersonas();
		}
		return _cantVotantesPorMesa;
	}

	public Map<Integer, Franja> getFranjas() {
		return _franjas;
	}

	public Integer get_numeroMesa() {
		return _numeroMesa;
	}

	/*
	 * inicializa las franjas de cada mesa: agrega al map de franjas los horarios
	 * como key y su value asociado es una Franja compuesta por un Set de dni's (por
	 * el momento vacío) asignados a ese horario
	 */
	protected void inicializarFranjas(Integer cantFranjas) {
		Integer horario = 8;
		for (int i = 0; i < cantFranjas; i++) {
			_franjas.put(horario, new Franja());
			horario++;
		}
	}

	/*
	 * dado un dni, me fijo si la mesa que le corresponde segun sus condiciones
	 * tiene turnos disponibles
	 */
	public Turno agregarPersonaAFranja(Integer dni) {
		Boolean tieneTurnosDisponibles = buscarTurnoLibre().getX();
		Integer horario = buscarTurnoLibre().getY();
		if (tieneTurnosDisponibles) {
			_franjas.get(horario).agregarPersona(dni);
			Turno t = new Turno(dni, horario, this);
			return t;
		}
		return null;
	}

	public Turno getTurnoPresidente() {
		return _turnoPresidente;
	}

	public Boolean sinTurnosAsignados() {
		return _franjas.keySet().size() == 0;
	}

	/*
	 * devuelve true en el parametro x si hay turnos disponibles y el horario
	 * correspondiente, en caso contrario devuelve false y 0
	 */
	abstract Tupla<Boolean, Integer> buscarTurnoLibre();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_franjas == null) ? 0 : _franjas.hashCode());
		result = prime * result + ((_nombreMesa == null) ? 0 : _nombreMesa.hashCode());
		result = prime * result + ((_numeroMesa == null) ? 0 : _numeroMesa.hashCode());
		result = prime * result + ((_presidenteMesa == null) ? 0 : _presidenteMesa.hashCode());
		return result;
	}

	public static class MesaMayores extends Mesa {
		public MesaMayores(String nombreMesa, Integer presidenteMesa) {
			super(nombreMesa, presidenteMesa);
			_numeroMesa = 7000;
			_franjas = new HashMap<>();
			inicializarFranjas(10);
			_turnoPresidente = agregarPersonaAFranja(_presidenteMesa);
		}

		public Tupla<Boolean, Integer> buscarTurnoLibre() {
			for (Integer horario : _franjas.keySet()) {
				if (_franjas.get(horario).cantDePersonas() < 10) {
					return new Tupla<>(true, horario);
				}
			}
			return new Tupla<>(false, 0);
		}
	}

	public static class MesaEnfPreexistentes extends Mesa {
		public MesaEnfPreexistentes(String nombreMesa, Integer presidenteMesa) {
			super(nombreMesa, presidenteMesa);
			_numeroMesa = 7001;
			_franjas = new HashMap<>();
			inicializarFranjas(10);
			_turnoPresidente = agregarPersonaAFranja(_presidenteMesa);
		}

		public Tupla<Boolean, Integer> buscarTurnoLibre() {
			for (Integer horario : _franjas.keySet()) {
				if (_franjas.get(horario).cantDePersonas() < 20) {
					return new Tupla<>(true, horario);
				}
			}
			return new Tupla<>(false, 0);
		}
	}

	public static class MesaTrabajadores extends Mesa {
		public MesaTrabajadores(String nombreMesa, Integer presidenteMesa) {
			super(nombreMesa, presidenteMesa);
			_numeroMesa = 7002;
			_franjas = new HashMap<>();
			inicializarFranjas(1);
			_turnoPresidente = agregarPersonaAFranja(_presidenteMesa);
		}

		public Tupla<Boolean, Integer> buscarTurnoLibre() {
			return new Tupla<>(true, 8);
		}
	}

	public static class MesaGeneral extends Mesa {
		public MesaGeneral(String nombreMesa, Integer presidenteMesa) {
			super(nombreMesa, presidenteMesa);
			_numeroMesa = 7003;
			_franjas = new HashMap<>();
			inicializarFranjas(10);
			_turnoPresidente = agregarPersonaAFranja(_presidenteMesa);
		}

		public Tupla<Boolean, Integer> buscarTurnoLibre() {
			for (Integer horario : _franjas.keySet()) {
				if (_franjas.get(horario).cantDePersonas() < 30) {
					return new Tupla<>(true, horario);
				}
			}
			return new Tupla<>(false, 0);
		}
	}
}
