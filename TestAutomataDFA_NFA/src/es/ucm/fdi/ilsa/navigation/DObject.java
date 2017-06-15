package es.ucm.fdi.ilsa.navigation;

import java.util.HashSet;
import java.util.Set;

public class DObject {
  private Set<Attribute> attributes;
  private static long ID=0;
  private long id;
  public DObject(Set<Attribute> as) {
    this.attributes = as;
    this.id = ID++;
  }  
  public Set<Attribute> getAttributes() {return attributes;}
  public String toString() {return "#"+id+":"+attributes;}
  //public String toString() {return "#"+id;}
}
