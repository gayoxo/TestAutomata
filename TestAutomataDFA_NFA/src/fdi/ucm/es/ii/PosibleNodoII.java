package fdi.ucm.es.ii;

import java.util.HashMap;
import java.util.List;

import fdi.ucm.es.model.DocumentsV;

public class PosibleNodoII implements Comparable<PosibleNodoII>{

	private int numeroElementos;
	private Long longTransicion;
	private HashMap<Long, List<DocumentsV>> estadoSiguiente;

	
	@SuppressWarnings("unused")
	private PosibleNodoII() {
		this.numeroElementos=0;
		this.longTransicion=0l;
		this.estadoSiguiente=null;
	}
	
	public PosibleNodoII(int nelements, Long transicion, HashMap<Long, List<DocumentsV>> estadoSiguiente) {
		this.numeroElementos=nelements;
		this.longTransicion=transicion;
		this.estadoSiguiente=estadoSiguiente;
	}

	@Override
	public int compareTo(PosibleNodoII o) {
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

	public HashMap<Long, List<DocumentsV>> getEstadoSiguiente() {
		return estadoSiguiente;
	}

	public void setEstadoSiguiente(HashMap<Long, List<DocumentsV>> estadoSiguiente) {
		this.estadoSiguiente = estadoSiguiente;
	}


	
}
