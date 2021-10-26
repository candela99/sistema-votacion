package main;

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
		padron = new HashMap<>();
		mesas = new HashMap<>();
		registroVotantes = new HashSet<>();
		tieneTurno = new HashMap<>();
	}

	public void registrarVotante(Integer dni, String nombre, Integer edad, Boolean enfPrevia, Boolean trabaja) {
		// se encarga de registrar las personas en el padron
		if (edad < 16) {
			throw new RuntimeException("Solo los mayores de 16 a�os pueden registrarse como votantes");
		} else {
			Persona p = new Persona(dni, nombre, edad, enfPrevia, trabaja);
			padron.put(dni, p);
		}
	}
	
	public Integer agregarMesa(String tipoMesa, Integer dni) {
		Mesa m = crearMesa(tipoMesa,dni);
		mesas.put(m.get_numeroMesa(), m);
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
			}if (tipoMesa.equals("Trabajador")) {
				Mesa m = new MesaTrabajadores(tipoMesa, dni);
				return m;
			}if (tipoMesa.equals("Mayor65")) {
				Mesa m = new MesaMayores(tipoMesa, dni);
				return m;
			}if (tipoMesa.equals("General")) {
				Mesa m = new MesaGeneral(tipoMesa, dni);
				return m;
			}
		}
		throw new RuntimeException("El tipo de mesa no es v�lido");
		/*
		 * if(tipoMesa.equals("Enf_Preex")) { Mesa mesa = new MesaEnfPreexistentes(dni);
		 * return mesa.get_numeroMesa(); } if(tipoMesa.equals("Mayor65")) { Mesa mesa2 =
		 * new MesaMayores(dni); return mesa2.get_numeroMesa(); }
		 */
	}
	
	
	
	
	
	public Tupla<Integer, Integer> asignarTurno(int dni){
		if(padron.containsKey(dni)) {
			return new Tupla<Integer, Integer>(2, 8);
			/**
			for (Mesa mesa: mesas) {
				if(padron.get(dni).get_EnfPreexistentes() && mesa instanceof MesaEnfPreexistentes) {
					this._CantTurnosAsignados++;
					return new Tupla<Integer, Integer>(mesa.get_numeroMesa(), 8);
				}
				if(padron.get(dni).get_trabaja() &&  mesa instanceof MesaTrabajadores) {
					this._CantTurnosAsignados++;
					return new Tupla<Integer, Integer>(mesa.get_numeroMesa(), 8);
				}
				if(padron.get(dni).get_Edad()>65 && mesa instanceof MesaMayores) {
					this._CantTurnosAsignados++;
					return new Tupla<Integer, Integer>(mesa.get_numeroMesa(), 8);
				}else {
					this._CantTurnosAsignados++;
					return new Tupla<Integer, Integer>(mesa.get_numeroMesa(), 8);
				}
			}**/
		}else {
			//throw new RuntimeException("Dni de votante no encontrado/registrado");
			return null;
		}
		/* Asigna turnos autom�ticamente a los votantes sin turno.
		* El sistema busca si hay alguna mesa y franja horaria factible en la que haya disponibilidad.
		* Devuelve la cantidad de turnos que pudo asignar.
		*/
	}
	/* Asigna turnos autom�ticamente a los votantes sin turno.
	* El sistema busca si hay alguna mesa y franja horaria factible en la que haya disponibilidad.
	* Devuelve la cantidad de turnos que pudo asignar.
	*/
	public Integer asignarTurnos() {
		for(Integer dni : padron.keySet()) {
			if(!tieneTurno.containsKey(dni)) {
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
	
	/* Dado un n�mero de mesa, devuelve una Map cuya clave es la franja horaria y
	* el valor es una lista con los DNI de los votantes asignados a esa franja.
	* Sin importar si se presentaron o no a votar.
	* - Si el n�mero de mesa no es v�lido genera una excepci�n.
	* - Si no hay asignados devuelve null.
	*/
	public Map<Integer,Franja> asignadosAMesa(Integer numMesa){
		if(!mesas.containsKey(numMesa)) {
			throw new RuntimeException("El numero de mesa no es v�lido");
		}
		if(mesas.get(numMesa).sinTurnosAsignados()) {
			return null;
		}
		return mesas.get(numMesa).getFranjas();
	}

	//public int votantesConTurno(String tipoMesa) {
		/** Cantidad de votantes con Turno asignados al tipo de mesa que se pide.
		 * - Permite conocer cu�ntos votantes se asignaron hasta el momento a alguno
		 * de los tipos de mesa que componen el sistema de votaci�n.
		 * - Si la clase de mesa solicitada no es v�lida debe generar una excepci�n
		 */
	//}
	public int votantesConTurno(String tipoMesa) {
		/*
		 * Cantidad de votantes con Turno asignados al tipo de mesa que se pide.
		 * - Permite conocer cu�ntos votantes se asignaron hasta el momento a alguno
		 * de los tipos de mesa que componen el sistema de votaci�n.
		 * - Si la clase de mesa solicitada no es v�lida debe generar una excepci�n
		 */
		for (Mesa mesa: mesas.values()) {
			if(mesa instanceof MesaEnfPreexistentes && tipoMesa.equals("Enf_Preex")) {
				return mesa._franjas.size()*20;
			}
			if(mesa instanceof MesaTrabajadores && tipoMesa.equals("Trabajador")) {
				return mesa._franjas.size()*20;
			}
			if(mesa instanceof MesaMayores && tipoMesa.equals("Mayor65")) {
				return mesa._franjas.size()*20;
			}
			if(mesa instanceof MesaGeneral && tipoMesa.equals("General")) {
				return mesa._franjas.size()*20;
			}
		}
		return _cantTurnosAsignados;
	}
	
	//public Tupla<Integer, Integer> consultaTurno(int dni){
		/** Consulta el turno de un votante dado su DNI. Devuelve Mesa y franja horaria.
		* - Si el DNI no pertenece a un votante genera una excepci�n.
		* - Si el votante no tiene turno devuelve null.
		**/
	//}
	
	public void addTieneTurno(Persona persona, Mesa mesa, Integer horario) {	
		if(tieneTurno.containsKey(persona.get_dni()) && padron.containsKey(persona.get_dni())) {
			tieneTurno.put(persona.get_dni(), new Turno(persona, horario, mesa));
		}
	}
	
	public Tupla<Integer, Integer> consultaTurno(int dni){
		
		/** Consulta el turno de un votante dado su DNI. Devuelve Mesa y franja horaria.
		 * - Si el DNI no pertenece a un votante genera una excepci�n.
		 * - Si el votante no tiene turno devuelve null. 
		 **/
		if(!padron.containsKey(dni)) {
			throw new RuntimeException("el DNI no pertenece a un votante registrado");
		}
		if(tieneTurno.containsKey(dni)) {// almacena los turnos y si se presento o no a votar
			return new Tupla<Integer, Integer>(tieneTurno.get(dni).get_m().get_numeroMesa(), tieneTurno.get(dni).get_horario()); 
		}else{
			return null;
		}

	}
	
	//public Map<Integer,List< Integer>> asignadosAMesa(int numMesa){
		/* Dado un n�mero de mesa, devuelve una Map cuya clave es la franja horaria y
		 * el valor es una lista con los DNI de los votantes asignados a esa franja.
		 * Sin importar si se presentaron o no a votar.
		 * - Si el n�mero de mesa no es v�lido genera una excepci�n.
		 * - Si no hay asignados devuelve null.
	//}
	
	//public List<Tupla<String, Integer>> sinTurnoSegunTipoMesa(){
		/** Consultar la cantidad de votantes sin turno asignados a cada tipo de mesa.
		 * Devuelve una Lista de Tuplas donde se vincula el tipo de mesa con la cantidad
		 * de votantes sin turno que esperan ser asignados a ese tipo de mesa.
		 * La lista no puede tener 2 elementos para el mismo tipo de mesa.
		 **/

	//}

	
}
