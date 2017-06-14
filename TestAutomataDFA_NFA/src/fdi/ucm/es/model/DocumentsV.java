/**
 * 
 */
package fdi.ucm.es.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class DocumentsV {
	
	private ArrayList<Long> Att;
	private Long Id;

	@SuppressWarnings("unused")
	private DocumentsV() {
		Att=new ArrayList<Long>();
		this.Id=0l;
	}
	
	public DocumentsV(Long Id) {
		Att=new ArrayList<Long>();
		this.Id=Id;
		
	}
	
	public ArrayList<Long> getAtt() {
		return Att;
	}

	public void setAtt(ArrayList<Long> att) {
		Att = att;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}
	
	
	
	@Override
	public String toString() {
		return "{ID:"+Id+","+"Att:"+Arrays.toString(Att.toArray())+"}";
	}

}
