/**
 * 
 */
package fdi.ucm.es.dfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import fdi.ucm.es.model.DocumentsV;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class DFAManager {

	public DFAManager(List<DocumentsV> documentos) {

	Stack<StateDFA> PilaProcesar = new Stack<StateDFA>();
	Queue<StateDFA> Viables =new PriorityQueue<StateDFA>();
	Long idco=1l;
	StateDFA root=new StateDFA(idco.longValue());
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
	            	for (StateDFA statepos : Viables) {
						if (check(statepos,value))
						{
						Destino=statepos;
						break;
						}
					}
	            	
	            	if (Destino==null)
	            		{
	            		Destino=new StateDFA(idco.longValue());
	            		idco++;
	            		Destino.setDocumentosIn(documentos);
	            		//AQUI SE METE
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

}
