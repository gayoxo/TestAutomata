package fdi.ucm.es.ii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import fdi.ucm.es.Principal;
import fdi.ucm.es.model.DocumentsV;

public class IIManager {
	
	private HashMap<Long, List<DocumentsV>> TablaII;
	private ArrayList<Long> NavegacionGenerada;
	private ArrayList<Integer> ResultadoDocs;
	private int navegacion_actual;

	public IIManager(List<DocumentsV> documentos) {
		TablaII=new HashMap<Long, List<DocumentsV>>();
		for (DocumentsV documentsV : documentos) {
			for (Long att : documentsV.getAtt()) {
				 List<DocumentsV> act=TablaII.get(att);
				 if (act==null)
					 act=new ArrayList<DocumentsV>();
				 if (!act.contains(documentsV))
					 act.add(documentsV);
				 TablaII.put(att, act);
			}
			
		}
	}

	public Long getIdco() {
		return new Long(TablaII.entrySet().size());
	}

	public ArrayList<Long> getNavegacionGenerada() {
		return NavegacionGenerada;
	}

	public void setNavegacionGenerada(ArrayList<Long> navegacionGenerada) {
		NavegacionGenerada = navegacionGenerada;
	}

	public long Navega() {
ArrayList<Long> navegacionGeneradaNueva=new ArrayList<Long>();

navegacion_actual=0;
		
		ResultadoDocs=new ArrayList<>();
		
		Long Salida=Navega(TablaII,navegacionGeneradaNueva);
		
		this.NavegacionGenerada=navegacionGeneradaNueva;
		
		if (Principal.Debug)
			System.out.println("II ->"+Arrays.toString(ResultadoDocs.toArray()));
		
		return Salida;
	}

	private Long Navega(HashMap<Long, List<DocumentsV>> tablaII2, ArrayList<Long> Salida) {
	
		ResultadoDocs.add(CalculaDocs(tablaII2));
		
		
		long StartDFAN1 = System.nanoTime();
		
		Queue<PosibleNodoII> cola = new PriorityQueue<PosibleNodoII>();
		
		for (Entry<Long, List<DocumentsV>> pieza : tablaII2.entrySet()) {
			
			List<DocumentsV> Total=pieza.getValue();
			
			HashMap<Long, List<DocumentsV>> tablaII2Posible=createTabla(tablaII2,Total,pieza.getKey());
			
			PosibleNodoII p=new PosibleNodoII(Total.size(),pieza.getKey(),tablaII2Posible);
			cola.add(p);
			
		}
		
		
		long EndDFAN1 = System.nanoTime();
		long DiferenciaDFAN1 = EndDFAN1-StartDFAN1;
		
		if (Principal.Debug)
			System.out.println("TimeI Ite->"+DiferenciaDFAN1);
		
		HashMap<Long, PosibleNodoII> tablaBusqueda=new HashMap<>();
		for (PosibleNodoII posibleNodoII : cola) 
			tablaBusqueda.put(posibleNodoII.getLongTransicion(), posibleNodoII);
		
		
		
		
		Long DiferenciaNFAN2 =0l;
		
		if (navegacion_actual<NavegacionGenerada.size())
		{
		Long transicionA=NavegacionGenerada.get(navegacion_actual);
		navegacion_actual++;
		
		PosibleNodoII PP=tablaBusqueda.get(transicionA);
		
//		HashSet<StateNFA> next=new HashSet<StateNFA>();
//		
//			for (StateNFA posibleNodoNFA : estadoSiguiente.getActual()) {
//				ArrayList<StateNFA> sucession=new ArrayList<StateNFA>();
//				if (posibleNodoNFA.getBucle().contains(transicionA))
//					sucession.add(posibleNodoNFA);
//				else
//					{
//					List<StateNFA> haciaDelante = posibleNodoNFA.getTransicion().get(transicionA);
//					if (haciaDelante!=null)
//						sucession.addAll(haciaDelante);
//					}
//				
//				
//					next.addAll(sucession);
//				
//			}
//		
//			estadoSiguiente.setActual(new HashSet<>(PP.getEstadoSiguiente()));
			DiferenciaNFAN2 =Navega(PP.getEstadoSiguiente(), Salida);
			
		}
		
		
		if (Principal.Debug)
			System.out.println("TimeI Ite2->"+DiferenciaNFAN2);
		
		
		if (Principal.Debug)
			System.out.println("TimeI IteT->"+DiferenciaDFAN1+DiferenciaNFAN2);
		
		return DiferenciaDFAN1+DiferenciaNFAN2;
	}

	private HashMap<Long, List<DocumentsV>> createTabla(HashMap<Long, List<DocumentsV>> tablaII2,
			List<DocumentsV> total, Long excluir) {
		HashMap<Long, List<DocumentsV>> Salida=new HashMap<>();
		for (Entry<Long, List<DocumentsV>> tablaElement : tablaII2.entrySet())
			if (!tablaElement.getKey().equals(excluir))
			{
				List<DocumentsV> Act=new ArrayList<>(tablaElement.getValue());
				List<DocumentsV> fur=new ArrayList<>();
				for (DocumentsV documentosBase : total) {
					if (Act.contains(documentosBase))
						fur.add(documentosBase);
				}
				
				if (fur.size()!=total.size()&&!fur.isEmpty())
					Salida.put(tablaElement.getKey(), fur);
				
			}
		
		return Salida;
	}

	private Integer CalculaDocs(HashMap<Long, List<DocumentsV>> tablaII2) {
		HashSet<DocumentsV> Salida=new HashSet<>();
		
		for (Entry<Long, List<DocumentsV>> integer : tablaII2.entrySet()) {
			Salida.addAll(integer.getValue());
		}
		
		return Salida.size();
	}


	

}
