/**
 * 
 */
package fdi.ucm.es.nfa;

import java.util.List;

/**
 * @author gayoxo
 *
 */
public class PosibleNodoNFA implements Comparable<PosibleNodoNFA>{

	private int numeroElementos;
	private Long longTransicion;
	private List<StateNFA> estadoSiguiente;

	
	@SuppressWarnings("unused")
	private PosibleNodoNFA() {
		this.numeroElementos=0;
		this.longTransicion=0l;
		this.estadoSiguiente=null;
	}
	
	public PosibleNodoNFA(int nelements, Long transicion, List<StateNFA> estadoSiguiente) {
		this.numeroElementos=nelements;
		this.longTransicion=transicion;
		this.estadoSiguiente=estadoSiguiente;
	}

	@Override
	public int compareTo(PosibleNodoNFA o) {
		return  o.numeroElementos - numeroElementos;
	}

	public int getNumeroElementos() {
		return numeroElementos;
	}

	public void setNumeroElementos(int numeroElementos) {
		this.numeroElementos = numeroElementos;
	}

	public Long getLongTransicion() {
		return longTransicion;
	}

	public void setLongTransicion(Long longTransicion) {
		this.longTransicion = longTransicion;
	}

	public List<StateNFA> getEstadoSiguiente() {
		return estadoSiguiente;
	}

	public void setEstadoSiguiente(List<StateNFA> estadoSiguiente) {
		this.estadoSiguiente = estadoSiguiente;
	}
	
	
	

}
