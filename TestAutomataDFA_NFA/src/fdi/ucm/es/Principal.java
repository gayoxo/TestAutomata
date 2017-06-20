/**
 * 
 */
package fdi.ucm.es;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import fdi.ucm.es.dfa.DFAManager;
import fdi.ucm.es.model.DocumentsV;
import fdi.ucm.es.nfa.NFAManager;

/**
 * Clase principal, arranca el sistema, carga el archivo y produce el resultado.
 * @author Joaquin Gayoso Cabada
 *
 */
public class Principal {

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
		Simulation(Documentos);
		long End = System.nanoTime();
		long Diferencia = End-Start;
		System.out.println("Simulation End time->"+Diferencia);
	}

	/**
	 * Proceso de simulacion
	 * @param documentos
	 */
	private static void Simulation(List<DocumentsV> documentos) {

		
		HSSFWorkbook libro = new HSSFWorkbook();
		HSSFSheet Construcion = libro.createSheet("Construccion");
		int filaI=0;
		HSSFRow fila = Construcion.createRow(filaI);
		filaI++;
		
		HSSFCell celda00 = fila.createCell(0);
		celda00.setCellValue("#");
		
		
		HSSFCell celda0 = fila.createCell(1);
		celda0.setCellValue("DFA");
		
		HSSFCell celda1 = fila.createCell(2);
		celda1.setCellValue("NFA");
		
		
		List<Long> TiemposDFA=new ArrayList<Long>();
		
		long StartDFA = System.nanoTime();
		DFAManager DFAObject = 
				new DFAManager(documentos,TiemposDFA);
		long EndDFA = System.nanoTime();
		long DiferenciaDFA = EndDFA-StartDFA;
		
		System.out.println("DFA->"+DFAObject.getIdco());
		
		
		List<Long> TiemposNFA=new ArrayList<Long>();
		
		long StartNFA = System.nanoTime();
		NFAManager NFAObject = 
				new NFAManager(documentos,TiemposNFA);
		long EndNFA = System.nanoTime();
		long DiferenciaNFA = EndNFA-StartNFA;
		
		System.out.println("NFA->"+NFAObject.getIdco());
		
		System.out.println("Creation time    DFA->"+DiferenciaDFA+" NFA->"+DiferenciaNFA);
		
		Long DFACT=0l;
		Long NFACT=0l;
		
		for (int i = 0; i < TiemposDFA.size(); i++) {
			
			DFACT = TiemposDFA.get(i);
			
			if (i<TiemposNFA.size())
				NFACT = TiemposNFA.get(i);
			
			HSSFRow filaN = Construcion.createRow(filaI);
			filaI++;
			
			
			HSSFCell celda00N = filaN.createCell(0);
			celda00N.setCellValue(i);
			
			
			HSSFCell celda0N = filaN.createCell(1);
			celda0N.setCellValue(DFACT);
			
			HSSFCell celda1N = filaN.createCell(2);
			celda1N.setCellValue(NFACT);
			
		
		}
		
		HSSFSheet Tiempos = libro.createSheet("Construccion_Navegacion");
		
		HSSFRow filaCN = Tiempos.createRow(filaI);
		filaI++;
		
		HSSFCell celda00CN = filaCN.createCell(0);
		celda00CN.setCellValue("#");
		
		
		HSSFCell celda0CN = filaCN.createCell(1);
		celda0CN.setCellValue("DFA");
		
		HSSFCell celda1CN = filaCN.createCell(2);
		celda1CN.setCellValue("NFA");
		

		{
			HSSFRow filaN = Construcion.createRow(filaI);
			filaI++;
			
			HSSFCell celda00N = filaN.createCell(0);
			celda00N.setCellValue(0);
			
			
			HSSFCell celda0N = filaN.createCell(1);
			celda0N.setCellValue(DiferenciaDFA);
			
			HSSFCell celda1N = filaN.createCell(2);
			celda1N.setCellValue(DiferenciaNFA);
			
		}
		
		
		
		int Navegaciones = documentos.size()*1;
		
		long StartDFAN = System.nanoTime();
		long StartNFAN = System.nanoTime();
		
		for (int i = 0; i < Navegaciones; i++) {
			
			ArrayList<Long> NavegacionGenerada=null;
			
			DFAObject.setNavegacionGenerada(NavegacionGenerada);
			
			
			NavegacionGenerada=DFAObject.Navega();
			long EndDFAN = System.nanoTime();
			long DiferenciaDFAN = EndDFAN-StartDFAN;
			
			System.out.println(Arrays.toString(NavegacionGenerada.toArray()));

			NFAObject.setNavegacionGenerada(NavegacionGenerada);
			
			
			NavegacionGenerada=NFAObject.Navega();
			long EndNFAN = System.nanoTime();
			long DiferenciaNFAN = EndNFAN-StartNFAN;
			
			
			{
				HSSFRow filaN = Construcion.createRow(filaI);
				filaI++;
				
				HSSFCell celda00N = filaN.createCell(0);
				celda00N.setCellValue(i+1);
				
				
				HSSFCell celda0N = filaN.createCell(1);
				celda0N.setCellValue(DiferenciaDFAN);
				
				HSSFCell celda1N = filaN.createCell(2);
				celda1N.setCellValue(DiferenciaNFAN);
				
			}
			
			System.out.println("Browsing->("+i+")   DFA->"+DiferenciaDFAN+" NFA->"+DiferenciaNFAN);
		}
		
		
		try {
			   FileOutputStream elFichero = new FileOutputStream(System.nanoTime()+".xls");
			   libro.write(elFichero);
			   elFichero.close();
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
