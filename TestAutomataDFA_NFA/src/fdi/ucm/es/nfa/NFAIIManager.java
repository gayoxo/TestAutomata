/**
 * 
 */
package fdi.ucm.es.nfa;

import java.util.List;
import java.util.Stack;
import fdi.ucm.es.model.DocumentsV;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class NFAIIManager extends NFAManager{

	public NFAIIManager(List<DocumentsV> documentos
//			, List<Long> tiemposNFA
			) {
		
		super();
		
		
		
//		long StartNFA = System.nanoTime();
		
		idco=1l;
		Stack<StateNFA> PilaProcesar = new Stack<StateNFA>();
		root=new StateNFA(idco.longValue(),null);
//		long EndNFA = System.nanoTime();
//		long DiferenciaNFA = EndNFA-StartNFA;
//		tiemposNFA.add(DiferenciaNFA);
		
//		System.out.println("Creado State: "+idco.longValue());
		root.setDocumentosIn(documentos);
		idco++;
		PilaProcesar.push(root);
		
		
		//COSAS DIFERENTES
		
		/*
		while (!PilaProcesar.isEmpty())
			{
			StateNFA Actual = PilaProcesar.pop();
				HashMap<Long,List<DocumentsV>> indice=GeneraIndice(Actual.getDocumentosIn());
				List<Entry<Long, List<DocumentsV>>> listaViables=new LinkedList<Entry<Long, List<DocumentsV>>>();
				for (Entry<Long, List<DocumentsV>> name: indice.entrySet())
				{
					Long key =name.getKey();
		            List<DocumentsV> value = name.getValue();
		            
		            if (value.size()==Actual.getDocumentosIn().size())
		            	Actual.getBucle().add(key);
		            else
		            	listaViables.add(name);
				}
				
				ProcesaBucle(Actual);

				
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
		            	StateNFA Destino = new StateNFA(idco.longValue(),Actual);
	            		
//		            	long EndNFAO = System.nanoTime();
//		        		long DiferenciaNFAO = EndNFAO-StartNFA;
//		        		tiemposNFA.add(DiferenciaNFAO);
		            	
		            	idco++;
	            		Destino.setDocumentosIn(value);

		            	PilaProcesar.add(Destino);
	            		
		            	procesados.addAll(value);
	            		
		            	
		            	List<StateNFA> ListaAc = Actual.getTransicion().get(key);
		            	
		            	if (ListaAc==null)
		            		ListaAc=new LinkedList<StateNFA>();
		            	
		            	ListaAc.add(Destino);
		            	
	            		Actual.getTransicion().put(key, ListaAc);
		            	}
					
				}
				
				
				
			}*/
		}
	



}
