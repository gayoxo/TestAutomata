package es.ucm.fdi.ilsa.navigation;

public class Attribute {
   private long attr;
   public Attribute(long attr) {
     this.attr = attr;  
   }
   public long getID() {return attr;}
   public int hashCode() {return (int)attr;}
   public boolean equals(Object o) {
      if (o instanceof Attribute) return ((Attribute)o).attr == attr;
      return false;
   }
   public String toString() {return "@"+attr;}
}
