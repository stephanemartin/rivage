package fr.inria.rivage.elements.interfaces;


import fr.inria.rivage.engine.concurrency.tools.ID;
import java.io.Serializable;

public interface ITreeElement extends Serializable{
  public IGroup getParent();
  public void setParent(IGroup parent);
  public ID getId();
}
