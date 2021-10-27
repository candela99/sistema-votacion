package main;

import java.awt.Container;
import java.util.*;
import main.Mesa.*;

public class SistemaDeTurnos {
	private String _nombre;
	private Integer _cantTurnosAsignados;
	private Map<Integer, Persona> padron; // Almacena las personas que estan en el padron, Integer dni, Persona p
	private Map<Integer, Mesa> mesas; // Almacena las mesas
	private Set<Integer> registroVotantes; // almacena los dni que ya votaron
	private Map<Integer, Turno> tieneTurno; // almacena los turnos y si se presento o no a votar

	public SistemaDeTurnos(String nombreSistema) {
		_nombre = nombreSistema;
		_cantTurnosAsignados = 0;
		padron = new HashMap<>();
		mesas = new HashMap<>();
		registroVotantes = new HashSet<>();
		tieneTurno = new HashMap<>();
	}

	public void registrarVotante(Integer dni, String nombre, Integer edad, Boolean enfPrevia, Boolean trabaja) {
		// se encarga de registrar las personas en el padron
		if (edad < 16) {
			throw new RuntimeException("Solo los mayores de 16 años pueden registrarse como votantes");
		} else {
			Persona p = new Persona(dni, nombre, edad, enfPrevia, trabaja);
			padron.put(dni, p);
		}
	}

	public Integer agregarMesa(String tipoMesa, Integer dni) {
		Mesa m = crearMesa(tipoMesa, dni);
		mesas.put(m.get_numeroMesa(), m);
		tieneTurno.put(dni, m.getTurnoPresidente());
		return m.get_numeroMesa();
	}

	public Mesa crearMesa(String tipoMesa, Integer dni) {
		// registra la mesa segun su tipo junto con el dni del presidente y devuelve el
		// numero de mesa
		if (!padron.containsKey(dni)) {
			throw new RuntimeException("El presidente no se encuentra regitrado en el padron");
		} else {
			if (tipoMesa.equals("Enf_Preex")) {
				Mesa m = new MesaEnfPreexistentes(tipoMesa, dni);
				return m;
			}
			if (tipoMesa.equals("Trabajador")) {
				Mesa m = new MesaTrabajadores(tipoMesa, dni);
				return m;
			}
			if (tipoMesa.equals("Mayor65")) {
				Mesa m = new MesaMayores(tipoMesa, dni);
				return m;
			}
			if (tipoMesa.equals("General")) {
				Mesa m = new MesaGeneral(tipoMesa, dni);
				return m;
			}
		}
		throw new RuntimeException("El tipo de mesa no es válido");
		/*
		 * if(tipoMesa.equals("Enf_Preex")) { Mesa mesa = new MesaEnfPreexistentes(dni);
		 * return mesa.get_numeroMesa(); } if(tipoMesa.equals("Mayor65")) { Mesa mesa2 =
		 * new MesaMayores(dni); return mesa2.get_numeroMesa(); }
		 */
	}

	/*
	 * Asigna un turno a un votante determinado. - Si el DNI no pertenece a un
	 * votante registrado debe generar una excepción.
	 * 
	 * - Si el votante ya tiene turno asignado se devuelve el turno como: Número de
	 * Mesa y Franja Horaria.
	 * 
	 * - Si aún no tiene turno asignado se busca una franja horaria disponible en
	 * una mesa del tipo correspondiente al votante y se devuelve el turno asignado,
	 * como Número de Mesa y Franja Horaria.
	 * 
	 * - Si no hay mesas con horarios disponibles no modifica nada y devuelve null.
	 * (Se supone que el turno permitirá conocer la mesa y la franja horaria
	 * asignada)
	 */

