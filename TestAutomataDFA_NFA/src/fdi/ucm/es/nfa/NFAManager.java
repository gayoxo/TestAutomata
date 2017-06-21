/**
 * 
 */
package fdi.ucm.es.nfa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.Map.Entry;

import fdi.ucm.es.model.DocumentsV;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class NFAManager {

	private Long idco;
	private ArrayList<Long> NavegacionGenerada;
	private StateNFA root;
	private int navegacion_actual;
	
	public NFAManager(List<DocumentsV> documentos, List<Long> tiemposNFA) {
		
		long StartNFA = System.nanoTime();
		
		idco=1l;
		Stack<StateNFA> PilaProcesar = new Stack<StateNFA>();
		root=new StateNFA(idco.longValue(),null);
		long EndNFA = System.nanoTime();
		long DiferenciaNFA = EndNFA-StartNFA;
		tiemposNFA.add(DiferenciaNFA);
		
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
	            		
		            	long EndNFAO = System.nanoTime();
		        		long DiferenciaNFAO = EndNFAO-StartNFA;
		        		tiemposNFA.add(DiferenciaNFAO);
		            	
		            	idco++;
	            		Destino.setDocumentosIn(value);

		            	PilaProcesar.add(Destino);
	            		
		            	procesados.addAll(value);
	            		
		            	
		            	List<StateNFA> ListaAc = Actual.getTransicion().get(key);
		            	
		            	if (ListaAc==null)
		            		ListaAc=new ArrayList<StateNFA>();
		            	
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
						transicionAc=new ArrayList<StateNFA>();
						
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
		ArrayList<Long> navegacion=new ArrayList<Long>();
		
		navegacion_actual=0;
		
		EstadoNavegacionNFA ES=new EstadoNavegacionNFA(root);
		Long Salida=Navega(ES,navegacion);
		
		return Salida;

		
	}
	
	private Long Navega(EstadoNavegacionNFA estadoSiguiente, ArrayList<Long> Salida) {
		
		
		long StartNFAN1 = System.nanoTime();
		
		Queue<PosibleNodoNFA> cola = new PriorityQueue<PosibleNodoNFA>();
				
		for (StateNFA posibleNodoNFA : estadoSiguiente.getActual()) {
			for (Entry<Long, List<StateNFA>> pieza : posibleNodoNFA.getTransicion().entrySet()) {
				
				HashSet<DocumentsV> Total=new HashSet<DocumentsV>(); 
				for (StateNFA posibleNodo : pieza.getValue()) {
					Total.addAll(calculaTotal(posibleNodo));
				}
				
				PosibleNodoNFA p=new PosibleNodoNFA(Total.size(),pieza.getKey(),pieza.getValue());
				cola.add(p);
				
			}
		}
		
		
		HashSet<Long> PosiblesBucles=new HashSet<Long>();
		for (StateNFA posibleStatebucle : estadoSiguiente.getActual())
			PosiblesBucles.addAll(posibleStatebucle.getBucle());

		
		
		for (Long long1 : PosiblesBucles) {
			List<StateNFA> reduce=new ArrayList<StateNFA>();
			for (StateNFA estadovalido : estadoSiguiente.getActual())
				if (estadovalido.getBucle().contains(long1))
					reduce.add(estadovalido);
			
			if (reduce.size()<estadoSiguiente.getActual().size())
			{
				HashSet<DocumentsV> Total=new HashSet<DocumentsV>(); 
				for (StateNFA posibleNodo : reduce) {
					Total.addAll(calculaTotal(posibleNodo));
				}
				
				PosibleNodoNFA p=new PosibleNodoNFA(Total.size(),long1,reduce);
				cola.add(p);
			}
				
		}		
				
		long EndNFAN1 = System.nanoTime();
		long DiferenciaNFAN1 = EndNFAN1-StartNFAN1;
				
//				boolean selecionada=false;
//				PosibleNodoNFA seleccion=null;
//				
//				Random R=new Random();
//				
//				while(!selecionada&&!cola.isEmpty())
//				{
//					seleccion=cola.remove();
//					selecionada=R.nextBoolean();
//					if (selecionada)
//							Salida.add(seleccion.getLongTransicion());
//								
//					
//				}
				
				
				Long DiferenciaNFAN2 =0l;
				
				if (navegacion_actual<NavegacionGenerada.size())
				{
				Long transicionA=NavegacionGenerada.get(navegacion_actual);
				navegacion_actual++;
				HashSet<StateNFA> next=new HashSet<StateNFA>();
				
					for (StateNFA posibleNodoNFA : estadoSiguiente.getActual()) {
						ArrayList<StateNFA> sucession=new ArrayList<StateNFA>();
						if (posibleNodoNFA.getBucle().contains(transicionA))
							sucession.add(posibleNodoNFA);
						else
							{
							List<StateNFA> haciaDelante = posibleNodoNFA.getTransicion().get(transicionA);
							if (haciaDelante!=null)
								sucession.addAll(haciaDelante);
							}
						
						
							next.addAll(sucession);
						
					}
				
					estadoSiguiente.setActual(next);
					DiferenciaNFAN2 =Navega(estadoSiguiente, Salida);
					
				}
				
				return DiferenciaNFAN1+DiferenciaNFAN2;
				
			}
	
	
	public void setNavegacionGenerada(ArrayList<Long> navegacionGenerada) {
		NavegacionGenerada=navegacionGenerada;
		
	}
	
	private List<DocumentsV> calculaTotal(StateNFA value) {
		ArrayList<DocumentsV> Salida=new ArrayList<DocumentsV>();
		Salida.addAll(value.getDocumentosIn());
		for (Entry<Long, List<StateNFA>> entryHijo : value.getTransicion().entrySet()) {
			for (StateNFA stater : entryHijo.getValue()) {

				List<DocumentsV> Posible = calculaTotal(stater);	
				for (DocumentsV documentsV : Posible)
					if (!Salida.contains(documentsV))
						Salida.add(documentsV);
	
			}
			
		}
		return Salida;
	}

}
