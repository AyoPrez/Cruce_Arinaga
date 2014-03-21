package com.rising.drawing;

public class NotaDeGracia {
	
	private Nota nota;
	private int compas;
	private int subcompas;
	
	//  Considerando las notas individuales y los acordes como elementos,
	//  este valor indica en qu� posici�n se encuentra como elemento
	private int posicion;
	
	public NotaDeGracia(Nota nota, int compas, int subcompas, int posicion) {
		this.nota = nota;
		this.compas = compas;
		this.subcompas = subcompas;
		this.posicion = posicion;
	}
	
	/**
	 * 
	 * @return Devuelve la nota de gracia
	 */
	public Nota nota() {
		return this.nota;
	}
	
	/**
	 * 
	 * @return Devuelve el comp�s en el que se encuentra la nota de gracia
	 */
	public int compas() {
		return this.compas;
	}
	
	/**
	 * 
	 * @return Devuelve el subcomp�s en el que se encuentra la nota de gracia
	 */
	public int subcompas() {
		return this.subcompas;
	}
	
	/**
	 * 
	 * @return Devuelve la posici�n de la nota de gracia
	 */
	public int posicion() {
		return this.posicion;
	}
}
