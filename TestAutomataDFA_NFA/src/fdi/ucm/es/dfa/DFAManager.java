/**
 * 
 */
package fdi.ucm.es.dfa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import fdi.ucm.es.Principal;
import fdi.ucm.es.model.DocumentsV;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class DFAManager {
	

	private Long idco;
	private StateDFA root;
	private ArrayList<Long> NavegacionGenerada;
	private ArrayList<Integer> ResultadoDocs;

	public DFAManager(List<DocumentsV> documentos
//			, List<Long> tiemposCreacion
			) {

//	long StartDFA = System.nanoTime();	
		
	idco=1l;
	PriorityQueue<StateDFA> PilaProcesar = new PriorityQueue<StateDFA>();
	Map<Set<DocumentsV>,StateDFA> existing = new HashMap<>();
	root=new StateDFA(idco.longValue());
//	long EndDFA = System.nanoTime();
//	long DiferenciaDFA = EndDFA-StartDFA;
//	tiemposCreacion.add(DiferenciaDFA);
	if (Principal.Debug)
		System.out.println("Creado State: "+idco.longValue());
	root.setDocumentosIn(documentos);
	
	existing.put(new LinkedHashSet<DocumentsV>(documentos),root);
	
	idco++;
	PilaProcesar.add(root);
	while (!PilaProcesar.isEmpty())
		{
		if (Principal.Debug)
			System.out.println("Pila Size: "+PilaProcesar.size());

			StateDFA Actual = PilaProcesar.remove();
			HashMap<Long,List<DocumentsV>> indice=GeneraIndice(Actual.getDocumentosIn());
			for (Entry<Long, List<DocumentsV>> name: indice.entrySet()){
	            Long key =name.getKey();
	            List<DocumentsV> value = name.getValue();
	            
	            if (value.size()==Actual.getDocumentosIn().size())
	            	Actual.getBucle().add(key);
	            else
	            {
	            	
	            	
	            	StateDFA Destino=null;
	            	
	            	
	            	Destino=existing.get(new LinkedHashSet<DocumentsV>(value));
	            	
	            	
//	            	PriorityQueue<StateDFA> PilaTemporal = new PriorityQueue<StateDFA>(PilaProcesar);
//	            	while (!PilaTemporal.isEmpty()&&Destino==null)
//	            	 {
//	            		StateDFA statepos = PilaTemporal.remove();
//	            		
//	            		if (statepos.getDocumentosIn().size()<value.size())
//	            			break;
//	            		
//						if (statepos.getDocumentosIn().size()==value.size() && check(statepos,value))
//							{
//							Destino=statepos;
//							break;
//							}
//					}
	            	
	            	if (Destino==null)
	            		{
	            		if (Principal.Debug)
	            			System.out.println("Creado State: "+idco.longValue());
	            		Destino=new StateDFA(idco.longValue());
//	            		long EndDFAO = System.nanoTime();
//	            		long DiferenciaDFAO = EndDFAO-StartDFA;
//	            		tiemposCreacion.add(DiferenciaDFAO);
	            		
	            		idco++;
	            		Destino.setDocumentosIn(value);
	            		existing.put(new LinkedHashSet<DocumentsV>(value),Destino);

		            	PilaProcesar.add(Destino);
	            		}

	            		
	            		Actual.getTransicion().put(key, Destino);
	            }
	            	  


			} 
		}
	}

//	/**
//	 * Check estado documentos
//	 * @param statepos
//	 * @param value
//	 * @return
//	 */
//	private boolean check(StateDFA statepos, List<DocumentsV> value) {
//		
//		
//	    Object[] ListaA = statepos.getDocumentosIn().toArray();
//		Object[] ListaB = value.toArray();
//		
//		Arrays.sort(ListaA);
//		Arrays.sort(ListaB);
//		
//		for (int i = 0; i < ListaA.length; i++) {
//			if (ListaA[i]!=ListaB[i])
//				return false;
//		}
//		
//		
//		return true;
////		List<DocumentsV> via=new ArrayList<>(statepos.getDocumentosIn());
////			via.removeAll(value);
////		return via.isEmpty();
//	}

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

	public Long Navega() {
		ArrayList<Long> navegacionGeneradaNueva=new ArrayList<Long>();
		
		ResultadoDocs=new ArrayList<>();
		
		Long Salida=Navega(root,navegacionGeneradaNueva);
		
		this.NavegacionGenerada=navegacionGeneradaNueva;
		
		if (Principal.Debug)
			System.out.println("DFA ->"+Arrays.toString(ResultadoDocs.toArray()));
		
		return Salida;
		
	}

	private Long Navega(StateDFA estadoSiguiente, ArrayList<Long> Salida) {
		
		ResultadoDocs.add(estadoSiguiente.getDocumentosIn().size());
		
		
		long StartDFAN1 = System.nanoTime();
		
Queue<PosibleNodoDFA> cola = new PriorityQueue<PosibleNodoDFA>();

		
		for (Entry<Long, StateDFA> pieza : estadoSiguiente.getTransicion().entrySet()) {
			
			List<DocumentsV> Total=pieza.getValue().getDocumentosIn();
			
			PosibleNodoDFA p=new PosibleNodoDFA(Total.size(),pieza.getKey(),pieza.getValue());
			cola.add(p);
			
		}
		
		
		long EndDFAN1 = System.nanoTime();
		long DiferenciaDFAN1 = EndDFAN1-StartDFAN1;
		
		boolean selecionada=false;
		PosibleNodoDFA seleccion=null;
		
		
		Random R=new Random();
		
		while(!selecionada&&!cola.isEmpty())
		{
			seleccion=cola.remove();
			selecionada=R.nextBoolean();
		}
		
		
		Long DiferenciaDFAN2 =0l;
		
		if (seleccion!=null&&selecionada)
			{
			Salida.add(seleccion.getLongTransicion());
			DiferenciaDFAN2 =Navega(seleccion.getEstadoSiguiente(),Salida);
			}
		
		return DiferenciaDFAN1+DiferenciaDFAN2;
		
	}

	

	public void setNavegacionGenerada(ArrayList<Long> navegacionGenerada) {
		//No hace nada
		
	}
	
	public ArrayList<Long> getNavegacionGenerada() {
		return NavegacionGenerada;
	}
	
	

}
