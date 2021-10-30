package main;

public class Tupla<X, Y> {
	final X x;
	final Y y;

	public Tupla(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Numero de mesa: " + x + " , Horario asignado: " + y;
	}

	public X getX() {
		return x;
	}

	public Y getY() {
		return y;
	}
}
