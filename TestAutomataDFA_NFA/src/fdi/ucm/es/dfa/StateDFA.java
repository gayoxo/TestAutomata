package fdi.ucm.es.dfa;

import java.util.HashMap;
import fdi.ucm.es.model.State;

public class StateDFA extends State {


	private HashMap<Long,StateDFA> Transicion;
	
	@SuppressWarnings("unused")
	private StateDFA() {
		super();
	}
	
	

	public StateDFA(Long id) {
		super(id);
		Transicion=new HashMap<Long,StateDFA>();
	}



	

	public HashMap<Long, StateDFA> getTransicion() {
		return Transicion;
	}

	public void setTransicion(HashMap<Long, StateDFA> transicion) {
		Transicion = transicion;
	}



	
	
	
}
