package main;

import java.util.*;

public class Mesa {
	public Map<Integer, Franja> _franjas;
	public Integer _presidenteMesa;
	public Integer _numeroMesa = 7000;

	public Mesa(String nombreMesa, Integer presidenteMesa) {
		_presidenteMesa = presidenteMesa;

	}

	public Integer get_numeroMesa() {
		return _numeroMesa;
	}

	void asignarTurnoPresidente() {
		_franjas.get(8).agregarPersona(_presidenteMesa);
	}
	// asigna que mesa le toca a cada presidente y su respectivo turno

	public static class MesaMayores extends Mesa {
		// modela la mesa para las personas mayores de 65años, heredando los atributos
		// de la clase Mesa.
		public MesaMayores(Integer presidenteMesa) {
			// 8 <= franjas.keys <= 18
			super("Mayor65", presidenteMesa);
			_numeroMesa++;
			_franjas = new HashMap<>();
			asignarTurnoPresidente();
		}
	}

	public static class MesaEnfPreexistentes extends Mesa {
		// modela la mesa para las personas con enfermedades preexistentes, heredando
		// los atributos de la clase Mesa.
		public MesaEnfPreexistentes(Integer presidenteMesa) {
			// 8 <= franjas.keys <= 18
			super("Enf_Preex", presidenteMesa);
			_numeroMesa++;
			_franjas = new HashMap<>();
			asignarTurnoPresidente();

		}
	}

	public static class MesaTrabajadores extends Mesa {
		// modela la mesa para las personas que trabaja el dia de la votacion, heredando
		// los atributos de la clase Mesa.
		public MesaTrabajadores(Integer presidenteMesa) {
			// franjas.keySet == 1 (que va de 8 a 12)
			super("Trabajador", presidenteMesa);
			_numeroMesa++;
			_franjas = new HashMap<>();
			asignarTurnoPresidente();
		}
	}

	public static class MesaGeneral extends Mesa {
		// modela la mesa para las personas que no entran en ningun grupo, heredando los
		// atributos de la clase Mesa.
		public MesaGeneral(Integer presidenteMesa) {
			// 8 <= franjas.keys <= 18
			super("General", presidenteMesa);
			_numeroMesa++;
			_franjas = new HashMap<>();
			asignarTurnoPresidente();
		}
	}
}
