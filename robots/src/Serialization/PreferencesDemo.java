package Serialization;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PreferencesDemo {

  public static void mainn() {

    Preferences userPrefs = Preferences.userNodeForPackage(PreferencesDemo.class);
    try {
      String[] keys = userPrefs.keys();

      if (keys == null || keys.length == 0) {
        userPrefs.putInt("X", 1000);
        userPrefs.putInt("Y", 2000);
        userPrefs.putInt("width", 12345);
        userPrefs.putInt("height", 12313);
        userPrefs.putBoolean("authentication", true);
        OutputStream osTree = new BufferedOutputStream(
            new FileOutputStream(System.getProperty("user.home") + "\\Tree.xml"));
        userPrefs.exportSubtree(osTree);
        osTree.close();
      } else {
        int X = userPrefs.getInt("X", 200);
        int Y = userPrefs.getInt("Y", 80);
        boolean authentication = userPrefs.getBoolean("authentication", false);
        int width = userPrefs.getInt("width", 200);
        int height = userPrefs.getInt("height", 80);

        System.out.println(X);
        System.out.println(Y);
        System.out.println(authentication);
        System.out.println(width);
        System.out.println(height);
        userPrefs.removeNode();
      }
    } catch (BackingStoreException ex) {
      System.err.println(ex);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws Exception {

  }

  public void readXML(String name) {
    try {
      mainn();
      File xmlFile = new File(System.getProperty("user.home") + "\\" + name);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();
      NodeList nList = doc.getElementsByTagName("entry");
      for (int temp = 0; temp < nList.getLength(); temp++) {
        Node nNode = nList.item(temp);
        System.out.println("\nCurrent Element :" + nNode.getNodeName());
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) nNode;
          System.out.println("Value : " + eElement.getAttribute("value"));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
