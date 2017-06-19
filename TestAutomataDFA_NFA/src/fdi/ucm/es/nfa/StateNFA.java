/**
 * 
 */
package fdi.ucm.es.nfa;

import java.util.HashMap;
import java.util.List;

import fdi.ucm.es.model.State;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class StateNFA extends State {

	private StateNFA Padre;
	private HashMap<Long,List<StateNFA>> Transicion;
	
	
	@SuppressWarnings("unused")
	private StateNFA() {
		super();
	}
	
	
	/**
	 * @param id
	 */
	public StateNFA(Long id,StateNFA Padre) {
		super(id);
		this.Padre=Padre;
		this.Transicion=new HashMap<Long,List<StateNFA>>();
	}
	public StateNFA getPadre() {
		return Padre;
	}
	public void setPadre(StateNFA padre) {
		Padre = padre;
	}
	public HashMap<Long, List<StateNFA>> getTransicion() {
		return Transicion;
	}
	public void setTransicion(HashMap<Long, List<StateNFA>> transicion) {
		Transicion = transicion;
	}


	
	
	
}
