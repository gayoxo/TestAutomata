/**
 * 
 */
package fdi.ucm.es.nfa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.Map.Entry;

import fdi.ucm.es.model.DocumentsV;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class NFAManager {

	private Long idco;
	
	public NFAManager(List<DocumentsV> documentos) {
		idco=1l;
		Stack<StateNFA> PilaProcesar = new Stack<StateNFA>();
		StateNFA root=new StateNFA(idco.longValue());
//		System.out.println("Creado State: "+idco.longValue());
		root.setDocumentosIn(documentos);
		idco++;
		PilaProcesar.push(root);
		while (!PilaProcesar.isEmpty())
			{
			StateNFA Actual = PilaProcesar.pop();
				HashMap<Long,List<DocumentsV>> indice=GeneraIndice(Actual.getDocumentosIn());
				List<Entry<Long, List<DocumentsV>>> listaViables=new ArrayList<Entry<Long, List<DocumentsV>>>();
				for (Entry<Long, List<DocumentsV>> name: indice.entrySet())
				{
					Long key =name.getKey();
		            List<DocumentsV> value = name.getValue();
		            
		            if (value.size()==Actual.getDocumentosIn().size())
		            	Actual.getBucle().add(key);
		            else
		            	listaViables.add(name);
				}
				
				HashSet<DocumentsV> procesados=new HashSet<DocumentsV>();
				
				Collections.shuffle(listaViables);
				
				while (!listaViables.isEmpty())
				{
					
					Entry<Long, List<DocumentsV>> tocao=listaViables.get(0);
					listaViables.remove(tocao);
					
					Long key =tocao.getKey();
		            List<DocumentsV> value = tocao.getValue();
		            
		            value.removeAll(procesados);
		            if (!value.isEmpty())
		            	{
		            	StateNFA Destino = new StateNFA(idco.longValue());
	            		idco++;
	            		Destino.setDocumentosIn(value);

		            	PilaProcesar.add(Destino);
	            		
		            	procesados.addAll(value);
	            		
	            		Actual.getTransicion().put(key, Destino);
		            	}
					
				}
				
				
				
			}
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
