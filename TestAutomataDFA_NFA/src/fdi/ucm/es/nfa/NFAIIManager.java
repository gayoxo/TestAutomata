/**
 * 
 */
package fdi.ucm.es.nfa;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import fdi.ucm.es.model.DocumentsV;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class NFAIIManager extends NFAManager{
	
	

	

	public NFAIIManager(List<DocumentsV> documentos) {
		
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
		
		for (DocumentsV doci : documentos) {
			StateNFA Destino = new StateNFA(idco.longValue(),root);
    		
//        	long EndNFAO = System.nanoTime();
//    		long DiferenciaNFAO = EndNFAO-StartNFA;
//    		tiemposNFA.add(DiferenciaNFAO);
        	
        	idco++;
        	List<DocumentsV> docYo = new ArrayList<DocumentsV>();
        	docYo.add(doci);
        	Destino.setDocumentosIn(docYo);
			for (Long longT : doci.getAtt()) {
				List<StateNFA> listaT = root.getTransicion().get(longT);
				if (listaT==null)
					listaT=new ArrayList<StateNFA>();
				
				listaT.add(Destino);
				root.getTransicion().put(longT, listaT);
				
			}
			
			Destino.setBucle(new ArrayList<>(doci.getAtt()));
		}

		}
	


	

}
