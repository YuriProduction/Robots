package Serialization;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PreferencesDemo {

  public DataOfFrame readXML(String name) {
    Node nNode;
    try {
      //дефолтные значения
      boolean isIcon = false;
      int X = 10, Y = 10;
      int width = 500, height = 500;
      File xmlFile = new File(System.getProperty("user.home") + "\\Preferencess\\" + name + ".xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      System.out.println(name);
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();
      NodeList nList = doc.getElementsByTagName("entry");
      for (int temp = 0; temp < nList.getLength(); temp++) {
        nNode = nList.item(temp);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) nNode;
          if (eElement.getAttribute("key").equals("X")) {
            X = Integer.parseInt(eElement.getAttribute("value"));
          }
          if (eElement.getAttribute("key").equals("Y")) {
            Y = Integer.parseInt(eElement.getAttribute("value"));
          }
          if (eElement.getAttribute("key").equals("Icon")) {
            isIcon = Boolean.parseBoolean(eElement.getAttribute("value"));
          }
          if (eElement.getAttribute("key").equals("width")) {
            width = Integer.parseInt(eElement.getAttribute("value"));
          }
          if (eElement.getAttribute("key").equals("height")) {
            height = Integer.parseInt(eElement.getAttribute("value"));
          }
        }
      }
      return new DataOfFrame(X, Y, width, height, isIcon);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new DataOfFrame(10, 10, 300, 300, false);
  }
}
