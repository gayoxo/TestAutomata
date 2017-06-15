package es.ucm.fdi.ilsa.navigation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NDState  {
   private List<DObject> objects;
   private Map<Attribute,List<NDState>> transitions;
   private static long ID=0; 
   private long id;
   public NDState(List<DObject> objects) {
     this.objects = objects;  
     this.transitions = new HashMap<>();
     id=ID++;
     System.out.println(id);
   }
   public List<DObject> getObjects() {return objects;}
   public Set<Attribute> getTransitions() {
      return transitions.keySet();
   }
   public List<NDState> transit(Attribute a) {
      return transitions.get(a);
   }
   public void addTransition(Attribute a, NDState s) {
      List<NDState> targets = transitions.get(a);
      if (targets == null) {
          targets = new LinkedList<>();
          transitions.put(a,targets); 
      }
      targets.add(s);
   }
   public String toString() {return "%"+id;}
   public long getID() {return id;}
}
