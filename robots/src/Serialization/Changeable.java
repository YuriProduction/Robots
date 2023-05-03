package Serialization;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.prefs.BackingStoreException;

public interface Changeable {

  public void load() throws PropertyVetoException;

  public void save() throws IOException, BackingStoreException;


}
