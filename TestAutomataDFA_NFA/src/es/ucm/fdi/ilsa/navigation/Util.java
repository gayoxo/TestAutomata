package es.ucm.fdi.ilsa.navigation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Util {
  public static void dumpAsDot(Automaton automaton) {
     State initialState = automaton.getInitialState();
     List<State> pending = new LinkedList<>();
     Set<State> expanded = new HashSet<>();
     pending.add(initialState);
     expanded.add(initialState);
     System.out.println("digraph G {");
     while (! pending.isEmpty()) {
       State s = pending.remove(0);
       for(Attribute a: s.getTransitions()) {
         State next = s.transit(a);
         System.out.println("\""+s+"{"+s.getObjects()+"}"+"\" -> \""+next+"{"+next.getObjects()+"}"+"\" [label=\""+a+"\"]");
         if (! expanded.contains(next)) {
             expanded.add(next);
             pending.add(next);
         }    
       }
     }
    System.out.println("}");                
  }  
  public static void dumpAsDot(NDAutomaton automaton) {
     NDState initialState = automaton.getInitialState();
     List<NDState> pending = new LinkedList<>();
     Set<NDState> expanded = new HashSet<>();
     pending.add(initialState);
     expanded.add(initialState);
     System.out.println("digraph G {");
     while (! pending.isEmpty()) {
       NDState s = pending.remove(0);
       for(Attribute a: s.getTransitions()) {
         List<NDState> nextStates = s.transit(a);
         for(NDState next: nextStates) {
           System.out.println("\""+s+"{"+s.getObjects()+"}"+"\" -> \""+next+"{"+next.getObjects()+"}"+"\" [label=\""+a+"\"]");
           if (! expanded.contains(next)) {
              expanded.add(next);
              pending.add(next);
          } 
         }   
       }
     }
    System.out.println("}");                
  }  
}
