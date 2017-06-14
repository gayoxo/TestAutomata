package fdi.ucm.es.dfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fdi.ucm.es.model.DocumentsV;

public class StateDFA {

	private Long Id;
	private List<Long> Bucle;
	private List<DocumentsV> DocumentosIn;
	private HashMap<Long,StateDFA> Transicion;
	
	@SuppressWarnings("unused")
	private StateDFA() {
	
	}
	
	

	public StateDFA(Long id) {
		super();
		Id = id;
		Bucle=new ArrayList<Long>();
		DocumentosIn=new ArrayList<DocumentsV>();
		Transicion=new HashMap<Long,StateDFA>();
	}



	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public List<Long> getBucle() {
		return Bucle;
	}

	public void setBucle(List<Long> bucle) {
		Bucle = bucle;
	}

	public List<DocumentsV> getDocumentosIn() {
		return DocumentosIn;
	}

	public void setDocumentosIn(List<DocumentsV> documentosIn) {
		DocumentosIn = documentosIn;
	}

	public HashMap<Long, StateDFA> getTransicion() {
		return Transicion;
	}

	public void setTransicion(HashMap<Long, StateDFA> transicion) {
		Transicion = transicion;
	}
	
	
}
