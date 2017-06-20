package fdi.ucm.es.nfa;

import java.util.HashSet;

public class EstadoNavegacionNFA {

	
	private HashSet<StateNFA> Actual;
	

	@SuppressWarnings("unused")
	private EstadoNavegacionNFA() {
		// TODO Auto-generated constructor stub
	}

	public EstadoNavegacionNFA(StateNFA root) {
		
		Actual=new HashSet<StateNFA>();
		Actual.add(root);

	}
	
	
	public HashSet<StateNFA> getActual() {
		return Actual;
	}
	
	public void setActual(HashSet<StateNFA> actual) {
		Actual = actual;
	}
}
