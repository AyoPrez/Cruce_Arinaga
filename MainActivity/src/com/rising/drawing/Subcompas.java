package com.rising.drawing;

public class Subcompas {

	//  Atributos
	private Nota[] notas;
	private int numNotas;
	
	//  Constructor
	public Subcompas() {
		this.numNotas = 0;
		this.notas = new Nota[50];
	}

	/**
	 *   A�adir una nota al subcomp�s
	 * @param nota
	 * Valor de la nota a�adida al subcomp�s
	 */
	public void nuevaNota(Nota nota) {
		this.notas[numNotas++] = nota;
	}
	
	/**
	 *   Inserta (nota) en la posici�n indicada por (indice)
	 * @param indice Posici�n en la que queremos que se inserte la nota
	 * @param nota Nota a insertar
	 */
	public void nuevaNotaEnIndice(int indice, Nota nota) {
		for (int i=numNotas; i>=indice; i--) {
			this.notas[i+1] = this.notas[i];
		}
		this.notas[indice] = nota;
		numNotas++;
	}

	/**
	 *   Devolver el n�mero de notas del subcomp�s
	 * @return
	 */
	public int numeroDeNotas() {
		return numNotas;
	}
	
	/**
	 *   Devolver la nota que se encuentra en la posici�n indicada por par�metro
	 * @param i
	 * �ndice de la nota que queremos
	 * @return
	 */
	public Nota nota(int i) {
		return notas[i];
	}
}
