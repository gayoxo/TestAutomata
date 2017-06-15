package es.ucm.fdi.ilsa.navigation;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Automaton {
   
      private static class ProtoState implements Comparable<ProtoState> {
       private IIndex index;
       private State state;
       public ProtoState(IIndex index,State state) {
          this.index = index;
          this.state = state;
       }
       public IIndex getIndex() {
           return index;
       }
       public State getState() {
           return state;
       }
       public int compareTo(ProtoState s) {
         return state.compareTo(s.state);  
       }
       public String toString() {
         return state.toString();
       }
   }
     

   
   private State initialState;
   public State getInitialState() {
       return initialState;
   }
   public Automaton(DCollection collection) {
     build(collection);
   }
   
   private void build(DCollection collection) {
     PriorityQueue<ProtoState> pending = new PriorityQueue<>();
     Map<Set<Attribute>,State> existing = new HashMap<>();
     this.initialState = new State(collection.getObjects());
     pending.add(new ProtoState(new IIndex(collection.getObjects()),this.initialState));
     while (! pending.isEmpty()) {
         ProtoState currentPS = pending.poll();
         existing.remove(currentPS.getState());
         for(Attribute a: currentPS.getIndex().getLoop()) {
             currentPS.getState().setTransition(a,currentPS.getState());
         }
         for(Attribute a: currentPS.getIndex().getOutgoing()) {
             IIndex targetI = new IIndex(currentPS.getIndex().get(a));
             State targetS = existing.get(targetI.getLoop());
             if (targetS == null) {
                targetS = new State(currentPS.getIndex().get(a));
                pending.add(new ProtoState(targetI,targetS));
                existing.put(targetI.getLoop(),targetS);
             }
             currentPS.getState().setTransition(a,targetS);
         }
     }
   } 
}
