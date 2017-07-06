/**
 * 
 */
package fdi.ucm.es.nfa;

import java.util.Arrays;
import java.util.Collections;
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
	
	public NFAManager(List<DocumentsV> documentos
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
				
				
				
			}
		}
	

	private void ProcesaBucle(StateNFA actual) {
		StateNFA procesando = actual.getPadre();
		List<Long> bucle = actual.getBucle();
		while (procesando!=null)
		{
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
			}
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
		
//		Integer TotalActual = CalculaDocs(estadoSiguiente.getActual());
		
		HashSet<DocumentsV> totalDocRe=new HashSet<>();
		
//		int TotalActual=0; 
		for (StateNFA posibleNodo : estadoSiguiente.getActual())
			{
			if (VariablesEstaticas.DebugTiming)
				System.out.println("DD: "+posibleNodo.getId()+"<->"+ Arrays.toString(posibleNodo.getDocumentosIn().toArray()));
			totalDocRe.addAll(posibleNodo.getDocumentosIn());
			}
		
		
		ResultadoDocs.add(totalDocRe.size());
		
		long StartNFAN1 = System.nanoTime();
		
		Queue<PosibleNodoNFA> cola = new PriorityQueue<PosibleNodoNFA>();
		
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
					cola.add(p);
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
					cola.add(p);
					TablaInversa.put(pieza, p);
					}
					else
					{
						Previo.setNumeroElementos(Previo.getNumeroElementos()+Total);
						Previo.getEstadoSiguiente().add(posibleNodoNFA);
					}
					
				}
		}	
	/*		
		Set<Long> TransicionesPosibles=new HashSet<Long>();
		
		
//		HashMap<Long, Integer> bucles=new HashMap<>();
//		
//		for (StateNFA estado : estadoSiguiente.getActual()) {
//			
//			for (Long long1 : estado.getBucle()) {
//				Integer lists = bucles.get(estado);
//				if (lists==null)
//					lists=new Integer(0);
//				lists++;
//				bucles.put(long1, lists);
//			}
//		}
//		
		
		for (StateNFA estado : estadoSiguiente.getActual()) 
			{
			TransicionesPosibles.addAll(estado.getTransicion().keySet());	
			TransicionesPosibles.addAll(estado.getBucle());
			}

//		
//		for (Entry<Long, Integer> estateLong : bucles.entrySet()) {
//			if (estateLong.getValue().intValue()!=estadoSiguiente.getActual().size())
//				TransicionesPosibles.add(estateLong.getKey());
//		}
		
		
		for (Long IdTransicion : TransicionesPosibles) {

			List<StateNFA> Resultado=new ArrayList<StateNFA>();
			for (StateNFA EstadoActual : estadoSiguiente.getActual()) {
				if (EstadoActual.getBucle().contains(IdTransicion))
					Resultado.add(EstadoActual);
				else
					{
					List<StateNFA> NextStates = EstadoActual.getTransicion().get(IdTransicion);
					if (NextStates!=null&&!NextStates.isEmpty())
						Resultado.addAll(NextStates);
					}
			}
					
			

			HashSet<DocumentsV> totalDocRe2=new HashSet<>();
			
				for (StateNFA posibleNodo : Resultado)
					totalDocRe2.addAll(posibleNodo.getDocumentosIn());
				
				int NDOC = totalDocRe2.size();
				if (NDOC!=totalDocRe.size())
				{
				PosibleNodoNFA p=new PosibleNodoNFA(totalDocRe2.size(),IdTransicion,Resultado);
				cola.add(p);
				}

			
			
		}
		*/
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
				cola.add(p);
				
			}
		}
		

		
	
				
		long EndNFAN1 = System.nanoTime();
		long DiferenciaNFAN1 = EndNFAN1-StartNFAN1;
		
		if (VariablesEstaticas.DebugTiming)
			System.out.println("TimeN Ite->"+DiferenciaNFAN1);

		HashMap<Long, PosibleNodoNFA> tablaBusqueda=new HashMap<>();
		for (PosibleNodoNFA posibleNodoNFA : cola) 
			tablaBusqueda.put(posibleNodoNFA.getLongTransicion(), posibleNodoNFA);
				
				Long DiferenciaNFAN2 =0l;
				
				if (navegacion_actual<NavegacionGenerada.size())
				{
				Long transicionA=NavegacionGenerada.get(navegacion_actual);
				navegacion_actual++;
				
				PosibleNodoNFA PP=tablaBusqueda.get(transicionA);
				
//				HashSet<StateNFA> next=new HashSet<StateNFA>();
//				
//					for (StateNFA posibleNodoNFA : estadoSiguiente.getActual()) {
//						ArrayList<StateNFA> sucession=new ArrayList<StateNFA>();
//						if (posibleNodoNFA.getBucle().contains(transicionA))
//							sucession.add(posibleNodoNFA);
//						else
//							{
//							List<StateNFA> haciaDelante = posibleNodoNFA.getTransicion().get(transicionA);
//							if (haciaDelante!=null)
//								sucession.addAll(haciaDelante);
//							}
//						
//						
//							next.addAll(sucession);
//						
//					}
				
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
	
	
//	private int CalculaDocs(List<StateNFA> estadoSiguiente) {
//		int Total=0; 
//		for (StateNFA posibleNodo : estadoSiguiente)
//			Total=Total+posibleNodo.getDocumentosIn().size();
//		return Total;
//	}
//
//
//	private int CalculaDocs(Set<StateNFA> estadoSiguiente) {
//		int Total=0; 
//		for (StateNFA posibleNodo : estadoSiguiente)
//			Total=Total+posibleNodo.getDocumentosIn().size();
//		return Total;
//	}


	public void setNavegacionGenerada(List<Long> navegacionGenerada) {
		NavegacionGenerada=navegacionGenerada;
		
	}
	
//	private List<DocumentsV> calculaTotal(StateNFA value) {
//		ArrayList<DocumentsV> Salida=new ArrayList<DocumentsV>();
//		Salida.addAll(value.getDocumentosIn());
//		for (Entry<Long, List<StateNFA>> entryHijo : value.getTransicion().entrySet()) {
//			for (StateNFA stater : entryHijo.getValue()) {
//
//				
//				List<DocumentsV> Posible=Ayuda.get(stater);
//				if (Posible==null)
//					{
//					Posible=calculaTotal(stater);
//					if (Principal.Ayuda)
//					Ayuda.put(stater, Posible);
//					}
//				
//				for (DocumentsV documentsV : Posible)
//					if (!Salida.contains(documentsV))
//						Salida.add(documentsV);
//	
//			}
//			
//		}
//		return Salida;
//	}

}
