package es.ucm.fdi.ilsa.navigation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class DCollection {
   private List<DObject> objects;
   public DCollection() {
     this.objects = new LinkedList<>();  
   }
   public void addObject(DObject o) {
     objects.add(o);  
   }
   public List<DObject> getObjects() {
     return objects;  
   }
   public void load(Reader in) throws IOException {
     Map<Long,Attribute> as = new HashMap<>();  
     BufferedReader reader = new BufferedReader(in);
     String line;
     do {
       line = reader.readLine();
       if (line != null) {
        // DObject obj = new DObject();  
        // objects.add(obj);
         Set<Attribute> oas = new HashSet<>();
         Scanner s = new Scanner(new StringReader(line));
         while (s.hasNextLong()) {
           long a = s.nextLong();
           Attribute attr = as.get(a);
           if (attr == null) {
              attr = new Attribute(a);
              as.put(a,attr);
           }
           oas.add(attr);
         }
         objects.add(new DObject(oas));
       }
     }
     while (line != null);
   }
 public String toString() {
   return objects.toString(); 
 }
 public static void main(String[] args) throws IOException {
   DCollection c = new DCollection();
   c.load(new FileReader(args[0]));
   //System.out.println(c);
   Automaton a = new Automaton(c);
   //Util.dumpAsDot(a);//a.dumpAsDot();
 }
}   