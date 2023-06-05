package Serialization;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.prefs.BackingStoreException;

public interface Changeable {

  void load() throws PropertyVetoException;

  void save() throws IOException, BackingStoreException;

}