	// me serviria un Mesa asignarMesa() para limpiar el codigo. PROBAR
	public Tupla<Integer, Integer> asignarTurno(Integer dni) {
		if (!padron.containsKey(dni)) {
			throw new RuntimeException("El dni no se encuentra registrado en el padron");
		}
		if (tieneTurno.containsKey(dni)) {
			Turno t = tieneTurno.get(dni); // retorna el Turno de la persona
			return new Tupla<Integer, Integer>(t.get_mesa().get_numeroMesa(), t.get_horario());
		} else {
			Persona votante = padron.get(dni);
			if (votante.esTrabajador()) {
				Turno t = mesas.get(mesaTrabajadores()).agregarPersonaAFranja(dni);
				tieneTurno.put(dni, t);
				return new Tupla<Integer, Integer>(mesaTrabajadores(), t.get_horario());
			}
			if (votante.get_EnfPreexistentes() && votante.get_Edad() >= 65) {
				if (mesas.get(mesaEnfPreexistentes()).tieneTurnosDisponibles()) {
					Turno t = mesas.get(mesaEnfPreexistentes()).agregarPersonaAFranja(dni);
					tieneTurno.put(dni, t);
					return new Tupla<Integer, Integer>(mesaEnfPreexistentes(), t.get_horario());
				} else {
					Turno t = mesas.get(mesaMayores()).agregarPersonaAFranja(dni);
					tieneTurno.put(dni, t);
					return new Tupla<Integer, Integer>(mesaMayores(), t.get_horario());
				}
			}
			if (votante.get_EnfPreexistentes()) {
				Turno t = mesas.get(mesaEnfPreexistentes()).agregarPersonaAFranja(dni);
				tieneTurno.put(dni, t);
				return new Tupla<Integer, Integer>(mesaEnfPreexistentes(), t.get_horario());
			}
			if (votante.get_Edad() >= 65) {
				Turno t = mesas.get(mesaMayores()).agregarPersonaAFranja(dni);
				tieneTurno.put(dni, t);
				return new Tupla<Integer, Integer>(mesaMayores(), t.get_horario());
			}
			if (!votante.get_EnfPreexistentes() && votante.get_Edad() < 65 && !votante.get_EnfPreexistentes()
					&& !votante.esTrabajador()) {
				Turno t = mesas.get(mesaGeneral()).agregarPersonaAFranja(dni);
				tieneTurno.put(dni, t);
				return new Tupla<Integer, Integer>(mesaGeneral(), t.get_horario());
			}
		}
		return null;
	}

	/*
	 * Asigna turnos automáticamente a los votantes sin turno. El sistema busca si
	 * hay alguna mesa y franja horaria factible en la que haya disponibilidad.
	 * Devuelve la cantidad de turnos que pudo asignar.
	 */
	public Integer asignarTurnos() {
		for (Integer dni : padron.keySet()) {
			if (!tieneTurno.containsKey(dni)) {
				asignarTurno(dni);
				_cantTurnosAsignados++;
			}
		}
		return _cantTurnosAsignados;
	}

	public Boolean votar(Integer dni) {
		if (!padron.containsKey(dni)) {
			throw new RuntimeException("Votante no registrado en el padron");
		} else {
			if (registroVotantes.contains(dni)) {
				return false;
			} else {
				registroVotantes.add(dni);
				return true;
			}
		}
	}

	/*
	 * Dado un número de mesa, devuelve una Map cuya clave es la franja horaria y el
	 * valor es una lista con los DNI de los votantes asignados a esa franja. Sin
	 * importar si se presentaron o no a votar. - Si el número de mesa no es válido
	 * genera una excepción. - Si no hay asignados devuelve null.
	 */
	public Map<Integer, Franja> asignadosAMesa(Integer numMesa) {
		if (!mesas.containsKey(numMesa)) {
			throw new RuntimeException("El numero de mesa no es válido");
		}
		if (mesas.get(numMesa).sinTurnosAsignados()) {
			return null;
		}
		return mesas.get(numMesa).getFranjas();
	}

