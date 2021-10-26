package main;

import java.util.*;

abstract public class Mesa {
	public Map<Integer, Franja> _franjas;
	public Integer _presidenteMesa;
	public String _nombreMesa;
	public Integer _numeroMesa = 7000;

	Mesa(String nombreMesa, Integer presidenteMesa) {
		_nombreMesa = nombreMesa;
		_presidenteMesa = presidenteMesa;

	}

	public Integer get_numeroMesa() {
		return _numeroMesa;
	}
	
	abstract Boolean tieneTurnosDisponibles();

	void asignarTurnoPresidente() {
		_franjas.get(8).agregarPersona(_presidenteMesa);
	}
	// asigna que mesa le toca a cada presidente y su respectivo turno

	public static class MesaMayores extends Mesa {
		// modela la mesa para las personas mayores de 65años, heredando los atributos
		// de la clase Mesa.
		public MesaMayores(String nombreMesa, Integer presidenteMesa) {
			// 8 <= franjas.keys <= 18
			super(nombreMesa, presidenteMesa);
			_numeroMesa++;
			_franjas = new HashMap<>();
			//asignarTurnoPresidente();
		}
		public Boolean tieneTurnosDisponibles() {
			Boolean hayTurnos = false;
			if(_franjas.keySet().size() <= 10) {
				for(Franja f : _franjas.values()) {
					hayTurnos = hayTurnos || f.cantDePersonas() <= 10;
				}
			}
			return hayTurnos;
		}
	}

	public static class MesaEnfPreexistentes extends Mesa {
		// modela la mesa para las personas con enfermedades preexistentes, heredando
		// los atributos de la clase Mesa.
		public MesaEnfPreexistentes(String nombreMesa, Integer presidenteMesa) {
			// 8 <= franjas.keys <= 18
			super(nombreMesa, presidenteMesa);
			_numeroMesa++;
			_franjas = new HashMap<>();
			//asignarTurnoPresidente();
		}
		public Boolean tieneTurnosDisponibles() {
			Boolean hayTurnos = false;
			if(_franjas.keySet().size() <= 10) {
				for(Franja f : _franjas.values()) {
					hayTurnos = hayTurnos || f.cantDePersonas() <= 20;
				}
			}
			return hayTurnos;
		}
	}

	public static class MesaTrabajadores extends Mesa {
		// modela la mesa para las personas que trabaja el dia de la votacion, heredando
		// los atributos de la clase Mesa.
		public MesaTrabajadores(String nombreMesa, Integer presidenteMesa) {
			// franjas.keySet == 1 (que va de 8 a 12)
			super(nombreMesa, presidenteMesa);
			_numeroMesa++;
			_franjas = new HashMap<>();
			//asignarTurnoPresidente();
		}
		public Boolean tieneTurnosDisponibles() {
			return _franjas.keySet().size() <= 1;
		}
	}

	public static class MesaGeneral extends Mesa {
		// modela la mesa para las personas que no entran en ningun grupo, heredando los
		// atributos de la clase Mesa.
		public MesaGeneral(String nombreMesa, Integer presidenteMesa) {
			// 8 <= franjas.keys <= 18
			super(nombreMesa, presidenteMesa);
			_numeroMesa++;
			_franjas = new HashMap<>();
			//asignarTurnoPresidente();
		}
		public Boolean tieneTurnosDisponibles() {
			Boolean hayTurnos = false;
			if(_franjas.keySet().size() <= 10) {
				for(Franja f : _franjas.values()) {
					hayTurnos = hayTurnos || f.cantDePersonas() <= 30;
				}
			}
			return hayTurnos;
		}
	}
}
