/**
 * 
 */
package fdi.ucm.es.nfa;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
public class NFAManager {

	private Long idco;
	private List<Long> NavegacionGenerada;
	private StateNFA root;
	private int navegacion_actual;
	
	private LinkedList<Integer> ResultadoDocs;
	
	

	
	public NFAManager(List<DocumentsV> documentos) {
		
		
		idco=1l;
		Stack<StateNFA> PilaProcesar = new Stack<StateNFA>();
		root=new StateNFA(idco.longValue(),null);
		root.setDocumentosIn(documentos);
		idco++;
		PilaProcesar.push(root);
		while (!PilaProcesar.isEmpty())
			{
			StateNFA Actual = PilaProcesar.pop();
				HashMap<Long,List<DocumentsV>> indice=GeneraIndice(Actual.getDocumentosIn());
				LinkedList<Entry<Long, List<DocumentsV>>> listaViables=new LinkedList<Entry<Long, List<DocumentsV>>>();
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
				
				
				Collections.sort(listaViables,new Comparator<Entry<Long, List<DocumentsV>>>() {

					@Override
					public int compare(Entry<Long, List<DocumentsV>> x, Entry<Long, List<DocumentsV>> y) {
						return (y.getValue().size() - x.getValue().size());
					}
					
					
				});
				//TU
				//Collections.shuffle(listaViables);
				
				while (!listaViables.isEmpty())
				{
					
					
					Entry<Long, List<DocumentsV>> tocao=listaViables.poll();
					listaViables.remove(tocao);
					
					Long key =tocao.getKey();
		            List<DocumentsV> value = tocao.getValue();
		            
		            value.removeAll(procesados);
		            if (!value.isEmpty())
		            	{
		            	StateNFA Destino = new StateNFA(idco.longValue(),Actual);
	            		
		            	
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
				
				
				
			}
		}
	

	private void ProcesaBucle(StateNFA actual) {
		StateNFA procesando = actual.getPadre();
		List<Long> bucle = new LinkedList<Long>(actual.getBucle());
		while (procesando!=null)
		{
			List<Long> borrar=new LinkedList<Long>();
			for (Long long1 : bucle) {
				if (!procesando.getBucle().contains(long1))
					{
					List<StateNFA> transicionAc = procesando.getTransicion().get(long1);
					
					if (transicionAc==null)
						transicionAc=new LinkedList<StateNFA>();
						
					if (!transicionAc.contains(actual))
						{
						transicionAc.add(actual);
						procesando.getTransicion().put(long1, transicionAc);
						}
					}
				else
					borrar.add(long1);
	
			}
			bucle.removeAll(borrar);
			procesando=procesando.getPadre();
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
					Latt=new LinkedList<DocumentsV>();
				
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
		LinkedList<Long> navegacion=new LinkedList<Long>();
		
		ResultadoDocs=new LinkedList<>();
		navegacion_actual=0;
		
		EstadoNavegacionNFA ES=new EstadoNavegacionNFA(root);
		
		
		Long Salida=Navega(ES,navegacion);
		
		if (VariablesEstaticas.Debug)
			System.out.println("NFA ->"+Arrays.toString(ResultadoDocs.toArray()));
		
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
	
	



	public void setNavegacionGenerada(List<Long> navegacionGenerada) {
		NavegacionGenerada=navegacionGenerada;
		
	}
	
	private int calculaDocs(List<StateNFA> list) {
		int Salida=0;
		
		
		for (StateNFA posibleNodo : list)
			Salida=Salida+posibleNodo.getDocumentosIn().size();
		
		return Salida;
	}
	
	public Long getTotalTransitions(boolean bucles){
		Stack<StateNFA> pendientes=new Stack<>();
		pendientes.add(root);
		Long Salida=0l;
				
		while(!pendientes.isEmpty())
		{
			StateNFA act=pendientes.pop();	
			for (Entry<Long, List<StateNFA>> entrystateDFA : act.getTransicion().entrySet()) 
				for (StateNFA stateNFA : entrystateDFA.getValue()) {
					Salida++;
					pendientes.add(stateNFA);
			}
			
			if (bucles)
				Salida=Salida+act.getBucle().size();
		}
				return Salida;
		
	}

}