	// public int votantesConTurno(String tipoMesa) {
	/**
	 * Cantidad de votantes con Turno asignados al tipo de mesa que se pide. -
	 * Permite conocer cuántos votantes se asignaron hasta el momento a alguno de
	 * los tipos de mesa que componen el sistema de votación. - Si la clase de mesa
	 * solicitada no es válida debe generar una excepción
	 */
	// }
	public int votantesConTurno(String tipoMesa) {
		/*
		 * Cantidad de votantes con Turno asignados al tipo de mesa que se pide. -
		 * Permite conocer cuántos votantes se asignaron hasta el momento a alguno de
		 * los tipos de mesa que componen el sistema de votación. - Si la clase de mesa
		 * solicitada no es válida debe generar una excepción
		 */
		int cont = 0;
		for (Mesa mesa : mesas.values()) {
			if (mesa instanceof MesaEnfPreexistentes && tipoMesa.equals("Enf_Preex")) {
				for (Integer horaFranja : mesa.getFranjas().keySet()) {
					cont += mesa.getFranjas().get(horaFranja).getFranja().size();
				}
				return cont += mesa.getFranjas().size();
			}
			if (mesa instanceof MesaTrabajadores && tipoMesa.equals("Trabajador")) {
				for (Integer horaFranja : mesa.getFranjas().keySet()) {
					cont += mesa.getFranjas().get(horaFranja).getFranja().size();
				}
				return cont += mesa.getFranjas().size();
			}
			if (mesa instanceof MesaMayores && tipoMesa.equals("Mayor65")) {
				for (Integer horaFranja : mesa.getFranjas().keySet()) {
					cont += mesa.getFranjas().get(horaFranja).getFranja().size();
				}
				return cont += mesa.getFranjas().size();
			}
			if (mesa instanceof MesaGeneral && tipoMesa.equals("General")) {
				for (Integer horaFranja : mesa.getFranjas().keySet()) {
					cont += mesa.getFranjas().get(horaFranja).getFranja().size();
				}
				return cont += mesa.getFranjas().size();
			}
		}
		return 0;
	}

	// public Tupla<Integer, Integer> consultaTurno(int dni){
	/**
	 * Consulta el turno de un votante dado su DNI. Devuelve Mesa y franja horaria.
	 * - Si el DNI no pertenece a un votante genera una excepción. - Si el votante
	 * no tiene turno devuelve null.
	 **/
	// }

	public void addTieneTurno(Persona persona, Mesa mesa, Integer horario) {
		if (tieneTurno.containsKey(persona.get_dni()) && padron.containsKey(persona.get_dni())) {
			tieneTurno.put(persona.get_dni(), new Turno(persona.get_dni(), horario, mesa));
		}
	}

	public Tupla<Integer, Integer> consultaTurno(int dni) {

		/**
		 * Consulta el turno de un votante dado su DNI. Devuelve Mesa y franja horaria.
		 * - Si el DNI no pertenece a un votante genera una excepción. - Si el votante
		 * no tiene turno devuelve null.
		 **/
		if (!padron.containsKey(dni)) {
			throw new RuntimeException("el DNI no pertenece a un votante registrado");
		}
		if (tieneTurno.containsKey(dni)) {// almacena los turnos y si se presento o no a votar
			return new Tupla<Integer, Integer>(tieneTurno.get(dni).get_mesa().get_numeroMesa(),
					tieneTurno.get(dni).get_horario());
		} else {
			return null;
		}

	}

	// public Map<Integer,List< Integer>> asignadosAMesa(int numMesa){
	/*
	 * Dado un número de mesa, devuelve una Map cuya clave es la franja horaria y el
	 * valor es una lista con los DNI de los votantes asignados a esa franja. Sin
	 * importar si se presentaron o no a votar. - Si el número de mesa no es válido
	 * genera una excepción. - Si no hay asignados devuelve null. //}
	 * 
	 * //public List<Tupla<String, Integer>> sinTurnoSegunTipoMesa(){ /** Consultar
	 * la cantidad de votantes sin turno asignados a cada tipo de mesa. Devuelve una
	 * Lista de Tuplas donde se vincula el tipo de mesa con la cantidad de votantes
	 * sin turno que esperan ser asignados a ese tipo de mesa. La lista no puede
	 * tener 2 elementos para el mismo tipo de mesa.
	 **/

	// }

	public Integer mesaTrabajadores() {
		for (Integer numeroMesa : mesas.keySet()) {
			if (mesas.get(numeroMesa) instanceof MesaTrabajadores) {
				return numeroMesa;
			}
		}
		return null;
	}

	public Integer mesaEnfPreexistentes() {
		for (Integer numeroMesa : mesas.keySet()) {
			if (mesas.get(numeroMesa) instanceof MesaEnfPreexistentes) {
				return numeroMesa;
			}
		}
		return null;
	}

	public Integer mesaGeneral() {
		for (Integer numeroMesa : mesas.keySet()) {
			if (mesas.get(numeroMesa) instanceof MesaGeneral) {
				return numeroMesa;
			}
		}
		return null;
	}

	public Integer mesaMayores() {
		for (Integer numeroMesa : mesas.keySet()) {
			if (mesas.get(numeroMesa) instanceof MesaMayores) {
				return numeroMesa;
			}
		}
		return null;
	}

}
