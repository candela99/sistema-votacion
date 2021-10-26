package main;

import java.util.*;

import com.sun.tools.sjavac.comp.dependencies.PublicApiCollector;

import main.Mesa.*;


public class SistemaDeTurnos {
	private String _nombre;
	private int _anotadosEnfPreex = 1, _anotadosMayores = 1, _anotadosGeneral = 1;
	private int _KeyEnfPreex = 8, _KeyMayores=8, _KeyGeneral = 8; 
	private int _CantTurnosAsignados;
	private Map<Integer, Persona> padron; // Almacena las personas que estan en el padron, Integer dni, Persona p
	private Map<Integer, Mesa> mesas; // Almacena las mesas. Key numeroDeMesa
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
			throw new RuntimeException("Solo los mayores de 16 años pueden registrarse como votantes");
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
		throw new RuntimeException("El tipo de mesa no es válido");
		/*
		 * if(tipoMesa.equals("Enf_Preex")) { Mesa mesa = new MesaEnfPreexistentes(dni);
		 * return mesa.get_numeroMesa(); } if(tipoMesa.equals("Mayor65")) { Mesa mesa2 =
		 * new MesaMayores(dni); return mesa2.get_numeroMesa(); }
		 */
	}
	public void aver() {
		System.out.println(mesas.keySet());
	}
	
	public Tupla<Integer, Integer> asignarTurno(int dni){
		/* Asigna turnos automáticamente a los votantes sin turno.
		* El sistema busca si hay alguna mesa y franja horaria factible en la que haya disponibilidad.
		* Devuelve la cantidad de turnos que pudo asignar.
		*/
		if(!tieneTurno.containsKey(dni) && padron.containsKey(dni)) {//revisar si no tiene turno
			Persona persona = padron.get(dni);
			//for (Mesa mesa1: mesas.values()) {}
				
			Iterator<Integer> it = mesas.keySet().iterator();
			while (it.hasNext()) {
				Integer keyInteger = it.next();
				Mesa mesa= mesas.get(keyInteger);	
				if(persona.get_EnfPreexistentes() && mesa instanceof MesaEnfPreexistentes) {
					if(this._anotadosEnfPreex==20) {
						this._KeyEnfPreex++;
					}
					mesa._franjas.get(this._KeyEnfPreex).agregarPersona(dni);
					this._anotadosEnfPreex++;
					this. _CantTurnosAsignados++;
					return new Tupla<Integer, Integer>(mesa.get_numeroMesa(), this._KeyEnfPreex);
				}
				if(persona.get_trabaja() &&  mesa instanceof MesaTrabajadores) {
					mesa._franjas.get(8).agregarPersona(dni);
					this. _CantTurnosAsignados++;
					return new Tupla<Integer, Integer>(mesa.get_numeroMesa(), 8);
				}
				if(persona.get_Edad()>65 && mesa instanceof MesaMayores) {
					if(this._anotadosMayores==10) {
						this._KeyMayores++;
					}
					mesa._franjas.get(this._KeyMayores).agregarPersona(dni);
					this._anotadosMayores++;
					this. _CantTurnosAsignados++;
					return new Tupla<Integer, Integer>(mesa.get_numeroMesa(), this._KeyMayores);
				}
				else if(persona.get_Edad()<65 && !persona.get_trabaja()&& !persona.get_EnfPreexistentes() && mesa instanceof MesaGeneral){
					if(this._anotadosGeneral==20) {
						this._KeyGeneral++;
					}
					mesa._franjas.get(this._KeyGeneral).agregarPersona(dni);
					this._anotadosGeneral++;
					this. _CantTurnosAsignados++;
					return new Tupla<Integer, Integer>(mesa.get_numeroMesa(), this._KeyGeneral);
				}
			}	
		}else {
			//return null provisorio
			return null;
			
		}
		//return null provisorio
		return null;
	}

	public void asignarTurnos() {
		Iterator<Integer> it = padron.keySet().iterator();
		while (it.hasNext()) {
			Integer keyInteger = it.next();
			Persona persona = padron.get(keyInteger);
			for (Mesa mesa: mesas.values()) {
				if(persona.get_EnfPreexistentes() && mesa instanceof MesaEnfPreexistentes && mesa.tieneTurnosDisponibles()) {
					if(this._anotadosEnfPreex==20) {
						this._KeyEnfPreex++;
					}
					mesa.getFranjas().get(this._KeyEnfPreex).agregarPersona(keyInteger);
					this._anotadosEnfPreex++;
				}
				if(persona.get_trabaja() &&  mesa instanceof MesaTrabajadores && mesa.tieneTurnosDisponibles()) {
					// franjas.keySet == 1 (que va de 8 a 12)
					mesa.getFranjas().get(1).agregarPersona(keyInteger);
				}
				if(persona.get_Edad()>65 && mesa instanceof MesaMayores && mesa.tieneTurnosDisponibles()) {
					if(this._anotadosMayores==10) {
						this._KeyMayores++;
					}
					mesa.getFranjas().get(this._KeyMayores).agregarPersona(keyInteger);
					this._anotadosMayores++;
				}
				if(persona.get_Edad()<65 && !persona.get_trabaja()&& !persona.get_EnfPreexistentes() && mesa instanceof MesaMayores && mesa.tieneTurnosDisponibles()){
					if(this._anotadosGeneral==20) {
						this._KeyGeneral++;
					}
					mesa.getFranjas().get(this._KeyGeneral).agregarPersona(keyInteger);
					this._anotadosGeneral++;
				}
			}
			
		}
		// revisa las condiciones de la persona y llama a su respectiva mesa para darle
		// el turno,controla la logica de si es mayor de 65 (inclusive) y tiene una
		// enfermedad preexistente, etc.
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
	
	/* Dado un número de mesa, devuelve una Map cuya clave es la franja horaria y
	* el valor es una lista con los DNI de los votantes asignados a esa franja.
	* Sin importar si se presentaron o no a votar.
	* - Si el número de mesa no es válido genera una excepción.
	* - Si no hay asignados devuelve null.
	*/
	//public Map<Integer,Franja> asignadosAMesa(int numMesa){
		
	//}

	//public int votantesConTurno(String tipoMesa) {
		/* Consulta el turno de un votante dado su DNI. Devuelve Mesa y franja horaria.
		 * - Si el DNI no pertenece a un votante genera una excepción.
		 * - Si el votante no tiene turno devuelve null.
		 */
	//}
	
	//public Tupla<Integer, Integer> consultaTurno(int dni){
		/* Dado un número de mesa, devuelve una Map cuya clave es la franja horaria y
		 * el valor es una lista con los DNI de los votantes asignados a esa franja.
		 * Sin importar si se presentaron o no a votar.
		 * - Si el número de mesa no es válido genera una excepción.
		 * - Si no hay asignados devuelve null.
		 */
	//}
	
	//public Map<Integer,List< Integer>> asignadosAMesa(int numMesa){
		/* Consultar la cantidad de votantes sin turno asignados a cada tipo de mesa.
		* Devuelve una Lista de Tuplas donde se vincula el tipo de mesa con la cantidad
		* de votantes sin turno que esperan ser asignados a ese tipo de mesa.
		* La lista no puede tener 2 elementos para el mismo tipo de mesa.
		*/
	//}
	
	//public List<Tupla<String, Integer>> sinTurnoSegunTipoMesa(){

	//}
	//metodos que ya estaban
	/**
	 public void asignarTurno(Persona p) { 
	 	// sobrecarga del metodo asignarTurno()
		// revisa las condiciones de la persona y llama a su respectiva mesa para darle
		// el turno,controla la logica de si es mayor de 65 (inclusive) y tiene una
		// enfermedad preexistente, etc.
	}
	
	public void registroDeVotantes() {
		// se encarga de almacenar los votantes, con su turno y si se presentó o no
	}

	public int votanteSinTurno(Mesa m) {
		// returna la cantidad de turnos restantes en una mesa
	}

	// consulta el turno para una persona
	public Boolean tieneTurno(Persona p) {
	}**/
}
