/**
 * 
 */
package fdi.ucm.es.dfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

import fdi.ucm.es.model.DocumentsV;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class DFAManager {
	

	private Long idco;

	public DFAManager(List<DocumentsV> documentos) {

	idco=1l;
	Stack<StateDFA> PilaProcesar = new Stack<StateDFA>();
	HashMap<Integer,List<StateDFA>> Total =new HashMap<Integer,List<StateDFA>>();
	StateDFA root=new StateDFA(idco.longValue());
//	System.out.println("Creado State: "+idco.longValue());
	root.setDocumentosIn(documentos);
	idco++;
	PilaProcesar.push(root);
	while (!PilaProcesar.isEmpty())
		{
			StateDFA Actual = PilaProcesar.pop();
			HashMap<Long,List<DocumentsV>> indice=GeneraIndice(Actual.getDocumentosIn());
			for (Entry<Long, List<DocumentsV>> name: indice.entrySet()){
	            Long key =name.getKey();
	            List<DocumentsV> value = name.getValue();
	            
	            if (value.size()==Actual.getDocumentosIn().size())
	            	Actual.getBucle().add(key);
	            else
	            {
	            	StateDFA Destino=null;
	            	List<StateDFA> Viables = Total.get(new Integer(value.size()));
	            	
	            	if (Viables==null)
	            		Viables=new ArrayList<StateDFA>();
	            	
	            	for (StateDFA statepos : Viables) {
						if (check(statepos,value))
						{
						Destino=statepos;
						break;
						}
					}
	            	
	            	if (Destino==null)
	            		{
	            	//	System.out.println("Creado State: "+idco.longValue());
	            		Destino=new StateDFA(idco.longValue());
	            		idco++;
	            		Destino.setDocumentosIn(value);
	            		
	            		List<StateDFA> ViablesN = Total.get(new Integer(value.size()));
		            	
		            	if (ViablesN==null)
		            		ViablesN=new ArrayList<StateDFA>();
		            	
		            	ViablesN.add(Destino);
		            	Total.put(new Integer(value.size()), ViablesN);
		            	PilaProcesar.add(Destino);
	            		}
	            		
	            		Actual.getTransicion().put(key, Destino);
	            }
	            	  


			} 
		}
	}

	/**
	 * Check estado documentos
	 * @param statepos
	 * @param value
	 * @return
	 */
	private boolean check(StateDFA statepos, List<DocumentsV> value) {
		HashSet<DocumentsV> via=new HashSet<>(statepos.getDocumentosIn());
			via.removeAll(value);
		return via.isEmpty();
	}

	/**
	 * Genera el indice inverso
	 * @param documentosIn
	 * @return
	 */
	private HashMap<Long, List<DocumentsV>> GeneraIndice(List<DocumentsV> documentosIn) {
		HashMap<Long, List<DocumentsV>> Salida = new HashMap<Long, List<DocumentsV>>();
		for (DocumentsV documentsV : documentosIn) {
			for (Long documentsVatt : documentsV.getAtt()) {
				List<DocumentsV> Latt=Salida.get(documentsVatt);
				
				if (Latt==null)
					Latt=new ArrayList<DocumentsV>();
				
				if (!Latt.contains(documentsV))
					Latt.add(documentsV);
				Salida.put(documentsVatt, Latt);
			}
		}
		return Salida;
	}

	public Long getIdco() {
		return idco;
	}

	public void setIdco(Long idco) {
		this.idco = idco;
	}
	
	

}
