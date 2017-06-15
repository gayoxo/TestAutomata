package es.ucm.fdi.ilsa.navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class State implements Comparable<State> {
   private List<DObject> objects;
   private Map<Attribute,State> transitions;
   private static long ID=0; 
   private long id;
   public State(List<DObject> objects) {
     this.objects = objects;  
     this.transitions = new HashMap<>();
     id=ID++;
     System.out.println(id);
   }
   public List<DObject> getObjects() {return objects;}
   public Set<Attribute> getTransitions() {
      return transitions.keySet();
   }
   public State transit(Attribute a) {
      return transitions.get(a);
   }
   public void setTransition(Attribute a, State s) {
      transitions.put(a,s); 
   }
   public int compareTo(State s) {
       return  s.objects.size() - objects.size();
   }
   public String toString() {return "%"+id;}
   public long getID() {return id;}
}
