package es.ucm.fdi.ilsa.navigation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IIndex extends HashMap<Attribute,List<DObject>>  {
       private int numberOfObjects;
       private Set<Attribute> loop;
       private List<Attribute> outgoing;
       public IIndex(List<DObject> objects) {
         for(DObject o: objects) {
            for(Attribute a: o.getAttributes()) {
               List<DObject> os = get(a);
               if (os == null){ 
                   os = new LinkedList<DObject>();
                   put(a,os);
               }
               os.add(o);
            } 
         }
         
         this.numberOfObjects = objects.size();
         loop=null;
         outgoing=null;
       }  
       public List<Attribute> getOutgoing() {
          if(outgoing == null) {
            outgoing = new LinkedList<>();
            for(Map.Entry<Attribute,List<DObject>> e: entrySet()) 
               if (e.getValue().size() != numberOfObjects)
                  outgoing.add(e.getKey());
          }
         return outgoing;
       }
       public Set<Attribute> getLoop() {
          if(loop == null) {
            loop = new HashSet<>();
            for(Map.Entry<Attribute,List<DObject>> e: entrySet()) 
               if (e.getValue().size() == numberOfObjects)
                  loop.add(e.getKey());
          }
         return loop;
       }
   }
