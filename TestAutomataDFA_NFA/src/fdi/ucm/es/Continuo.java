/**
 * 
 */
package fdi.ucm.es;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fdi.ucm.es.dfa.DFAManager;
import fdi.ucm.es.ii.IIManager;
import fdi.ucm.es.model.DocumentsV;
import fdi.ucm.es.nfa.NFAManager;

/**
 * Clase principal, arranca el sistema, carga el archivo y produce el resultado.
 * @author Joaquin Gayoso Cabada
 *
 */
public class Continuo extends Principal{
	
	

	private static final int _PARTIDO_CONTINUO_TOTAL = 50;
	private static int _PARTIDOCONTINUO = 10;


	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		DebugTiming=Debug&&DebugExtra;
		System.out.println(Arrays.toString(args));
		System.out.println("Loading File");
		if (args.length==0)
			System.exit(1);
		String arString=args[0];
		List<DocumentsV> Documentos=Load(arString);
		System.out.println("File Loaded");
		Collections.shuffle(Documentos);
		long Start = System.nanoTime();	
		Simulation(Documentos,arString);
		long End = System.nanoTime();
		long Diferencia = End-Start;
		System.out.println("Simulation End time->"+Diferencia);
	}

	/**
	 * Proceso de simulacion
	 * @param documentos
	 * @param filename 
	 */
	private static void Simulation(List<DocumentsV> documentosEntrada, String filename) {

		_PARTIDOCONTINUO=documentosEntrada.size()/_PARTIDO_CONTINUO_TOTAL;

		 ArrayList<String> LineasSalida=new ArrayList<String>();

		if (_PARTIDOCONTINUO<1)
			_PARTIDOCONTINUO=1;
		
		String Separacion="iteracion cada-> " +_PARTIDOCONTINUO+" documents"; 
		System.out.println(Separacion);
		LineasSalida.add(Separacion);
	        
		
		 XSSFWorkbook  libro = new XSSFWorkbook ();
		 
		 
		 
		 List<DocumentsV> documentos=new ArrayList<DocumentsV>();
		 int FilaConstruccionNavegacion=0;
		 int FilaConstruccion=0;
		 int FilaConstruccionDFA=0;
		 int FilaConstruccionNFA=0;
		 int FilaConstruccionII=0;
				 
		 XSSFSheet Tiempos = libro.createSheet("Construccion_Navegacion");
			
			XSSFRow filaCN = Tiempos.createRow(FilaConstruccionNavegacion);
			FilaConstruccionNavegacion++;
			
			XSSFCell celda00CN = filaCN.createCell(0);
			celda00CN.setCellValue("#");
			
			XSSFCell  celda1AC = filaCN.createCell(1);
			celda1AC.setCellValue("ACTION");
			
			XSSFCell celda0CN = filaCN.createCell(2);
			celda0CN.setCellValue("DFA");
			
			XSSFCell  celda0CNP = filaCN.createCell(3);
			celda0CNP.setCellValue("DFA+");
			
			XSSFCell  celda1CN = filaCN.createCell(4);
			celda1CN.setCellValue("NFA");
			
			XSSFCell  celda1CNP = filaCN.createCell(5);
			celda1CNP.setCellValue("NFA+");
			
			XSSFCell  celda2CI = filaCN.createCell(6);
			celda2CI.setCellValue("II");
			
			XSSFCell  celda2CIP = filaCN.createCell(7);
			celda2CIP.setCellValue("II+");
			
			XSSFCell  celda3ND = filaCN.createCell(8);
			celda3ND.setCellValue("NFA-DFA");
			
			XSSFCell  celda3NI = filaCN.createCell(9);
			celda3NI.setCellValue("NFA-II");
			
			XSSFCell  celda3DI = filaCN.createCell(10);
			celda3DI.setCellValue("DFA-II");
			
			

			
			
			
			XSSFSheet Tiempos_Construccion = libro.createSheet("Construccion");
			
			XSSFRow filaConstrucccion = Tiempos_Construccion.createRow(FilaConstruccion);
			FilaConstruccion++;
			
			XSSFCell celda00filaConstrucccion = filaConstrucccion.createCell(0);
			celda00filaConstrucccion.setCellValue("NDOCS");
			
			
			XSSFCell celda0filaConstrucccion = filaConstrucccion.createCell(1);
			celda0filaConstrucccion.setCellValue("DFA");
			
			XSSFCell  celda1filaConstrucccion = filaConstrucccion.createCell(2);
			celda1filaConstrucccion.setCellValue("NFA");
			
			XSSFCell  celda2filaConstrucccion = filaConstrucccion.createCell(3);
			celda2filaConstrucccion.setCellValue("II");

			XSSFCell celda0filaNodes = filaConstrucccion.createCell(4);
			celda0filaNodes.setCellValue("DFA Nodes");
			
			XSSFCell  celda1filaNodes = filaConstrucccion.createCell(5);
			celda1filaNodes.setCellValue("NFA Nodes");
			
			XSSFCell  celda2filaNodes = filaConstrucccion.createCell(6);
			celda2filaNodes.setCellValue("II Size");
			
			XSSFCell  celda0Diferencial = filaConstrucccion.createCell(7);
			celda0Diferencial.setCellValue("NFA-DFA");
			
			XSSFCell  celda1Diferencial = filaConstrucccion.createCell(8);
			celda1Diferencial.setCellValue("NFA-II");
			
			XSSFCell  celda2Diferencial = filaConstrucccion.createCell(9);
			celda2Diferencial.setCellValue("DFA-II");
			
			XSSFSheet Tiempos_ConstruccionDFA = libro.createSheet("NavegacionDFA");
			
			{
				XSSFRow filaConstrucccionDFA = Tiempos_ConstruccionDFA.createRow(FilaConstruccionDFA);
				FilaConstruccionDFA++;
				
				XSSFCell celda00filaConstrucccionDFA = filaConstrucccionDFA.createCell(0);
				celda00filaConstrucccionDFA.setCellValue("NDOCS\\Navegacion");
				
				for (int i = 0; i < 50; i++) {
					XSSFCell celda0filaConstrucccionDFA = filaConstrucccionDFA.createCell(i+1);
					celda0filaConstrucccionDFA.setCellValue(i+1);
				}
				
			}
			
			XSSFSheet Tiempos_ConstruccionNFA = libro.createSheet("NavegacionNFA");
			
			{
				XSSFRow filaConstrucccionNFA = Tiempos_ConstruccionNFA.createRow(FilaConstruccionNFA);
				FilaConstruccionNFA++;
				
				XSSFCell celda00filaConstrucccionNFA = filaConstrucccionNFA.createCell(0);
				celda00filaConstrucccionNFA.setCellValue("NDOCS\\Navegacion");
				
				for (int i = 0; i < 50; i++) {
					XSSFCell celda0filaConstrucccionNFA = filaConstrucccionNFA.createCell(i+1);
					celda0filaConstrucccionNFA.setCellValue(i+1);
				}
				
			}
			
			XSSFSheet Tiempos_ConstruccionII = libro.createSheet("NavegacionII");
			
			{
				XSSFRow filaConstrucccionII = Tiempos_ConstruccionII.createRow(FilaConstruccionII);
				FilaConstruccionII++;
				
				XSSFCell celda00filaConstrucccionII = filaConstrucccionII.createCell(0);
				celda00filaConstrucccionII.setCellValue("NDOCS\\Navegacion");
				
				for (int i = 0; i < 50; i++) {
					XSSFCell celda0filaConstrucccionII = filaConstrucccionII.createCell(i+1);
					celda0filaConstrucccionII.setCellValue(i+1);
				}
				
			}

		 
		
		for (int ite = 0; ite < documentosEntrada.size(); ite=ite+_PARTIDOCONTINUO) {
			for (int j = 0; j < _PARTIDOCONTINUO && (ite+j) < documentosEntrada.size(); j++) {
				documentos.add(documentosEntrada.get(ite+j));
			}
			
			
			int IteracionDocs=documentos.size();
			
			String CreationIte = "->>>>IterationNumber<<<<--- : "+IteracionDocs+"/"+documentosEntrada.size();
			
				System.out.println(CreationIte);
			
			LineasSalida.add(CreationIte);
			
			long StartDFA = System.nanoTime();
			DFAManager DFAObject = 
					new DFAManager(documentos
//							,TiemposDFA
							);
			long EndDFA = System.nanoTime();
			long DiferenciaDFA = EndDFA-StartDFA;
			
			String NodosDFA = "DFA->"+DFAObject.getIdco();
			System.out.println(NodosDFA);
			LineasSalida.add(NodosDFA);
			
			
//			List<Long> TiemposNFA=new ArrayList<Long>();
			
			long StartNFA = System.nanoTime();
			NFAManager NFAObject = 
					new NFAManager(documentos
//							,TiemposNFA
							);
			long EndNFA = System.nanoTime();
			long DiferenciaNFA = EndNFA-StartNFA;
			
			String NodosNFA = "NFA->"+NFAObject.getIdco();
			System.out.println(NodosNFA);
			LineasSalida.add(NodosNFA);
			
			
			long StartII = System.nanoTime();
			IIManager IIObject = 
					new IIManager(documentos
//							,TiemposNFA
							);
			long EndII = System.nanoTime();
			long DiferenciaII = EndII-StartII;
			
			String NodosII = "II->"+IIObject.getIdco();
			System.out.println(NodosII);
			LineasSalida.add(NodosII);
			
			
			String Creation0 = "Creation time    DFA->"+DiferenciaDFA+" NFA->"+DiferenciaNFA+" II->"+DiferenciaII;
			if (Debug)
				System.out.println(Creation0);
			LineasSalida.add(Creation0);
			
			
			
			long DiferenciaDFAN=DiferenciaDFA;
			long DiferenciaNFAN=DiferenciaNFA;
			long DiferenciaIIN=DiferenciaII;

			if (documentos.size()==documentosEntrada.size())
			{
				XSSFRow filaN = Tiempos.createRow(FilaConstruccionNavegacion);
				FilaConstruccionNavegacion++;
				
				XSSFCell  celda00N = filaN.createCell(0);
				celda00N.setCellValue(0);
				
				XSSFCell  celdaCC = filaN.createCell(1);
				celdaCC.setCellValue("CREATION");

				XSSFCell  celda0N = filaN.createCell(2);
				celda0N.setCellValue(DiferenciaDFA);
				
				XSSFCell  celda0NP = filaN.createCell(3);
				celda0NP.setCellValue(DiferenciaDFAN);
				
				XSSFCell  celda1N = filaN.createCell(4);
				celda1N.setCellValue(DiferenciaNFA);
				
				XSSFCell  celda1NP = filaN.createCell(5);
				celda1NP.setCellValue(DiferenciaNFAN);
				
				XSSFCell  celda2I = filaN.createCell(6);
				celda2I.setCellValue(DiferenciaII);
				
				XSSFCell  celda2IN = filaN.createCell(7);
				celda2IN.setCellValue(DiferenciaIIN);
				
				XSSFCell  celda3CND = filaN.createCell(8);
				celda3CND.setCellValue(DiferenciaNFA-DiferenciaDFA);
				
				XSSFCell  celda3CNI = filaN.createCell(9);
				celda3CNI.setCellValue(DiferenciaNFA-DiferenciaII);
				
				XSSFCell  celda3CDI = filaN.createCell(10);
				celda3CDI.setCellValue(DiferenciaDFA-DiferenciaII);		
			}
			
			{
				XSSFRow filaN = Tiempos_Construccion.createRow(FilaConstruccion);
				FilaConstruccion++;
				
				XSSFCell  celda00N = filaN.createCell(0);
				celda00N.setCellValue(IteracionDocs);

				XSSFCell  celda0N = filaN.createCell(1);
				celda0N.setCellValue(DiferenciaDFA);
				
				
				XSSFCell  celda1N = filaN.createCell(2);
				celda1N.setCellValue(DiferenciaNFA);
				
				XSSFCell  celda2N = filaN.createCell(3);
				celda2N.setCellValue(DiferenciaII);
						
				XSSFCell  celda0DT = filaN.createCell(4);
				celda0DT.setCellValue(DFAObject.getIdco());
								
				XSSFCell  celda1DT = filaN.createCell(5);
				celda1DT.setCellValue(NFAObject.getIdco());
				
				XSSFCell  celda2DT = filaN.createCell(6);
				celda2DT.setCellValue(IIObject.getIdco());
				
				XSSFCell  celda0DND = filaN.createCell(7);
				celda0DND.setCellValue(DiferenciaNFA-DiferenciaDFA);	
				
				XSSFCell  celda1DND = filaN.createCell(8);
				celda1DND.setCellValue(DiferenciaNFA-DiferenciaII);
				
				XSSFCell  celda2DND = filaN.createCell(9);
				celda2DND.setCellValue(DiferenciaDFA-DiferenciaII);
				
			}
			
			int FilaConstruccionDFAColum=0;
			int FilaConstruccionNFAColum=0;
			int FilaConstruccionIIColum=0;
			
			
			XSSFRow DFATimeSerie = Tiempos_ConstruccionDFA.createRow(FilaConstruccionDFA);
			FilaConstruccionDFA++;
			
			XSSFCell  celda0CFDA = DFATimeSerie.createCell(FilaConstruccionDFAColum);
			celda0CFDA.setCellValue(IteracionDocs);
			FilaConstruccionDFAColum++;
		
			
			
			XSSFRow NFATimeSerie = Tiempos_ConstruccionNFA.createRow(FilaConstruccionNFA);
			FilaConstruccionNFA++;
			
			XSSFCell  celda0NFDA = NFATimeSerie.createCell(FilaConstruccionNFAColum);
			celda0NFDA.setCellValue(IteracionDocs);
			FilaConstruccionNFAColum++;
			
			
			XSSFRow IITimeSerie = Tiempos_ConstruccionII.createRow(FilaConstruccionII);
			FilaConstruccionII++;
			
			XSSFCell  celda0IIA = IITimeSerie.createCell(FilaConstruccionIIColum);
			celda0IIA.setCellValue(IteracionDocs);
			FilaConstruccionIIColum++;
		
			
			
			int Navegaciones=1000;
			if (documentos.size()!=documentosEntrada.size())
				Navegaciones=50;
			else
				{
				System.out.println("Se realizan "+Navegaciones+" navegaciones");
				if (Debug)
					System.out.println("Navegaciones Finales");
				}
			
				
			
			for (int i = 0; i < Navegaciones; i++) {
				
				ArrayList<Long> NavegacionGenerada=null;
				
				DFAObject.setNavegacionGenerada(NavegacionGenerada);
				
				
				long DiferenciaDFANP=DFAObject.Navega();
				
				DiferenciaDFAN = DiferenciaDFAN+DiferenciaDFANP;
				
				NavegacionGenerada=DFAObject.getNavegacionGenerada();
				
				

				NFAObject.setNavegacionGenerada(NavegacionGenerada);
				
				
				long DiferenciaNFANP=NFAObject.Navega();
				DiferenciaNFAN = DiferenciaNFAN+DiferenciaNFANP;
				
				
				IIObject.setNavegacionGenerada(NavegacionGenerada);
				
				long DiferenciaNIIP=IIObject.Navega();
				DiferenciaIIN = DiferenciaIIN+DiferenciaNIIP;
				
				
				if (Debug)
					System.out.println(Arrays.toString(NavegacionGenerada.toArray()));
				
				
				if (documentos.size()==documentosEntrada.size())
				{
					XSSFRow filaN = Tiempos.createRow(FilaConstruccionNavegacion);
					FilaConstruccionNavegacion++;
					
					XSSFCell  celda00N = filaN.createCell(0);
					celda00N.setCellValue(i+1);
					
					XSSFCell  celda3N = filaN.createCell(1);
					celda3N.setCellValue("BROWSING");
					
					XSSFCell  celda0N = filaN.createCell(2);
					celda0N.setCellValue(DiferenciaDFANP);
					
					XSSFCell  celda0NP = filaN.createCell(3);
					celda0NP.setCellValue(DiferenciaDFAN);
					
					XSSFCell  celda1N = filaN.createCell(4);
					celda1N.setCellValue(DiferenciaNFANP);
					
					XSSFCell  celda1NP = filaN.createCell(5);
					celda1NP.setCellValue(DiferenciaNFAN);
					
					XSSFCell  celda2N = filaN.createCell(6);
					celda2N.setCellValue(DiferenciaNIIP);
					
					XSSFCell  celda2NP = filaN.createCell(7);
					celda2NP.setCellValue(DiferenciaIIN);
					
					XSSFCell  celda0DND = filaN.createCell(8);
					celda0DND.setCellValue(DiferenciaNFANP-DiferenciaDFANP);
					
					XSSFCell  celda1DNI = filaN.createCell(9);
					celda1DNI.setCellValue(DiferenciaNFANP-DiferenciaNIIP);
					
					XSSFCell  celda2DDI = filaN.createCell(10);
					celda2DDI.setCellValue(DiferenciaDFANP-DiferenciaNIIP);
					
					
					
					
				}
				
				
				if (FilaConstruccionDFAColum <= 50)
				{
				XSSFCell  celdaDCDFA = DFATimeSerie.createCell(FilaConstruccionDFAColum);
				celdaDCDFA.setCellValue(DiferenciaDFANP);
				FilaConstruccionDFAColum++;
				
				XSSFCell  celdaNCDFA = NFATimeSerie.createCell(FilaConstruccionNFAColum);
				celdaNCDFA.setCellValue(DiferenciaNFANP);
				FilaConstruccionNFAColum++;
				
				XSSFCell  celdaIIA = IITimeSerie.createCell(FilaConstruccionIIColum);
				celdaIIA.setCellValue(DiferenciaNIIP);
				FilaConstruccionIIColum++;
				}
				
				String BrowsingN = "Browsing->("+i+"/"+Navegaciones+")   DFA->"+DiferenciaDFANP+" NFA->"+DiferenciaNFANP+" II->"+DiferenciaNIIP ;
				if (Debug) 
					System.out.println(BrowsingN);
				
				if (!Debug) {
					if (i%100==0 && documentos.size()==documentosEntrada.size())
						System.out.println(i);
				}
				
				LineasSalida.add(BrowsingN);

			}
			
			
		}		 
		 
		int FilaConstruccionNFA50 = 0;
		 XSSFSheet TiemposNFA50 = libro.createSheet("Construccion_NFA50");
			
			XSSFRow filaCN50 = TiemposNFA50.createRow(FilaConstruccionNFA50);
			FilaConstruccionNFA50++;
			
			XSSFCell celda00CN50 = filaCN50.createCell(0);
			celda00CN50.setCellValue("#");
			
			
			XSSFCell celda0CN50 = filaCN50.createCell(1);
			celda0CN50.setCellValue("NFANodes");
			
			XSSFCell celda0CN50TM = filaCN50.createCell(2);
			celda0CN50TM.setCellValue("Time");
		 
		for (int j = 0; j < 50; j++) {
			
			System.out.println("Simulacion NFA -> ite: " +j);
			
			Collections.shuffle(documentosEntrada);
			
			long StartNFA = System.nanoTime();
			NFAManager NFAObject = 
					new NFAManager(documentosEntrada
//							,TiemposNFA
							);
			long EndNFA = System.nanoTime();
			long DiferenciaNFA = EndNFA-StartNFA;
			
			Long NodosNFA = NFAObject.getIdco();
			
			XSSFRow filaCN50N = TiemposNFA50.createRow(FilaConstruccionNFA50);
			FilaConstruccionNFA50++;
			
			XSSFCell celda00CN50N = filaCN50N.createCell(0);
			celda00CN50N.setCellValue(j);
			
			
			XSSFCell celda0CN50N = filaCN50N.createCell(1);
			celda0CN50N.setCellValue(NodosNFA);
			
			XSSFCell celda0CN50TMN = filaCN50N.createCell(2);
			celda0CN50TMN.setCellValue(DiferenciaNFA);
			
		}	
			
		
	
		
		String filerandomvalue = filename+"_CONTINUO_"+System.nanoTime();
		
		try {
			   FileOutputStream elFichero = new FileOutputStream(filerandomvalue+".xlsx");
			   libro.write(elFichero);
			   elFichero.close();
			} catch (Exception e) {
			   e.printStackTrace();
			}
		
		try {
			 String ruta = filerandomvalue+".txt";
		        File archivo = new File(ruta);
		        BufferedWriter bw;
		        bw = new BufferedWriter(new FileWriter(archivo));
		        for (String cell : LineasSalida) {
		        	 bw.write(cell+"\n");
				}
//		        if(archivo.exists()) {
//		            bw = new BufferedWriter(new FileWriter(archivo));
//		            bw.write("El fichero de texto ya estaba creado.");
//		        } else {
//		            bw = new BufferedWriter(new FileWriter(archivo));
//		            bw.write("Acabo de crear el fichero de texto.");
//		        }
		        bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	

}
