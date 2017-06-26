/**
 * 
 */
package fdi.ucm.es;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fdi.ucm.es.dfa.DFAManager;
import fdi.ucm.es.model.DocumentsV;
import fdi.ucm.es.nfa.NFAManager;

/**
 * Clase principal, arranca el sistema, carga el archivo y produce el resultado.
 * @author Joaquin Gayoso Cabada
 *
 */
public class Continuo {
	
	
//private static final int _PARTIDOCONTINUO = 10;
	//	private static final Float _DOWN_LIMIT = 5f; //1+_DOWN_LIMIT=f(1)
	public static boolean Debug=false;
	private static int _PARTIDOCONTINUO = 10;
//	public static boolean Ayuda=false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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

		_PARTIDOCONTINUO=documentosEntrada.size()/50;

		 ArrayList<String> LineasSalida=new ArrayList<String>();
		 
		String Separacion="iteracion cada-> " +_PARTIDOCONTINUO+" documents"; 
		System.out.println(Separacion);
		LineasSalida.add(Separacion);
		
		if (_PARTIDOCONTINUO<1)
			_PARTIDOCONTINUO=1;
		
	
	        
		
		 XSSFWorkbook  libro = new XSSFWorkbook ();
		 
		 
		 
		 List<DocumentsV> documentos=new ArrayList<DocumentsV>();
		 int FilaConstruccionNavegacion=0;
		 int FilaConstruccion=0;
		 int FilaConstruccionDFA=0;
		 int FilaConstruccionNFA=0;
				 
		 XSSFSheet Tiempos = libro.createSheet("Construccion_Navegacion");
			
			XSSFRow filaCN = Tiempos.createRow(FilaConstruccionNavegacion);
			FilaConstruccionNavegacion++;
			
			XSSFCell celda00CN = filaCN.createCell(0);
			celda00CN.setCellValue("#");
			
			
			XSSFCell celda0CN = filaCN.createCell(1);
			celda0CN.setCellValue("DFA");
			
			XSSFCell  celda0CNP = filaCN.createCell(2);
			celda0CNP.setCellValue("DFA+");
			
			XSSFCell  celda1CN = filaCN.createCell(3);
			celda1CN.setCellValue("NFA");
			
			XSSFCell  celda1CNP = filaCN.createCell(4);
			celda1CNP.setCellValue("NFA+");
			
			XSSFCell  celda2CN = filaCN.createCell(5);
			celda2CN.setCellValue("NFA-DFA");
			
			XSSFCell  celda1AC = filaCN.createCell(6);
			celda1AC.setCellValue("ACTION");

			
			
			
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
			celda2filaConstrucccion.setCellValue("NFA-DFA");
			
			XSSFCell celda0filaNodes = filaConstrucccion.createCell(4);
			celda0filaNodes.setCellValue("DFA Nodes");
			
			XSSFCell  celda1filaNodes = filaConstrucccion.createCell(5);
			celda1filaNodes.setCellValue("NFA Nodes");
			
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

		 
		
