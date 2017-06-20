/**
 * 
 */
package fdi.ucm.es.dfa;


/**
 * @author gayoxo
 *
 */
public class PosibleNodoDFA implements Comparable<PosibleNodoDFA>{

	private int numeroElementos;
	private Long longTransicion;
	private StateDFA estadoSiguiente;

	
	@SuppressWarnings("unused")
	private PosibleNodoDFA() {
		this.numeroElementos=0;
		this.longTransicion=0l;
		this.estadoSiguiente=null;
	}
	
	public PosibleNodoDFA(int nelements, Long transicion, StateDFA estadoSiguiente) {
		this.numeroElementos=nelements;
		this.longTransicion=transicion;
		this.estadoSiguiente=estadoSiguiente;
	}

	@Override
	public int compareTo(PosibleNodoDFA o) {
		if (numeroElementos < o.getNumeroElementos()) {
            return 1;
        } else if (numeroElementos > o.getNumeroElementos()) {
            return -1;
        } else {
            return 0;
        }
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

	public StateDFA getEstadoSiguiente() {
		return estadoSiguiente;
	}

	public void setEstadoSiguiente(StateDFA estadoSiguiente) {
		this.estadoSiguiente = estadoSiguiente;
	}


	
	
	

}
