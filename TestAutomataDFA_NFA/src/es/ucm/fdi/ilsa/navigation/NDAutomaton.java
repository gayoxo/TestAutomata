package es.ucm.fdi.ilsa.navigation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NDAutomaton {
   
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
     
   private static class BuildContext {
     private NDState state;
     private BuildContext parent;
     private Attribute transition;
     public BuildContext(NDState state, BuildContext parent, Attribute transition) {
         this.state = state;
         this.parent = parent;
         this.transition = transition;
     }
     public BuildContext(NDState state) {
         this(state,null, null);
     }
     public NDState getState() {return state;}
     public BuildContext getParent() {return parent;}
     public Attribute getTransition() {return transition;}
   }
   
   private NDState initialState;
   public NDState getInitialState() {return initialState;}
   public NDAutomaton(DCollection collection) {
     build(collection);
   }
   
   private void split(IIndex index) { 
     Set<Attribute> torefine = new HashSet<>(index.getOutgoing());
     while (! torefine.isEmpty()) {
       Attribute winner = null;
       for(Attribute a: torefine) {
          if (winner == null || index.get(a).size() > index.get(winner).size()) {
              winner = a;
          }  
       }
       torefine.remove(winner);
       Set<DObject> objectsWinner = new HashSet<>(index.get(winner));
       Iterator<Attribute> itorefine = torefine.iterator();
       while (itorefine.hasNext()) {
         Attribute a = itorefine.next();
         List<DObject> currentObjects = index.get(a);
         Iterator<DObject> icurrentObjects = currentObjects.iterator();
         while (icurrentObjects.hasNext()) {
          if (objectsWinner.contains(icurrentObjects.next())) {
              icurrentObjects.remove();
          }
         }
         if (currentObjects.size() == 0) 
             itorefine.remove();
       }            
      }
   }   
   
   private void backpropagate(BuildContext ctx, Attribute a, NDState target) {
     while (ctx != null) {
      ctx.getState().addTransition(a,target); 
      ctx = ctx.getParent();
    } 
   }
   
   private boolean mustBeBackpropagated(BuildContext ctx, Attribute transition) {
       if (ctx.getParent() == null) return false;
       else {
         List<NDState> parentSelfTransitions = ctx.getParent().getState().transit(transition);
         return parentSelfTransitions == null ||
              ! (ctx.getTransition() == transition
                     ||   
                 parentSelfTransitions.size() == 1 &&
                 parentSelfTransitions.get(0) == ctx.getParent().getState());
       }  
   }
   
   private void build(DCollection collection) {
     List<BuildContext> pending = new LinkedList<>();  
     this.initialState = new NDState(collection.getObjects());
     pending.add(new BuildContext(initialState));
     while (! pending.isEmpty()) {
       BuildContext currentCtx = pending.remove(0);
       IIndex currentIndex = new IIndex(currentCtx.getState().getObjects());
       for(Attribute a: currentIndex.getLoop())  {
           currentCtx.getState().addTransition(a, currentCtx.getState());
           if (mustBeBackpropagated(currentCtx,a)) {
               backpropagate(currentCtx.getParent(),a,currentCtx.getState());
           }
       }
       split(currentIndex);
       for(Attribute a: currentIndex.getOutgoing()) {
           List<DObject> targetObjects = currentIndex.get(a);
           if (targetObjects.size() > 0) {
             BuildContext targetCtx = new BuildContext(new NDState(targetObjects),currentCtx,a);
             pending.add(targetCtx);
             backpropagate(currentCtx,a,targetCtx.getState());  
          }
       }       
     }
   } 
  } 
