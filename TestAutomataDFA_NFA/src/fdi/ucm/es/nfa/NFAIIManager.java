/**
 * 
 */
package fdi.ucm.es.nfa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.Map.Entry;

import fdi.ucm.es.VariablesEstaticas;
import fdi.ucm.es.model.DocumentsV;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class NFAIIManager {
	
	
	private Long idco;
	private List<Long> NavegacionGenerada;
	private StateNFA root;
	private int navegacion_actual;
	
	private LinkedList<Integer> ResultadoDocs;
	

	public NFAIIManager(List<DocumentsV> documentos
//			, List<Long> tiemposNFA
			) {
		
		
		
		
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
	

	public Long getIdco() {
		return idco;
	}

	public void setIdco(Long idco) {
		this.idco = idco;
	}


	public Long Navega() {
		LinkedList<Long> navegacion=new LinkedList<Long>();
		
		ResultadoDocs=new LinkedList<>();
		navegacion_actual=0;
		
		EstadoNavegacionNFA ES=new EstadoNavegacionNFA(root);
		
		
		Long Salida=Navega(ES,navegacion);
		
		if (VariablesEstaticas.Debug)
			System.out.println("NFAII ->"+Arrays.toString(ResultadoDocs.toArray()));
		
		return Salida;

		
	}
	
	private Long Navega(EstadoNavegacionNFA estadoSiguiente, LinkedList<Long> Salida) {
		
		
		
		
		
		long StartNFAN1 = System.nanoTime();
		
		
		int calculaNodosIn=calculaDocs(new LinkedList<StateNFA>(estadoSiguiente.getActual()));
		
		
		
		ResultadoDocs.add(calculaNodosIn);
		
	
		
		Queue<PosibleNodoNFA> colaT = new PriorityQueue<PosibleNodoNFA>();
		
		Queue<PosibleNodoNFA> colaR = new PriorityQueue<PosibleNodoNFA>();
		
		if (estadoSiguiente.getActual().size()>1)	
		{
		
			HashMap<Long, PosibleNodoNFA> TablaInversa=new HashMap<>();
			
		for (StateNFA posibleNodoNFA : estadoSiguiente.getActual()) {
				for (Entry<Long, List<StateNFA>> pieza : posibleNodoNFA.getTransicion().entrySet()) {
					
					
					List<StateNFA> Resultado=new LinkedList<>(pieza.getValue());
					
					int Total=0; 
					for (StateNFA posibleNodo : Resultado)
						Total=Total+posibleNodo.getDocumentosIn().size();
					
					
					PosibleNodoNFA Previo = TablaInversa.get(pieza.getKey());
					
					if (Previo==null)
					{
					PosibleNodoNFA p=new PosibleNodoNFA(Total,pieza.getKey(),Resultado);
					colaT.add(p);
					TablaInversa.put(pieza.getKey(), p);
					}
					else
					{
						Previo.setNumeroElementos(Previo.getNumeroElementos()+Total);
						Previo.getEstadoSiguiente().addAll(Resultado);
					}
					
				}
				
				for (Long pieza : posibleNodoNFA.getBucle()) {
					
					
					
					int Total=posibleNodoNFA.getDocumentosIn().size(); 

					List<StateNFA> STA=new LinkedList<StateNFA>();
					STA.add(posibleNodoNFA);
					
					PosibleNodoNFA Previo = TablaInversa.get(pieza);
					
					if (Previo==null)
					{
					PosibleNodoNFA p=new PosibleNodoNFA(Total,pieza,STA);
					colaT.add(p);
					TablaInversa.put(pieza, p);
					}
					else
					{
						Previo.setNumeroElementos(Previo.getNumeroElementos()+Total);
						Previo.getEstadoSiguiente().add(posibleNodoNFA);
					}
					
					
					
					
				}
		}
		
		
		
		for (PosibleNodoNFA posibleNodoNFA2 : colaT) {
			int t=calculaDocs(posibleNodoNFA2.getEstadoSiguiente());
			if (t!=calculaNodosIn)
				colaR.add(posibleNodoNFA2);
		}
		

		}
		
		else
		{
			StateNFA Actual=(StateNFA) estadoSiguiente.getActual().toArray()[0];
				//SOLO hay uno
			
			for (Entry<Long, List<StateNFA>> pieza : Actual.getTransicion().entrySet()) {
				
				List<StateNFA> Resultado=pieza.getValue();
				
				int Total=0; 
				for (StateNFA posibleNodo : Resultado)
					Total=Total+posibleNodo.getDocumentosIn().size();
				
				
				PosibleNodoNFA p=new PosibleNodoNFA(Total,pieza.getKey(),pieza.getValue());
				colaT.add(p);
				
			}
		}
		

		
	
				
		long EndNFAN1 = System.nanoTime();
		long DiferenciaNFAN1 = EndNFAN1-StartNFAN1;
		
		if (VariablesEstaticas.DebugTiming)
			System.out.println("TimeN Ite->"+DiferenciaNFAN1);

		HashMap<Long, PosibleNodoNFA> tablaBusqueda=new HashMap<>();
		for (PosibleNodoNFA posibleNodoNFA : colaT) 
			tablaBusqueda.put(posibleNodoNFA.getLongTransicion(), posibleNodoNFA);
				
				Long DiferenciaNFAN2 =0l;
				
				if (navegacion_actual<NavegacionGenerada.size())
				{
				Long transicionA=NavegacionGenerada.get(navegacion_actual);
				navegacion_actual++;
				
				PosibleNodoNFA PP=tablaBusqueda.get(transicionA);
				
				
				if (PP==null)
					System.out.println(Arrays.toString(ResultadoDocs.toArray())+"<->"+Arrays.toString(NavegacionGenerada.toArray()));
				
					estadoSiguiente.setActual(new HashSet<>(PP.getEstadoSiguiente()));
					DiferenciaNFAN2 =Navega(estadoSiguiente, Salida);
	
					
				}
				
				
				if (VariablesEstaticas.DebugTiming)
					System.out.println("TimeN Ite2->"+DiferenciaNFAN2);
				
				
				if (VariablesEstaticas.DebugTiming)
					System.out.println("TimeN IteT->"+DiferenciaNFAN1+DiferenciaNFAN2);
				
				return DiferenciaNFAN1+DiferenciaNFAN2;
				
				
				
			}
	
	



	private int calculaDocs(List<StateNFA> list) {
		HashSet<DocumentsV> totalDocRe=new HashSet<>();
		for (StateNFA posibleNodo : list)
			totalDocRe.addAll(posibleNodo.getDocumentosIn());
		
		return totalDocRe.size();
	}


	public void setNavegacionGenerada(List<Long> navegacionGenerada) {
		NavegacionGenerada=navegacionGenerada;
		
	}

}
