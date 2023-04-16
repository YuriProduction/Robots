package Serialization;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesDemo {

  public static void main(String[] args) {

    Preferences userPrefs = Preferences.userNodeForPackage(PreferencesDemo.class);
    try {
      String[] keys = userPrefs.keys();

      if (keys == null || keys.length == 0) {
        userPrefs.put("hostname", "ura!!!");
        userPrefs.putInt("port", 12345);
        userPrefs.putBoolean("authentication", true);
        userPrefs.putLong("timeout", 90000);
        OutputStream osTree = new BufferedOutputStream(
            new FileOutputStream(System.getProperty("user.home") + "\\Tree.xml"));
        userPrefs.exportSubtree(osTree);
        osTree.close();
      } else {
        String hostname = userPrefs.get("hostname", "hz kakoe znachenie");
        int port = userPrefs.getInt("port", 80);
        boolean authentication = userPrefs.getBoolean("authentication", false);
        long timeout = userPrefs.getLong("timeout", 20000);

        String username = userPrefs.get("username", "tom");

        System.out.println(hostname);
        System.out.println(port);
        System.out.println(authentication);
        System.out.println(timeout);
        System.out.println(username);
        userPrefs.removeNode();
      }
    } catch (BackingStoreException ex) {
      System.err.println(ex);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
