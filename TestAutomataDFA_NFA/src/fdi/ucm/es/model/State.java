/**
 * 
 */
package fdi.ucm.es.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public abstract class State {

	private Long Id;
	private List<Long> Bucle;
	private List<DocumentsV> DocumentosIn;
	
	public State(Long id) {
		Id = id;
		Bucle=new ArrayList<Long>();
		DocumentosIn=new ArrayList<DocumentsV>();
	}

	public State() {
		Bucle=new ArrayList<Long>();
		DocumentosIn=new ArrayList<DocumentsV>();
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

	
}
