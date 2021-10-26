package main;

import java.util.*;

abstract public class Mesa {
	protected Map<Integer, Franja> _franjas;
	protected Integer _presidenteMesa;
	protected String _nombreMesa;
	protected Integer _numeroMesa = 7000;

	Mesa(String nombreMesa, Integer presidenteMesa) {
		_nombreMesa = nombreMesa;
		_presidenteMesa = presidenteMesa;

	}
	public Map<Integer, Franja> getFranjas(){
		return _franjas;
	}
	
	public Integer get_numeroMesa() {
		return _numeroMesa;
	}
	
	protected void inicializarFranjas(Integer cantFranjas) {
		Integer horario = 8;
		for (int i = 0; i < cantFranjas; i++) {
			_franjas.put(horario, null);
			horario++;
		}
	}
	
	public void agregarPersonaAFranja(Integer dni) {
		_franjas.get(buscarTurnoLibre()).agregarPersona(dni);
	}
	
	
	
	abstract Boolean tieneTurnosDisponibles();

	void asignarTurnoPresidente() {
		_franjas.get(8).agregarPersona(_presidenteMesa);
	}
	
	public Boolean sinTurnosAsignados() {
		return _franjas.keySet().size() == 0;
	}
	
	abstract Integer buscarTurnoLibre();		//devuelve el horario con turnos libres
		
	
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
		// modela la mesa para las personas mayores de 65años, heredando los atributos
		// de la clase Mesa.
		public MesaMayores(String nombreMesa, Integer presidenteMesa) {
			// 8 <= franjas.keys <= 18
			super(nombreMesa, presidenteMesa);
			_numeroMesa++;
			_franjas = new HashMap<>();
			inicializarFranjas(10);
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
		
		public Integer buscarTurnoLibre() {		//devuelve el horario que tenga turnos disponibles
			for(Integer horario : _franjas.keySet()) {
				if(_franjas.get(horario).cantDePersonas() <= 10) {
					return horario;
				}
			}
			throw new RuntimeException("Esta mesa no tiene turnos disponibles");
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
			inicializarFranjas(10);
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
		
		public Integer buscarTurnoLibre() {
			for(Integer horario : _franjas.keySet()) {
				if(_franjas.get(horario).cantDePersonas() <= 20) {
					return horario;
				}
			}
			throw new RuntimeException("Esta mesa no tiene turnos disponibles");
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
			inicializarFranjas(1);
			//asignarTurnoPresidente();
		}
		public Boolean tieneTurnosDisponibles() {
			return _franjas.keySet().size() <= 1;
		}
		
		public Integer buscarTurnoLibre() {
			return 8;
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
			inicializarFranjas(10);
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
		
		public Integer buscarTurnoLibre() {
			for(Integer horario : _franjas.keySet()) {
				if(_franjas.get(horario).cantDePersonas() <= 30) {
					return horario;
				}
			}
			throw new RuntimeException("Esta mesa no tiene turnos disponibles");
		}
	}
}
