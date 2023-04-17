package Serialization;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.prefs.BackingStoreException;

public interface SaveableAndLoadable {

  public void loadData();

  public void saveData() throws IOException, BackingStoreException;

}
