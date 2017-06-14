/**
 * 
 */
package fdi.ucm.es;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import fdi.ucm.es.dfa.DFAManager;
import fdi.ucm.es.model.DocumentsV;

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

		long Start = System.nanoTime();
//		DFAManager DFAObject = 
				new DFAManager(documentos);
		long End = System.nanoTime();
		long Diferencia = End-Start;
		System.out.println("Creation time->"+Diferencia);
		
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
