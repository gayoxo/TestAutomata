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
import java.util.Random;
import java.util.Stack;

import fdi.ucm.es.model.DocumentsV;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class DFAManager {
	

	private Long idco;
	private StateDFA root;

	public DFAManager(List<DocumentsV> documentos, List<Long> tiemposCreacion) {

	long StartDFA = System.nanoTime();	
		
	idco=1l;
	Stack<StateDFA> PilaProcesar = new Stack<StateDFA>();
	HashMap<Integer,List<StateDFA>> Total =new HashMap<Integer,List<StateDFA>>();
	root=new StateDFA(idco.longValue());
	long EndDFA = System.nanoTime();
	long DiferenciaDFA = EndDFA-StartDFA;
	tiemposCreacion.add(DiferenciaDFA);
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
	            		long EndDFAO = System.nanoTime();
	            		long DiferenciaDFAO = EndDFAO-StartDFA;
	            		tiemposCreacion.add(DiferenciaDFAO);
	            		
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

	public ArrayList<Long> Navega() {
		ArrayList<Long> Salida=new ArrayList<Long>();
		
		Navega(root,Salida);
		
		return Salida;
		
	}

	private void Navega(StateDFA estadoSiguiente, ArrayList<Long> Salida) {
Queue<PosibleNodoDFA> cola = new PriorityQueue<PosibleNodoDFA>();
		
		for (Entry<Long, StateDFA> pieza : estadoSiguiente.getTransicion().entrySet()) {
			HashSet<StateDFA> procesed=new HashSet<StateDFA>();
			List<DocumentsV> Total=calculaTotal(pieza.getValue(),procesed);
			PosibleNodoDFA p=new PosibleNodoDFA(Total.size(),pieza.getKey(),pieza.getValue());
			cola.add(p);
		}
		
		
		boolean selecionada=false;
		PosibleNodoDFA seleccion=null;
		
		Random R=new Random();
		
		while(!selecionada&&!cola.isEmpty())
		{
			seleccion=cola.remove();
			selecionada=R.nextBoolean();
			if (selecionada)
				{
					Salida.add(seleccion.getLongTransicion());
					Navega(seleccion.getEstadoSiguiente(),Salida);
				}
			
		}
		
	}

	private List<DocumentsV> calculaTotal(StateDFA value,HashSet<StateDFA> procesed) {
		ArrayList<DocumentsV> Salida=new ArrayList<DocumentsV>();
		Salida.addAll(value.getDocumentosIn());
		procesed.add(value);
		for (Entry<Long, StateDFA> entryHijo : value.getTransicion().entrySet()) {
			if (!procesed.contains(entryHijo.getValue()))
				{
				List<DocumentsV> Posible = calculaTotal(entryHijo.getValue(),procesed);	
				for (DocumentsV documentsV : Posible)
					if (!Salida.contains(documentsV))
						Salida.add(documentsV);
				
				}
		}
		return Salida;
	}

	public void setNavegacionGenerada(ArrayList<Long> navegacionGenerada) {
		//No hace nada
		
	}
	
	

}
