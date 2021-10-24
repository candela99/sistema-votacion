package main;

import java.util.*;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import main.Mesa.*;

public class SistemaDeTurnos {
	private String _nombre;
	private int _CantTurnosAsignados;
	private Map<Integer, Persona> padron; // Almacena las personas que estan en el padron, Integer dni, Persona p
	private Set<Mesa> mesas; // Almacena las mesas
	private Set<Integer> registroVotantes; // almacena los dni que ya votaron
	private Map<Turno, Boolean> registro; // almacena los turnos y si se presento o no a votar

	public SistemaDeTurnos(String nombreSistema) {
		_nombre = nombreSistema;
		padron = new HashMap<>();
		mesas = new HashSet<>();
		registroVotantes = new HashSet<>();
		registro = new HashMap<>();
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
		// registra la mesa segun su tipo junto con el dni del presidente y devuelve el
		// numero de mesa
		if (!padron.containsKey(dni)) {
			throw new RuntimeException("El presidente no se encuentra regitrado en el padron");
		} else {
			Mesa mesa = new Mesa(tipoMesa, dni);
			if (mesa instanceof MesaMayores || mesa instanceof MesaEnfPreexistentes || mesa instanceof MesaTrabajadores
					|| mesa instanceof MesaGeneral) {
				mesas.add(mesa);
				return mesa.get_numeroMesa();
			} else {
				throw new RuntimeException("El tipo de mesa no es válido");
			}
		}
		/*
		 * if(tipoMesa.equals("Enf_Preex")) { Mesa mesa = new MesaEnfPreexistentes(dni);
		 * return mesa.get_numeroMesa(); } if(tipoMesa.equals("Mayor65")) { Mesa mesa2 =
		 * new MesaMayores(dni); return mesa2.get_numeroMesa(); }
		 */
	}
	
	public Tupla<Integer, Integer> asignarTurno(int dni){
		if(padron.containsKey(dni)) {
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
			}
		}else {
			throw new RuntimeException("Dni de votante no encontrado/registrado");
		}
		/* Asigna turnos automáticamente a los votantes sin turno.
		* El sistema busca si hay alguna mesa y franja horaria factible en la que haya disponibilidad.
		* Devuelve la cantidad de turnos que pudo asignar.
		*/
		return null;
	}

	public void asignarTurno() {
		// revisa las condiciones de la persona y llama a su respectiva mesa para darle
		// el turno,controla la logica de si es mayor de 65 (inclusive) y tiene una
		// enfermedad preexistente, etc.
	}

	public void asignarTurno(Persona p) { // sobrecarga del metodo asignarTurno()
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

	public void asignarENfPreexistentes(Persona p) {
		// limita el cupo a 20 por franja horario (19 contando al presidente)
		// asigna el turno a las personas con enfermedades preexistentes
	}

	public void asignarTrabajadores(Persona p) {
		// pide el certificado de trabajo

	}

	public void asignarMayores(Persona p) {
		// limita el cupo a 10 por franja horario (9 contando al presidente)
		// asigna el turno a las personas mayores de 65
	}

	public void asignarMesaNormal(Persona p) {
		// limita el cupo a 30 por franja horario (29 contando al presidente)
	}

	public void registroDeVotantes() {
		// se encarga de almacenar los votantes, con su turno y si se presentó o no
	}

	public int votanteSinTurno(Mesa m) {
		// returna la cantidad de turnos restantes en una mesa
	}

	// consulta el turno para una persona
	public Boolean tieneTurno(Persona p) {
	}
}
