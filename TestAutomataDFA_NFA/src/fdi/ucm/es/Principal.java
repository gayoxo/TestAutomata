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
public class Principal {
	
	
	private static final Float _DOWN_LIMIT = 0.01f; //1+_DOWN_LIMIT=f(1)
	public static boolean Debug=true;

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
	private static void Simulation(List<DocumentsV> documentos, String filename) {

		
		 ArrayList<String> LineasSalida=new ArrayList<String>();
	        
		
		 XSSFWorkbook  libro = new XSSFWorkbook ();
		 
		 
//TEST Construccion Absurdo		 
//		 XSSFSheet  Construcion = libro.createSheet("Construccion");
//
//		 
//		int filaI=0;
//		XSSFRow fila = Construcion.createRow(filaI);
//		filaI++;
//		
//		XSSFCell  celda00 = fila.createCell(0);
//		celda00.setCellValue("#");
//		
//		
//		XSSFCell  celda0 = fila.createCell(1);
//		celda0.setCellValue("DFA");
//		
//		XSSFCell  celda1 = fila.createCell(2);
//		celda1.setCellValue("NFA");
//		
//		
//		XSSFCell  celdaHelp = fila.createCell(4);
//		celdaHelp.setCellValue("Tiempor acmulados en la creacion de los nodos");
//			
//		List<Long> TiemposDFA=new ArrayList<Long>();
		
		long StartDFA = System.nanoTime();
		DFAManager DFAObject = 
				new DFAManager(documentos
//						,TiemposDFA
						);
		long EndDFA = System.nanoTime();
		long DiferenciaDFA = EndDFA-StartDFA;
		
		String NodosDFA = "DFA->"+DFAObject.getIdco();
		System.out.println(NodosDFA);
		LineasSalida.add(NodosDFA);
		
		
//		List<Long> TiemposNFA=new ArrayList<Long>();
		
		long StartNFA = System.nanoTime();
		NFAManager NFAObject = 
				new NFAManager(documentos
//						,TiemposNFA
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
		
//TEST Construccion Absurdo			
//		Long DFACT=0l;
//		Long NFACT=0l;
//		
//		for (int i = 0; i < TiemposDFA.size(); i++) {
//			
//			DFACT = TiemposDFA.get(i);
//			
//			if (i<TiemposNFA.size())
//				NFACT = TiemposNFA.get(i);
//			
//			XSSFRow filaN = Construcion.createRow(filaI);
//			filaI++;
//			
//			
//			XSSFCell  celda00N = filaN.createCell(0);
//			celda00N.setCellValue(i);
//			
//			
//			XSSFCell  celda0N = filaN.createCell(1);
//			celda0N.setCellValue(DFACT);
//			
//			XSSFCell  celda1N = filaN.createCell(2);
//			celda1N.setCellValue(NFACT);
//			
//		
//		}
		
		int FilaI2=0;
		XSSFSheet Tiempos = libro.createSheet("Construccion_Navegacion");
		
		XSSFRow filaCN = Tiempos.createRow(FilaI2);
		FilaI2++;
		
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
		
//		XSSFCell  celdaHelpCN = filaCN.createCell(4);
//		celdaHelpCN.setCellValue("Tiempos no-acumulados en la creacion de los nodos");
		
		
		long DiferenciaDFAN=DiferenciaDFA;
		long DiferenciaNFAN=DiferenciaNFA;

		{
			XSSFRow filaN = Tiempos.createRow(FilaI2);
			FilaI2++;
			
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
		
		int Navegaciones=1;
		
		if (!documentos.isEmpty())
			Navegaciones = Math.round(documentos.size()*((1/documentos.size())+_DOWN_LIMIT));
		
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
			
			
			{
				XSSFRow filaN = Tiempos.createRow(FilaI2);
				FilaI2++;
				
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
			
			String BrowsingN = "Browsing->("+i+")   DFA->"+DiferenciaDFANP+" NFA->"+DiferenciaNFANP;
			if (Debug) 
				System.out.println(BrowsingN);
			
			if (!Debug&&i%100==0)
				System.out.println(i);
			
			LineasSalida.add(BrowsingN);

		}
		
		String filerandomvalue = filename+System.nanoTime();
		
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