		for (int ite = 0; ite < documentosEntrada.size(); ite=ite+_PARTIDOCONTINUO) {
			for (int j = 0; j < _PARTIDOCONTINUO && (ite+j) < documentosEntrada.size(); j++) {
				documentos.add(documentosEntrada.get(ite+j));
			}
			
			
			int IteracionDocs=documentos.size();
			
			String CreationIte = "->>>>IterationNumber<<<<--- : "+IteracionDocs;
			
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
			
			
			String Creation0 = "Creation time    DFA->"+DiferenciaDFA+" NFA->"+DiferenciaNFA;
			if (Debug)
				System.out.println(Creation0);
			LineasSalida.add(Creation0);
			
			
			
			long DiferenciaDFAN=DiferenciaDFA;
			long DiferenciaNFAN=DiferenciaNFA;

			if (documentos.size()==documentosEntrada.size())
			{
				XSSFRow filaN = Tiempos.createRow(FilaConstruccionNavegacion);
				FilaConstruccionNavegacion++;
				
				XSSFCell  celda00N = filaN.createCell(0);
				celda00N.setCellValue(0);

				XSSFCell  celda0N = filaN.createCell(1);
				celda0N.setCellValue(DiferenciaDFA);
				
				XSSFCell  celda0NP = filaN.createCell(2);
				celda0NP.setCellValue(DiferenciaDFAN);
				
				XSSFCell  celda1N = filaN.createCell(3);
				celda1N.setCellValue(DiferenciaNFA);
				
				XSSFCell  celda1NP = filaN.createCell(4);
				celda1NP.setCellValue(DiferenciaNFAN);
				
				XSSFCell  celda2N = filaN.createCell(5);
				celda2N.setCellValue(DiferenciaNFA-DiferenciaDFA);
				
				XSSFCell  celda3N = filaN.createCell(6);
				celda3N.setCellValue("CREATION");
				
				
				
				
				
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
				celda2N.setCellValue(DiferenciaNFA-DiferenciaDFA);		
				
				XSSFCell  celda3N = filaN.createCell(4);
				celda3N.setCellValue(DFAObject.getIdco());
				
				
				XSSFCell  celda4N = filaN.createCell(5);
				celda4N.setCellValue(NFAObject.getIdco());
				
			}
			
			int FilaConstruccionDFAColum=0;
			int FilaConstruccionNFAColum=0;
			
			
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
		
			
			
			int Navegaciones=1000;
			if (documentos.size()!=documentosEntrada.size())
				Navegaciones=50;
			
			System.out.println("Se realizan "+Navegaciones+" navegaciones");
			

			
			for (int i = 0; i < Navegaciones; i++) {
				
				ArrayList<Long> NavegacionGenerada=null;
				
				DFAObject.setNavegacionGenerada(NavegacionGenerada);
				
				
				long DiferenciaDFANP=DFAObject.Navega();
				
				DiferenciaDFAN = DiferenciaDFAN+DiferenciaDFANP;
				
				NavegacionGenerada=DFAObject.getNavegacionGenerada();
				
				if (Debug)
					System.out.println(Arrays.toString(NavegacionGenerada.toArray()));

				NFAObject.setNavegacionGenerada(NavegacionGenerada);
				
				
				long DiferenciaNFANP=NFAObject.Navega();
				DiferenciaNFAN = DiferenciaNFAN+DiferenciaNFANP;
				
				if (documentos.size()==documentosEntrada.size())
				{
					XSSFRow filaN = Tiempos.createRow(FilaConstruccionNavegacion);
					FilaConstruccionNavegacion++;
					
					XSSFCell  celda00N = filaN.createCell(0);
					celda00N.setCellValue(i+1);
					
					
					XSSFCell  celda0N = filaN.createCell(1);
					celda0N.setCellValue(DiferenciaDFANP);
					
					XSSFCell  celda0NP = filaN.createCell(2);
					celda0NP.setCellValue(DiferenciaDFAN);
					
					XSSFCell  celda1N = filaN.createCell(3);
					celda1N.setCellValue(DiferenciaNFANP);
					
					XSSFCell  celda1NP = filaN.createCell(4);
					celda1NP.setCellValue(DiferenciaNFAN);
					
					XSSFCell  celda2N = filaN.createCell(5);
					celda2N.setCellValue(DiferenciaNFANP-DiferenciaDFANP);
					
					XSSFCell  celda3N = filaN.createCell(6);
					celda3N.setCellValue("BROWSING");
					
					
				}
				
				
				if (FilaConstruccionDFAColum <= 50)
				{
				XSSFCell  celdaDCDFA = DFATimeSerie.createCell(FilaConstruccionDFAColum);
				celdaDCDFA.setCellValue(DiferenciaDFANP);
				FilaConstruccionDFAColum++;
				
				XSSFCell  celdaNCDFA = NFATimeSerie.createCell(FilaConstruccionNFAColum);
				celdaNCDFA.setCellValue(DiferenciaNFANP);
				FilaConstruccionNFAColum++;
				}
				
				String BrowsingN = "Browsing->("+i+"/"+Navegaciones+")   DFA->"+DiferenciaDFANP+" NFA->"+DiferenciaNFANP;
				if (Debug) 
					System.out.println(BrowsingN);
				
				if (!Debug&&i%100==0)
					System.out.println(i);
				
				LineasSalida.add(BrowsingN);

			}
			
			
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

	/**
	 * Clase que carga el archivo
	 * @param arString
	 * @return
	 */
	private static List<DocumentsV> Load(String pathname) {
		List<DocumentsV> Salida=new ArrayList<DocumentsV>();
		HashMap<Long,Long> Actuales=new HashMap<Long,Long>();
		FileReader fr = null;
		BufferedReader br = null;
		Long idcounter=1l;
		
		try {
			File F=new File(pathname);
			fr = new FileReader(F);
			br = new BufferedReader(fr);
			String linea;
	         while((linea=br.readLine())!=null)
	            {
	        	 DocumentsV DV=new DocumentsV(idcounter.longValue());
	        	 idcounter++;
	        	 if (Debug)
	        		 System.out.println(linea);
	             String[] lineaNE=linea.split(" ");
	             for (String stringatt : lineaNE) {
	            	try {
						Long attr=Long.parseLong(stringatt);
						Long attr2 = Actuales.get(attr);
						if (attr2==null)
							Actuales.put(attr, attr);
						else
							attr=attr2;
						DV.getAtt().add(attr);
					} catch (Exception e) {
						e.printStackTrace();
					} 
	             }
	             Salida.add(DV);
	            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{                    
	            if( fr != null ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
		}
		
		return Salida;
	}

}
