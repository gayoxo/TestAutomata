package fdi.ucm.es.ii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import fdi.ucm.es.VariablesEstaticas;
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
		
		Long Salida=Navega(new ArrayList<Long>(),navegacionGeneradaNueva);
		
		this.NavegacionGenerada=navegacionGeneradaNueva;
		
		if (VariablesEstaticas.Debug)
			System.out.println("II ->"+Arrays.toString(ResultadoDocs.toArray()));
		
		return Salida;
	}

	private Long Navega(ArrayList<Long> select, ArrayList<Long> Salida) {
	
		

		long StartDFAN1 = System.nanoTime();

		List<DocumentsV> Documentos=CalculaDocs(TablaII,select);
		
		ResultadoDocs.add(Documentos.size());
		
		Queue<PosibleNodoII> cola = new PriorityQueue<PosibleNodoII>();
		
		for (Long pieza : TablaII.keySet()) {
			
			if (!Salida.contains(pieza))
			{
				
				ArrayList<Long> nueva=new ArrayList<>(select);
				nueva.add(pieza);
			
			List<DocumentsV> Intersect=CalculaDocs(TablaII,nueva);
			
				if (!Intersect.isEmpty()&&Intersect.size()<Documentos.size())
				{
				PosibleNodoII p=new PosibleNodoII(Intersect.size(),pieza);
				cola.add(p);
				}
			}
			
		}
		
		
		long EndDFAN1 = System.nanoTime();
		long DiferenciaDFAN1 = EndDFAN1-StartDFAN1;
		
		if (VariablesEstaticas.DebugTiming)
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
			select.add(PP.getLongTransicion());
			DiferenciaNFAN2 =Navega(select, Salida);
			
		}
		
		
		if (VariablesEstaticas.DebugTiming)
			System.out.println("TimeI Ite2->"+DiferenciaNFAN2);
		
		
		if (VariablesEstaticas.DebugTiming)
			System.out.println("TimeI IteT->"+DiferenciaDFAN1+DiferenciaNFAN2);
		
		return DiferenciaDFAN1+DiferenciaNFAN2;
	}

	private List<DocumentsV> intersec(List<DocumentsV> total,
			List<DocumentsV> documentos) {
		List<DocumentsV> List1=total;
		List<DocumentsV> List2=documentos;
		if (List1.size()>List2.size())
			{
			List<DocumentsV> TMP=List1;
			List1=List2;
			List2=TMP;
			}
		
		List<DocumentsV> ListSalida=new ArrayList<>();
		
		for (DocumentsV documentsV : List1) {
			if (List2.contains(documentsV))
				ListSalida.add(documentsV);
		}
		
		return ListSalida;
	}

	

	private List<DocumentsV> CalculaDocs(HashMap<Long, List<DocumentsV>> tablaII2, ArrayList<Long> select) {
		
		
		ArrayList<List<DocumentsV>> ListasInter=new ArrayList<>();
		
		
		List<DocumentsV> Salida=null;
		
		if (!select.isEmpty())
			{
			for (Long selectedLong : select) {
				List<DocumentsV> act=tablaII2.get(selectedLong);
				if (act!=null)
					ListasInter.add(act);
			}
			for (List<DocumentsV> documentsV : ListasInter) {
				if (Salida==null)
					Salida=documentsV;
				else
					Salida=intersec(Salida, documentsV);
					
			}
			
			}
		else
			{
			
			HashSet<DocumentsV> SalidaHa=new HashSet<>();
			for (Entry<Long, List<DocumentsV>> documentsV : tablaII2.entrySet()) 
				SalidaHa.addAll(documentsV.getValue());
			
			Salida=new ArrayList<>(SalidaHa);
			}
		
		
		
		
		
		
		return Salida;
	}


	

}
