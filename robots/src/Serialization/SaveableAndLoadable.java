package Serialization;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.prefs.BackingStoreException;

public interface SaveableAndLoadable {

  public void loadData() throws PropertyVetoException;

  public void saveData() throws IOException, BackingStoreException;

}
