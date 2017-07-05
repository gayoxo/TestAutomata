package fdi.ucm.es.ii;

public class PosibleNodoII implements Comparable<PosibleNodoII>{

	private int numeroElementos;
	private Long longTransicion;

	
	@SuppressWarnings("unused")
	private PosibleNodoII() {
		this.numeroElementos=0;
		this.longTransicion=0l;
	}
	
	public PosibleNodoII(int nelements, Long transicion) {
		this.numeroElementos=nelements;
		this.longTransicion=transicion;
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

	
}
